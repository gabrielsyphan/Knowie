package com.syphan.pwebproject.repository;

import com.syphan.pwebproject.model.entity.Exam;
import com.syphan.pwebproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    Optional<Exam> findByCode(String code);

    List<Exam> findAllByOwner(User user);
}
