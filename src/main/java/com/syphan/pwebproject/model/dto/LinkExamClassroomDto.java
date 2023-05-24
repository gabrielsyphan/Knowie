package com.syphan.pwebproject.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkExamClassroomDto {

    @NotNull
    @Positive
    private Long classroomId;

    @NotNull
    @NotEmpty
    private String examCode;
}
