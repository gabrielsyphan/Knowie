package com.syphan.pwebproject.repository;

import com.syphan.pwebproject.model.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
}
