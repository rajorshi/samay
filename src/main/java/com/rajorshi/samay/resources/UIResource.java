package com.rajorshi.samay.resources;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping(value = "/ui")
public class UIResource {

    @GetMapping("/dashboard")
    public String index(Model model) {
        return "index.html";
    }

    @GetMapping("/api-docs")
    public String doc(Model model) {
        return "index.html";
    }

}
