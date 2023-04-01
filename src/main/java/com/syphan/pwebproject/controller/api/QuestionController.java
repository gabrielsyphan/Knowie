package com.syphan.pwebproject.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class QuestionController {

    @GetMapping("/questions")
    public String getAllQuestions(Model model) {
        log.info("QuestionController - getAllQuestions: Get all questions");
        model.addAttribute("route", "questions");
        return "questions/all";
    }

    @GetMapping("/questions/new")
    public String newQuestion(Model model) {
        log.info("QuestionController - newQuestion: New question");
        model.addAttribute("route", "questions");
        return "questions/new";
    }
}
