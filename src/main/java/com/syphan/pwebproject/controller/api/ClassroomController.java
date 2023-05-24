package com.syphan.pwebproject.controller.api;

import com.syphan.pwebproject.constants.PathConstants;
import com.syphan.pwebproject.model.dto.ClassroomDto;
import com.syphan.pwebproject.model.dto.EnrollDto;
import com.syphan.pwebproject.model.dto.LinkExamClassroomDto;
import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.service.classroom.ClassroomService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@Slf4j
public class ClassroomController {

    private final ClassroomService classroomService;

    @Autowired
    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping("/classrooms")
    public String getAllClassrooms(Model model, HttpSession session) throws Exception {
        log.info("ClassroomController - getAllClassrooms: Get all classrooms");
        UserDto owner = (UserDto) session.getAttribute("user");
        List<ClassroomDto> classroomDtos = this.classroomService.findAllByOwner(owner);
        model.addAttribute("route", "classrooms");
        model.addAttribute("classrooms", classroomDtos);
        return "classrooms/all";
    }

    @GetMapping("/classrooms/new")
    public String newClassroom(Model model) {
        log.info("ClassroomController - newClassroom: New classroom");
        model.addAttribute("route", "classrooms");
        return "classrooms/new";
    }

    @GetMapping("/classrooms/{id}")
    public String getClassroomById(Model model) {
        log.info("ClassroomController - getClassroomById: Get classroom by id");
        ClassroomDto classroomDto = this.classroomService.findById(1L);
        model.addAttribute("route", "classrooms");
        model.addAttribute("classroom", classroomDto);
        return "classrooms/new";
    }

    @PutMapping(value = PathConstants.CLASSROOM, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveClassroom(@RequestBody @Valid ClassroomDto classroomDto, UriComponentsBuilder uriComponentsBuilder, HttpSession session) throws Exception {
        log.info("ClassroomController - saveClassroom: Save classroom");
        ClassroomDto createdClassroom = this.classroomService.create(classroomDto, session);
        URI uri = uriComponentsBuilder.path("classrooms/{id}").buildAndExpand(createdClassroom.getId()).toUri();
        return ResponseEntity.created(uri).body(createdClassroom);
    }

    @DeleteMapping(PathConstants.CLASSROOM + "/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable("id") long id) {
        log.info("ClassroomController - deleteClassroomById: Delete classroom by id");
        this.classroomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = PathConstants.CLASSROOM + "/enroll", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> enrollClassroom(@RequestBody @Valid EnrollDto enrollDto, UriComponentsBuilder uriComponentsBuilder) throws Exception {
        log.info("ClassroomController - enrollClassroom: Enroll classroom");
        ClassroomDto classroomDto = this.classroomService.enroll(enrollDto);
        URI uri = uriComponentsBuilder.path("classrooms/{id}").buildAndExpand(classroomDto.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping(value = PathConstants.CLASSROOM + "/link-exam-classroom", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> linkExamToClassroom(@RequestBody @Valid LinkExamClassroomDto linkExamClassroomDto) {
        log.info("ClassroomController - linkExamToClassroom: Link exam to classroom");
        this.classroomService.linkExamToClassroom(linkExamClassroomDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = PathConstants.JOIN_CLASSROOM, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> joinClassroom(@RequestBody ClassroomDto classroomDto, HttpSession httpSession) throws Exception {
        try {
            log.info("ClassroomController - joinClassroom: Join classroom");
            UserDto user = (UserDto) httpSession.getAttribute("user");
            this.classroomService.join(classroomDto, user);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
