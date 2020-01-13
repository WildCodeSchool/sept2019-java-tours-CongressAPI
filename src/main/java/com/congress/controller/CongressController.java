package com.congress.controller;

import com.congress.entity.Congress;
import com.congress.repository.CongressRepository;
import com.congress.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
     * This controller display the list of congress
     *
     * @param model
     * @return Template of congress view list
     */
    @GetMapping
    public String getCongress(Model model) {
        model.addAttribute("congressList", congressRepository.findAll());
        model.addAttribute("pageTitle", "List Congress");
        return "pages/congress/congressListView";
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
     * This controller is used to create a congress
     *
     * @param currentCongress The model of the congress
     * @param model
     * @return Redirect to the congress view
     */
    @PostMapping("/")
    public String createCongress(@Valid @ModelAttribute Congress currentCongress, BindingResult binding, Model model) {
        if(binding.hasErrors()){
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod", "/congress");
            model.addAttribute("newCongress", currentCongress);
            return "pages/congress/congressFormView";
        }
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
    @PutMapping("/{id}")
    public String updateCongress(@PathVariable long id, @Valid @ModelAttribute Congress currentCongress, BindingResult binding, Model model, RedirectAttributes redirectAttributes) {
        if(binding.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.currentCongress", "PUT");
            redirectAttributes.addFlashAttribute("httpMethod", "PUT");
            redirectAttributes.addFlashAttribute("pathMethod", "/congress/" +id);
            redirectAttributes.addFlashAttribute("newCongress", currentCongress);
            return "redirect:/congress/" + id + "edit";
        }
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
    @DeleteMapping("/{id}")
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
        model.addAttribute("httpMethod", "PUT");
        model.addAttribute("pathMethod", "/congress/" + id);
        model.addAttribute("pageTitle", "Update " + newCongress.getName());

        return "pages/congress/congressFormView";
    }

    /**
     * This controller is used to display the congress creation form
     *
     * @param model
     * @return Template of congress creation view form
     * @throws Exception
     */
    @GetMapping("/create")
    public String createCongressForm(Model model) throws Exception {
        model.addAttribute("httpMethod", "POST");
        model.addAttribute("pathMethod", "/congress");
        model.addAttribute("newCongress", new Congress());
        return "pages/congress/congressFormView";
    }

}
