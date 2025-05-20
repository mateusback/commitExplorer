package br.edu.ifpr.commitexplorer.CommitExplorer.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootRedirectController extends BaseController {

    @GetMapping("/")
    public String index() {
        return "forward:/swagger-ui.html";
    }
}