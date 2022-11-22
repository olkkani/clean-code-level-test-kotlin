package io.olkkani.cleancodeleveltest

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class MainController {

    @RequestMapping("main")
    fun main () : String {
        return "pages/main"
    }
}