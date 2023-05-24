package com.syphan.pwebproject.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollDto {

    @NotNull
    private Long userId;

    @NotNull
    private String classroomCode;
}
