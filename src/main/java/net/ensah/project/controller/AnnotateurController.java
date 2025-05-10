package net.ensah.project.controller;

import lombok.extern.slf4j.Slf4j;
import net.ensah.project.entity.CoupleText;
import net.ensah.project.entity.Tache;
import net.ensah.project.service.IAnnotateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String work(@RequestParam(name="id") Long id,
                       @RequestParam(defaultValue = "0") int index,
                       Principal principal,
                       Model model) {
        Tache task = service.getTaskById(id);
        if (task == null || task.getCouples().isEmpty()) {
            return "redirect:/user/work";
        }
        int size = task.getCouples().size();
        if (index < 0) index = 0;
        if (index >= size) index = size - 1;
        CoupleText couple = task.getCouples().get(index);
        boolean already = service.existsByCoupleId(couple.getId());
        model.addAttribute("task", task);
        model.addAttribute("couple", couple);
        model.addAttribute("index", index);
        model.addAttribute("maxIndex", size - 1);
        model.addAttribute("alreadyAnnotated", already);
          return "user/work";
    }


    @PostMapping("/task/validate")
    public String validate(@RequestParam("taskId") Long taskId,
                           @RequestParam("coupleId") long coupleId,
                           @RequestParam("similarity") String similarity,
                           @RequestParam("index")    int    index,
                           Principal principal) {
        service.validate(taskId,coupleId,similarity,index,principal);
        int nextIndex = index + 1;
        return "redirect:/annotation/work?id=" + taskId + "&index=" + nextIndex;
    }

}
