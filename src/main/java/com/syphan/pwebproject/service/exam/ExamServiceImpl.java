package com.syphan.pwebproject.service.exam;

import com.syphan.pwebproject.model.dto.ExamDto;
import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.model.entity.ExamEntity;
import com.syphan.pwebproject.model.entity.QuestionEntity;
import com.syphan.pwebproject.model.entity.StartedExamEntity;
import com.syphan.pwebproject.model.entity.UserEntity;
import com.syphan.pwebproject.model.mapper.ExamMapper;
import com.syphan.pwebproject.model.mapper.UserMapper;
import com.syphan.pwebproject.repository.ExamRepository;
import com.syphan.pwebproject.repository.QuestionRepository;
import com.syphan.pwebproject.repository.StartedExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    private final QuestionRepository questionRepository;

    private final StartedExamRepository startedExamRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository, StartedExamRepository startedExamRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.startedExamRepository = startedExamRepository;
    }

    @Override
    public ExamDto create(ExamDto obj) throws Exception {
        try {
            ExamEntity examEntity = ExamMapper.INSTANCE.dtoToEntity(obj);

            if(obj.getQuestions().size() == 0) {
                throw new Exception("ExamServiceImpl - create: Exam must have at least 1 question");
            }

            List<QuestionEntity> questionEntities = obj.getQuestions().stream().map(questionDto -> {
                return this.questionRepository.findById(questionDto.getId()).orElseThrow(
                        () -> new RuntimeException("ExamServiceImpl - create: Question not found")
                );
            }).toList();

            examEntity.setQuestions(questionEntities);

            if(examEntity.getId() != null) {
                ExamEntity examEntityFound = this.examRepository.findById(examEntity.getId()).orElseThrow(
                        () -> new Exception("ExamServiceImpl - create/update: Exam not found")
                );
                ExamEntity examEntityUpdated = ExamMapper.INSTANCE.updateEntity(examEntity, examEntityFound);
                return ExamMapper.INSTANCE.entityToDto(this.examRepository.saveAndFlush(examEntityUpdated));
            }

            ExamEntity examSaved = this.examRepository.save(examEntity);
            return ExamMapper.INSTANCE.entityToDto(examSaved);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ExamServiceImpl - create: Error when create exam: {}", e);
        }
    }

    @Override
    public void delete(long id) {
        try {
            Optional<ExamEntity> examEntity = this.examRepository.findById(id);
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
        List<ExamEntity> examEntities = this.examRepository.findAll();
        List<ExamDto> examsDto = examEntities.stream().map(ExamMapper.INSTANCE::entityToDto).toList();
        examsDto.forEach(examDto -> {
            this.updateExamStatus(examDto);
        });
        return examsDto;
    }

    private void updateExamStatus(ExamDto examDto) {
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
    public StartedExamEntity startExam(long examId, long userId) {
        UserEntity userEntity = UserMapper.INSTANCE.dtoToEntity(UserDto.builder().id(userId).build());
        ExamEntity examEntity = ExamEntity.builder().id(examId).build();

        Optional<StartedExamEntity> startedExamEntityFound = this.startedExamRepository.findByExamAndUser(examEntity, userEntity);
        if(startedExamEntityFound.isEmpty()) {
            StartedExamEntity startedExamEntity = StartedExamEntity.builder()
                    .exam(examEntity)
                    .user(userEntity)
                    .startedAt(LocalDateTime.now())
                    .build();

            return this.startedExamRepository.save(startedExamEntity);
        }

        return startedExamEntityFound.get();
    }
}
