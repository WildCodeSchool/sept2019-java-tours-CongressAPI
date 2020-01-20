package com.congress.controller;

import com.congress.entity.Congress;
import com.congress.repository.CongressRepository;
import com.congress.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/congress")
public class CongressController {

    @Autowired
    private CongressRepository congressRepository;

    private StorageService storageService;

    @Autowired
    public void FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }


    /*
        CRUD Congress
    */

    /**
     * This controller is used to display the congress creation form
     *
     * @param model
     * @return Template of congress creation view form
     * @throws Exception
     */
    @GetMapping("/create")
    public String createCongressForm(Model model) throws Exception {
        model.addAttribute("pathMethod", "/congress/create");
        model.addAttribute("newCongress", new Congress());
        return "pages/congress/congressFormView";
    }

    /**
     * This controller is used to create a congress
     *
     * @param currentCongress The model of the congress
     * @param model
     * @return Redirect to the congress view
     */
    @PostMapping("/create")
    public String createCongressPost(@Valid @ModelAttribute("newCongress") Congress newCongress, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pathMethod", "/congress/create");
            model.addAttribute("newCongress", newCongress);
            return "pages/congress/congressFormView";
        }
        return updateFileIfPresent(newCongress);
    }

    /**
     * This controller display the congress with specific {id}
     *
     * @param id    The id of the congress
     * @param model
     * @return Template of congress view
     * @throws Exception
     */
    @GetMapping("/{id}")
    public String getCongress(@PathVariable long id, Model model) throws Exception {
        Optional<Congress> finded = congressRepository.findById(id);
        Congress currentCongress;
        if (finded.isPresent())
            currentCongress = finded.get();
        else
            throw new Exception("Can't find congress with id=" + id);
        model.addAttribute("currentCongress", currentCongress);
        model.addAttribute("pageTitle", "Congress" + currentCongress.getName());
        return "pages/congress/congressMainView";
    }

    /**
     * This controller display the list of congress
     *
     * @param model
     * @return Template of congress view list
     */
    @GetMapping
    public String getCongressList(Model model) {
        model.addAttribute("congressList", congressRepository.findAll());
        model.addAttribute("pageTitle", "List Congress");
        return "pages/congress/congressListView";
    }

    @PostMapping("/{id}")
    public String updateCongressWithPost(@PathVariable long id, @Valid @ModelAttribute Congress currentCongress, BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            return "redirect:/congress/" + id + "/edit";
        }
        return updateFileIfPresent(currentCongress);
    }

    @GetMapping("/{id}/delete")
    public String deleteCongressWithPost(@PathVariable long id) throws Exception {
        Optional<Congress> tofind = congressRepository.findById(id);
        if (tofind.isEmpty()) {
            throw new Exception("Can't find congress with id=" + id);
        }
        Congress currentCongress = tofind.get();
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
    @GetMapping("/{id}/edit")
    public String updateCongressForm(@PathVariable Long id, Model model) throws Exception {
        Congress newCongress;
        Optional<Congress> finded = congressRepository.findById(id);
        if (finded.isPresent()) {
            newCongress = finded.get();
        } else {
            throw new Exception("Can't find congress with id=" + id);
        }
        if (!model.containsAttribute("newCongress"))
            model.addAttribute("newCongress", newCongress);
        model.addAttribute("pathMethod", "/congress/" + id);
        model.addAttribute("pageTitle", "Update " + newCongress.getName());
        return "pages/congress/congressFormView";
    }

    private String updateFileIfPresent(Congress currentCongress) {
        if (!currentCongress.getLogo().isEmpty()) {
            storageService.store(currentCongress.getLogo());
            currentCongress.setLogo_url("/files/" + currentCongress.getLogo().getOriginalFilename());
        }
        if (!currentCongress.getBanner().isEmpty()) {
            storageService.store(currentCongress.getBanner());
            currentCongress.setBanner_url("/files/" + currentCongress.getBanner().getOriginalFilename());
        }
        currentCongress = congressRepository.save(currentCongress);
        return "redirect:/congress/" + currentCongress.getId();
    }
}
