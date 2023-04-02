package com.syphan.pwebproject.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer difficulty;

    @ElementCollection
    private List<String> tags;

    private Integer answerType;

    @ElementCollection
    private List<String> answerValues;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    private UserEntity user;
}
