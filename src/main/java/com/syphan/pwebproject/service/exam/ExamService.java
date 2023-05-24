package com.syphan.pwebproject.service.exam;

import com.syphan.pwebproject.model.dto.ExamDto;
import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.model.entity.StartedExam;
import com.syphan.pwebproject.service.GenericService;

import java.util.List;

public interface ExamService extends GenericService<ExamDto> {

    StartedExam startExam(long examId, long userId);

    List<ExamDto> findAllByUser(UserDto user);

    void updateExamStatus(ExamDto examDto);
}
