package com.danlju.tulip.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProjectWebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/project/{id}")
    public String project(@PathVariable String id, Model model) {

        model.addAttribute("projectId", id);

        return "project";
    }

}