package com.congress.controller;

import com.congress.entity.Congress;
import com.congress.repository.CongressRepository;
import com.congress.storage.StorageFileNotFoundException;
import com.congress.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class CongressController {

    @Autowired
    private CongressRepository congressRepository;

    private StorageService storageService;

    @Autowired
    public void FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    /*
        CRUD Congress
    */

    /**
     * This controller display the list of congress
     *
     * @param model
     * @return Template of congress view list
     */
    @GetMapping("/congress")
    public String getCongress(Model model) {
        model.addAttribute("congressList", congressRepository.findAll());
        return "congressList";
    }

    /**
     * This controller display the congress with specific {id}
     *
     * @param id    The id of the congress
     * @param model
     * @return Template of congress view
     * @throws Exception
     */
    @GetMapping("/congress/{id}")
    public String getCongress(@PathVariable long id, Model model) throws Exception {
        Optional<Congress> finded = congressRepository.findById(id);
        Congress currentCongress;
        if (finded.isPresent())
            currentCongress = finded.get();
        else
            throw new Exception("Can't find congress with id=" + id);
        model.addAttribute("currentCongress", currentCongress);
        return "congress";
    }

    /**
     * This controller is used to create a congress
     *
     * @param currentCongress The model of the congress
     * @param model
     * @return Redirect to the congress view
     */
    @PostMapping("/congress")
    public String createCongress(@ModelAttribute Congress currentCongress, Model model) {
        storageService.store(currentCongress.getLogo());
        storageService.store(currentCongress.getBanner());
        currentCongress.setLogo_url("/files/" + currentCongress.getLogo().getOriginalFilename());
        currentCongress.setBanner_url("/files/" + currentCongress.getBanner().getOriginalFilename());
        currentCongress = congressRepository.save(currentCongress);
        return "redirect:/congress/" + currentCongress.getId();
    }

    /**
     * This controller is used to update a congress
     *
     * @param id              The id of the updated congress
     * @param currentCongress The model of the congress
     * @return Redirect to the congress view
     */
    @PutMapping("/congress/{id}")
    public String updateCongress(@PathVariable long id, @ModelAttribute Congress currentCongress) {
        storageService.store(currentCongress.getLogo());
        storageService.store(currentCongress.getBanner());
        currentCongress.setLogo_url("/files/" + currentCongress.getLogo().getOriginalFilename());
        currentCongress.setBanner_url("/files/" + currentCongress.getBanner().getOriginalFilename());
        currentCongress = congressRepository.save(currentCongress);
        return "redirect:/congress/" + currentCongress.getId();
    }

    /**
     * This controller is used to delete a congress
     *
     * @param id              The id of the congress
     * @param currentCongress The model of the congress
     * @return Redirect to the home page
     */
    @DeleteMapping("/congress/{id}")
    public String deleteCongress(@PathVariable long id, @ModelAttribute Congress currentCongress) {
        congressRepository.delete(currentCongress);
        return "redirect:/";
    }

    /**
     * This controller is used to display the congress update form
     *
     * @param id    The id of the congress
     * @param model
     * @return Template of congress update view form
     * @throws Exception
     */
    @GetMapping("/congress/{id}/edit")
    public String updateCongressForm(Long id, Model model) throws Exception {
        Congress newCongress;
        Optional<Congress> finded = congressRepository.findById(id);
        if (finded.isPresent()) {
            newCongress = finded.get();
        } else {
            throw new Exception("Can't find congress with id=" + id);
        }
        model.addAttribute("httpMethod", "PUT");
        model.addAttribute("pathMethod", "/congress/" + id);
        model.addAttribute("newCongress", newCongress);
        return "createUpdate";
    }

    /**
     * This controller is used to display the congress creation form
     *
     * @param model
     * @return Template of congress creation view form
     * @throws Exception
     */
    @GetMapping("/congress/create")
    public String createCongressForm(Model model) throws Exception {
        model.addAttribute("httpMethod", "POST");
        model.addAttribute("pathMethod", "/congress");
        model.addAttribute("newCongress", new Congress());
        return "createUpdate";
    }

    /*
        STORAGE Management
    */

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
