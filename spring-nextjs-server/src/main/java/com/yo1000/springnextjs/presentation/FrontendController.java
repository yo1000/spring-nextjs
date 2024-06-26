package com.yo1000.springnextjs.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {
    @GetMapping(value = "/**/{path:[^.]*}")
    public String getUi() {
        return "forward:/index.html";
    }
}
