package io.olkkani.question.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("question")
@Controller
class QuestionController {

    @GetMapping("register")
    fun register () : String {
        return "pages/question/register"
    }

}