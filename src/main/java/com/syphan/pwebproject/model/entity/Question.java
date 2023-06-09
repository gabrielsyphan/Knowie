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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer difficulty;

    @ElementCollection
    private List<String> tags;

    @Column
    private Integer answerType;

    @ElementCollection
    @Embedded
    private List<Answer> answers;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean trueOrFalse;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @ManyToOne
    private User user;
}
