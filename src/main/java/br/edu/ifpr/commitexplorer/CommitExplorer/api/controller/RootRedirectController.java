package br.edu.ifpr.commitexplorer.CommitExplorer.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootRedirectController {

    @GetMapping("/")
    public String index() {
        return "forward:/swagger-ui.html";
    }
}