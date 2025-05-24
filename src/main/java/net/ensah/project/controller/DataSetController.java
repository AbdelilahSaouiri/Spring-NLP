package net.ensah.project.controller;

import lombok.extern.slf4j.Slf4j;
import net.ensah.project.dtos.AnnotateurDtoRequest;
import net.ensah.project.dtos.DataSetDto;
import net.ensah.project.dtos.UpdateAnnotateursDto;
import net.ensah.project.entity.Annotateur;
import net.ensah.project.entity.CoupleText;
import net.ensah.project.entity.DataSet;
import net.ensah.project.exception.InvalidFileException;
import net.ensah.project.service.IDataSetService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class DataSetController {


    private final IDataSetService service;

    public DataSetController(IDataSetService service) {
        this.service = service;
    }

    @GetMapping("/data")
    public String data() {
        return "admin/add-data";
    }

    @PostMapping("/dataset")
    public String upload(@ModelAttribute DataSetDto request, Model model) {
        try {
            service.isSupportedFile(request);
            log.info(request.description());
            model.addAttribute("message", "Fichier téléchargé avec succès ! ");
            return "redirect:/home";
        } catch (InvalidFileException e) {
            model.addAttribute("message", e.getMessage());
            return "admin/add-data";
        }
    }


    @GetMapping({"/show", "/affectation"})
    public String show(Model model) {
        List<DataSet> all = service.getAllDataSet();
          List<Double> avancements= service.calculateAvancementforEachDataSet(all);
        model.addAttribute("dataSets", all);
        model.addAttribute("avancements", avancements);
        return "admin/show";
    }

    @GetMapping("/details")
    public String details(@RequestParam(name = "id") Long id,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "size", defaultValue = "10") int size,
                          Model model) {
        Page<CoupleText> pg = service.getDetails(id, page, size);
        List<Annotateur> annotateurs = service.getAnnotateursByDataSetId(id);
        DataSet data = service.getDataSetById(id);
        double percent = service.calculateAvancement(data);
        model.addAttribute("percent", percent);
        model.addAttribute("annotateurs", annotateurs);
        model.addAttribute("data",data);
        model.addAttribute("couplesPage", pg.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pg.getTotalPages());
        model.addAttribute("datasetId", id);
        return "admin/details";
    }


    @GetMapping("/add-annotators")
    public String addAnnotateur(@RequestParam(name = "id") Long id, Model model) {
        List<Annotateur> allAnnotateurs = service.getAllAnnotateurs();
        List<Annotateur> assignedAnnotateurs = service.getAnnotateursByDataSetId(id);
        model.addAttribute("annotateurs", allAnnotateurs);
        model.addAttribute("assignedAnnotateurs",assignedAnnotateurs);
        model.addAttribute("dataSetId", id);
        return "admin/affecter-annotateur";
    }

    @PostMapping("/affecter-annotateurs")
    public String affecterAnnotateurToDataset(
            @RequestParam(value = "ids") List<Long> ids,
            @RequestParam("dataId") Long dataSetId
            ) {
        log.info("dataset id {}", dataSetId);
        log.info("ids {}", ids);
        service.affecterAnnotateursToDataset(ids, dataSetId);
        return "redirect:/admin/show";
    }

    @PostMapping("/dataset/annotateur")
    public String supprimerAnnotateur(
            @RequestParam("datasetId") Long dataSetId,
            @RequestParam("annotateurId") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        service.supprimerAnnotateurFromDataSet(dataSetId,id);
        return "redirect:/admin/details?id=" + dataSetId + "&page=" + page + "&size=10";
    }

    @GetMapping("/annotateurs")
    public String annotateurs(Model model) {
        List<Annotateur> allAnnotateurs = service.getAllAnnotateurs();
        model.addAttribute("annotateursList", allAnnotateurs);
        return "admin/annotateurs";
    }


    @GetMapping("/annotateurs/new")
    public String newAnnotateur(Model model) {
        model.addAttribute("annotateur", new AnnotateurDtoRequest("", "", ""));
        return "admin/new-annotateur";
    }

    @PostMapping("/annotateurs/save")
    public String addAnnotateur(@ModelAttribute(name = "annotateur") AnnotateurDtoRequest request,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("annotateur", new AnnotateurDtoRequest("", "", ""));
            return "admin/new-annotateur";
        }
        log.info(request.toString());
        String password = service.saveNewAnnotateur(request);
        redirectAttributes.addFlashAttribute("saved",
                "Annotateur ajouté avec succès. Mot de passe : " + password);
        return "redirect:/admin/annotateurs";
    }

    @PostMapping("/annotateurs/delete")
    public String deleteAnnotateur(@RequestParam(name = "id") Long id) {
        log.info("deleteAnnotateur {}", id);
        service.supprimerAnnotateur(id);
        return "redirect:/admin/annotateurs";
    }


    @GetMapping("/annotateurs/update")
    public String formUpdate(@RequestParam(name = "id") Long id, Model model) {
        UpdateAnnotateursDto annotateur = service.getAnnotateursById(id);
        model.addAttribute("id", id);
        model.addAttribute("annotateur", annotateur);
        log.info("avant {}", annotateur);
        return "admin/update-annotateur";
    }

    @PostMapping("/annotateurs/update")
    public String updateAnnotateur(@RequestParam(name = "id") Long id,
                                   @ModelAttribute(name = "annotateur") UpdateAnnotateursDto annotateur,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/update-annotateur";
        }
        String password = service.updateAnnotateur(id, annotateur);
        if (!password.isEmpty()) {
            redirectAttributes.addFlashAttribute("updated", "Annotateur modifié avec succès. (new password) " + password);
        } else {
            redirectAttributes.addFlashAttribute("updated", "Annotateur modifié avec succès.");
        }
        return "redirect:/admin/annotateurs";
    }

}
