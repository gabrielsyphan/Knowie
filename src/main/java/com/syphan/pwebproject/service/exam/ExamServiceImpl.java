package com.syphan.pwebproject.service.exam;

import com.syphan.pwebproject.model.dto.ExamDto;
import com.syphan.pwebproject.model.entity.ExamEntity;
import com.syphan.pwebproject.model.entity.QuestionEntity;
import com.syphan.pwebproject.model.mapper.ExamMapper;
import com.syphan.pwebproject.repository.ExamRepository;
import com.syphan.pwebproject.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    private final QuestionRepository questionRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public ExamDto create(ExamDto obj) throws Exception {
        try {
            ExamEntity examEntity = ExamMapper.INSTANCE.dtoToEntity(obj);

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
        return examEntities.stream().map(ExamMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public ExamDto findById(long id) {
        return this.examRepository.findById(id).map(ExamMapper.INSTANCE::entityToDto).orElseThrow(
                () -> new RuntimeException("ExamServiceImpl - findById: Exam not found")
        );
    }
}
