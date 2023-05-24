package com.syphan.pwebproject.service.exam;

import com.syphan.pwebproject.model.dto.ExamDto;
import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.model.entity.*;
import com.syphan.pwebproject.model.mapper.ExamMapper;
import com.syphan.pwebproject.model.mapper.UserMapper;
import com.syphan.pwebproject.repository.ClassroomRepository;
import com.syphan.pwebproject.repository.ExamRepository;
import com.syphan.pwebproject.repository.QuestionRepository;
import com.syphan.pwebproject.repository.StartedExamRepository;
import com.syphan.pwebproject.util.StringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    private final QuestionRepository questionRepository;

    private final StartedExamRepository startedExamRepository;

    private final ClassroomRepository classroomRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository, StartedExamRepository startedExamRepository, ClassroomRepository classroomRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.startedExamRepository = startedExamRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    public ExamDto create(ExamDto obj) throws Exception {
        try {
            Exam examEntity = ExamMapper.INSTANCE.dtoToEntity(obj);

            if(obj.getQuestions().size() == 0) {
                throw new Exception("ExamServiceImpl - create: Exam must have at least 1 question");
            }

            List<Question> questionEntities = obj.getQuestions().stream().map(questionDto ->
                    this.questionRepository.findById(questionDto.getId()).orElseThrow(
                        () -> new RuntimeException("ExamServiceImpl - create: Question not found")
            )).toList();

            examEntity.setQuestions(questionEntities);
            examEntity.setCode("EXAM-" + StringGenerator.generateCode(6));

            if(examEntity.getId() != null) {
                Exam examEntityFound = this.examRepository.findById(examEntity.getId()).orElseThrow(
                        () -> new Exception("ExamServiceImpl - create/update: Exam not found")
                );
                Exam examEntityUpdated = ExamMapper.INSTANCE.updateEntity(examEntity, examEntityFound);
                return ExamMapper.INSTANCE.entityToDto(this.examRepository.saveAndFlush(examEntityUpdated));
            }

            Exam examSaved = this.examRepository.save(examEntity);
            return ExamMapper.INSTANCE.entityToDto(examSaved);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ExamServiceImpl - create: Error when create exam: {}", e);
        }
    }

    @Override
    public void delete(long id) {
        try {
            Optional<Exam> examEntity = this.examRepository.findById(id);
            if(examEntity.isPresent()) {
                this.examRepository.delete(examEntity.get());
            } else {
                throw new Exception("ExamServiceImpl - delete: Exam not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ExamServiceImpl - delete: Error when delete exam: {}", e);
        }
    }

    @Override
    public List<ExamDto> findAll() {
        List<Exam> examEntities = this.examRepository.findAll();
        List<ExamDto> examsDto = examEntities.stream().map(ExamMapper.INSTANCE::entityToDto).toList();
        examsDto.forEach(this::updateExamStatus);
        return examsDto;
    }

    @Override
    public void updateExamStatus(ExamDto examDto) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if(examDto.getEndDateTime().isBefore(currentDateTime)) {
            // Exam is finished
            examDto.setStatus(2);
        } else if(examDto.getStartDateTime().isBefore(currentDateTime) && examDto.getEndDateTime().isAfter(currentDateTime)) {
            // Exam is started
            examDto.setStatus(1);
        } else {
            // Exam is not started
            examDto.setStatus(0);
        }
    }

    @Override
    public ExamDto findById(long id) {
        ExamDto examDto = this.examRepository.findById(id).map(ExamMapper.INSTANCE::entityToDto).orElseThrow(
                () -> new RuntimeException("ExamServiceImpl - findById: Exam not found")
        );

        this.updateExamStatus(examDto);
        return examDto;
    }

    @Override
    public StartedExam startExam(long examId, long userId) {
        User userEntity = UserMapper.INSTANCE.dtoToEntity(UserDto.builder().id(userId).build());
        Exam examEntity = Exam.builder().id(examId).build();

        Optional<StartedExam> startedExamEntityFound = this.startedExamRepository.findByExamAndUser(examEntity, userEntity);
        if(startedExamEntityFound.isEmpty()) {
            StartedExam startedExamEntity = StartedExam.builder()
                    .exam(examEntity)
                    .user(userEntity)
                    .startedAt(LocalDateTime.now())
                    .build();

            return this.startedExamRepository.save(startedExamEntity);
        }

        return startedExamEntityFound.get();
    }

    @Override
    public List<ExamDto> findAllByUser(UserDto user) {
        if(user.getUserType() != 1) {
            return this.examRepository.findAllByOwner(
                    UserMapper.INSTANCE.dtoToEntity(user)
            ).stream().map(ExamMapper.INSTANCE::entityToDto).toList();
        }

        List<Classroom> classrooms = this.classroomRepository.findAllByUsers(
                UserMapper.INSTANCE.dtoToEntity(user)
        );

        List<Exam> examEntities = new ArrayList<>();
        classrooms.forEach(classroom -> {
            examEntities.addAll(classroom.getExams());
        });

        List<ExamDto> exams = examEntities.stream().map(ExamMapper.INSTANCE::entityToDto).toList();
        exams.forEach(this::updateExamStatus);

        return exams;
    }
}
