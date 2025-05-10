package net.ensah.project.controller;

import lombok.extern.slf4j.Slf4j;
import net.ensah.project.entity.Tache;
import net.ensah.project.service.IAnnotateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/annotation")
public class AnnotateurController {

    private final IAnnotateurService service;

    public AnnotateurController(IAnnotateurService service) {
        this.service = service;
    }


    @GetMapping("/tasks")
    public String tasks(Model model, Principal principal) {
        List<Tache> tasks = service.getTasks(principal);
        model.addAttribute("tasks", tasks);
        return "user/tasks";
    }

    @GetMapping("/work")
    public String work(Model model) {
        return "user/work";
    }
}
