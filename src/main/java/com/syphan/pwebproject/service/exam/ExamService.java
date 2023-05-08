package com.syphan.pwebproject.service.exam;

import com.syphan.pwebproject.model.dto.ExamDto;
import com.syphan.pwebproject.model.entity.StartedExamEntity;
import com.syphan.pwebproject.service.GenericService;

public interface ExamService extends GenericService<ExamDto> {

    StartedExamEntity startExam(long examId, long userId);
}
