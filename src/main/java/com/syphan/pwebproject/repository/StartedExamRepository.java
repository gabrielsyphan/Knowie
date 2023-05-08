package com.syphan.pwebproject.repository;

import com.syphan.pwebproject.model.entity.ExamEntity;
import com.syphan.pwebproject.model.entity.StartedExamEntity;
import com.syphan.pwebproject.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StartedExamRepository extends JpaRepository<StartedExamEntity, Long> {
    Optional<StartedExamEntity> findByExamAndUser(ExamEntity examEntity, UserEntity userEntity);
}
