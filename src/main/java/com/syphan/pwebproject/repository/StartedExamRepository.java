package com.syphan.pwebproject.repository;

import com.syphan.pwebproject.model.entity.Exam;
import com.syphan.pwebproject.model.entity.StartedExam;
import com.syphan.pwebproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StartedExamRepository extends JpaRepository<StartedExam, Long> {
    Optional<StartedExam> findByExamAndUser(Exam examEntity, User userEntity);
}
