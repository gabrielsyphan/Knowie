package com.syphan.pwebproject.service.classroom;

import com.syphan.pwebproject.model.dto.ClassroomDto;
import com.syphan.pwebproject.model.dto.EnrollDto;
import com.syphan.pwebproject.model.dto.LinkExamClassroomDto;
import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.service.GenericService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface ClassroomService extends GenericService<ClassroomDto> {

    ClassroomDto enroll(EnrollDto enrollDto) throws Exception;

    ClassroomDto create(ClassroomDto classroomDto, HttpSession session) throws Exception;

    List<ClassroomDto> findAllByOwner(UserDto user) throws Exception;

    void join(ClassroomDto classroomDto, UserDto userDto) throws Exception;

    void linkExamToClassroom(LinkExamClassroomDto linkExamClassroomDto);
}
