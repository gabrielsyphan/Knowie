package com.syphan.pwebproject.service.question;

import com.syphan.pwebproject.model.dto.QuestionDto;
import com.syphan.pwebproject.model.entity.QuestionEntity;
import com.syphan.pwebproject.model.mapper.QuestionMapper;
import com.syphan.pwebproject.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public QuestionDto create(QuestionDto obj) throws Exception {
        try {
            QuestionEntity questionEntity = QuestionMapper.INSTANCE.dtoToEntity(obj);

            if(questionEntity.getId() != null) {
                QuestionEntity userEntityFound = this.questionRepository.findById(questionEntity.getId()).orElseThrow(
                        () -> new Exception("QuestionServiceImpl - create/update: Question not found")
                );
                QuestionEntity questionEntityUpdated = QuestionMapper.INSTANCE.updateEntity(questionEntity, userEntityFound);
                return QuestionMapper.INSTANCE.entityToDto(this.questionRepository.saveAndFlush(questionEntityUpdated));
            }

            QuestionEntity questionSaved = this.questionRepository.save(questionEntity);
            return QuestionMapper.INSTANCE.entityToDto(questionSaved);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("QuestionServiceImpl - create: Error when create question: {}", e);
        }
    }

    @Override
    public void delete(long id) {
        try {
            Optional<QuestionEntity> questionEntity = this.questionRepository.findById(id);
            if(questionEntity.isPresent()) {
                this.questionRepository.delete(questionEntity.get());
            } else {
                throw new Exception("QuestionServiceImpl - delete: Question not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("QuestionServiceImpl - delete: Error when delete question: {}", e);
        }
    }

    @Override
    public List<QuestionDto> findAll() {
        List<QuestionEntity> questionEntities = this.questionRepository.findAll();
        return questionEntities.stream().map(QuestionMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public QuestionDto findById(long id) {
        return this.questionRepository.findById(id).map(QuestionMapper.INSTANCE::entityToDto).orElseThrow(
                () -> new RuntimeException("QuestionServiceImpl - findById: Question not found")
        );
    }
}
