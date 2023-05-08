package com.syphan.pwebproject.controller.api;

import com.syphan.pwebproject.constants.PathConstants;
import com.syphan.pwebproject.model.dto.ExamDto;
import com.syphan.pwebproject.model.dto.QuestionDto;
import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.model.entity.StartedExamEntity;
import com.syphan.pwebproject.service.exam.ExamService;
import com.syphan.pwebproject.service.question.QuestionService;
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
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
public class ExamController {

    private final QuestionService questionService;

    private final ExamService examService;

    @Autowired
    public ExamController(QuestionService questionService, ExamService examService) {
        this.questionService = questionService;
        this.examService = examService;
    }

    @GetMapping("/exams")
    public String getAllExams(Model model) {
        log.info("ExamController - getAllExams: Get all exams");
        List<ExamDto> exams = this.examService.findAll();
        model.addAttribute("route", "exams");
        model.addAttribute("exams", exams);
        return "exams/all";
    }

    @GetMapping("/exams/{id}")
    public String getExamById(@PathVariable("id") long id, Model model) {
        log.info("ExamController - getExamById: Get exam with id: {}", id);
        ExamDto examDto = this.examService.findById(id);
        model.addAttribute("exam", examDto);
        model.addAttribute("route", "exams");
        return "exams/details";
    }

    @GetMapping("/exams/new")
    public String newExam(Model model) {
        log.info("ExamController - newExam: New exam");
        List<QuestionDto> questionDtoList = this.questionService.findAll();
        model.addAttribute("route", "exams");
        model.addAttribute("questions", questionDtoList.stream().peek(questionDto -> questionDto.setUser(null)).toList());
        return "exams/new";
    }

    @PutMapping(value = PathConstants.EXAMS, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveExam(@RequestBody @Valid ExamDto examDto, UriComponentsBuilder uriComponentsBuilder) throws Exception {
        log.info("ExamController - saveExam: Save exam");
        ExamDto createdEXAM = this.examService.create(examDto);
        URI uri = uriComponentsBuilder.path("exams/{id}").buildAndExpand(createdEXAM.getId()).toUri();
        return ResponseEntity.created(uri).body(createdEXAM);
    }

    @DeleteMapping(PathConstants.EXAMS + "/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable("id") long id) {
        log.info("ExamController - deleteExam: Delete exam with id: {}", id);
        this.examService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = PathConstants.EXAMS + "/{id}", produces = "application/json")
    public ResponseEntity<ExamDto> getExamByIdApi(@PathVariable("id") long id) {
        log.info("ExamController - getExamByIdApi: Get exam with id: {}", id);
        ExamDto examDto = this.examService.findById(id);
        return ResponseEntity.ok(examDto);
    }

    @GetMapping(value = PathConstants.EXAMS + "/start/{examId}/{userId}", produces = "application/json")
    public ResponseEntity<LocalDateTime> startExam(@PathVariable("examId") long examId, @PathVariable("userId") long userId) {
        log.info("ExamController - startExam: Start exam with id: {}", examId);
        StartedExamEntity startedExamEntity = this.examService.startExam(examId, userId);
        return ResponseEntity.ok(startedExamEntity.getStartedAt());
    }
}
