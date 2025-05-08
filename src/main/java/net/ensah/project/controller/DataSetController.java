package net.ensah.project.controller;

import lombok.extern.slf4j.Slf4j;
import net.ensah.project.dtos.DataSetDto;
import net.ensah.project.entity.CoupleText;
import net.ensah.project.entity.DataSet;
import net.ensah.project.exception.InvalidFileException;
import net.ensah.project.service.IDataSetService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
         try{
             service.isSupportedFile(request);
             log.info(request.description());
             model.addAttribute("message","Fichier téléchargé avec succès ! ");
             return "redirect:/home";
         }catch (InvalidFileException e){
             model.addAttribute("message", e.getMessage());
             return "admin/add-data";
         }
    }


        @GetMapping("/show")
        public String show (Model model) {
            List<DataSet> all = service.getAllDataSet();
            model.addAttribute("dataSets", all);
            return "admin/show";
        }

        @GetMapping("/details")
        public String details (@RequestParam(name="id") Long id,
                               @RequestParam(name="page",defaultValue = "0") int page,
                               @RequestParam(name="size",defaultValue = "10") int size,
                               Model model) {
            Page<CoupleText> pg = service.getDetails(id, page, size);
            model.addAttribute("data", service.getDataSetById(id));
            model.addAttribute("couplesPage", pg.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pg.getTotalPages());
            model.addAttribute("datasetId", id);
            return "/admin/details";
        }

        @GetMapping("/annotateurs")
        public String annotateurs (Model model) {
           return "admin/annotateurs";
        }


}
