package com.syphan.pwebproject.model.dto;

import com.syphan.pwebproject.model.entity.Answer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @Positive
    private Integer difficulty;

    @NotNull
    @Min(1)
    private List<String> tags;

    @NotNull
    @Positive
    private Integer answerType;

    private List<Answer> answers;

    private boolean trueOrFalse;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Valid
    private UserDto user;
}
