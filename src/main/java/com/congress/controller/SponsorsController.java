package com.congress.controller;


import com.congress.entity.Sponsors;
import com.congress.repository.SponsorsRepository;
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
@RequestMapping("/sponsors")
public class SponsorsController {

    @Autowired
    private SponsorsRepository sponsorsRepository;

    private StorageService storageService;

    @Autowired
    public void FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }
    /*
        crud sponsors
     */
    /**
     * this controller display a list of sponsors
     *
     * @param model
     * @return Template of sponsorts view list
     */

    @GetMapping("/congress/{congressId}/sponsors/")
    public String getSponsors(@PathVariable long congressId, Model model){
        model.addAttribute("sponsorsList", sponsorsRepository.findAll());
        model.addAttribute("pageTitle", "List Sponsors");
        return"pages/sponsors/sponsorsListView";

    }

    /**
     * this controller display specific description in sponsors
     *
     * @param congressId
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}")
    public String getSponsors(@PathVariable Long congressId, @PathVariable long id, Model model)throws Exception {
        Optional<Sponsors> finded = sponsorsRepository.findById(id);
        Sponsors currentSponsors = finded.get();
        if (finded.isPresent()){
            currentSponsors = finded.get();
        }else {
            throw new Exception("Filed empty, please write a comment");
        }
        model.addAttribute("sponsors",currentSponsors);
        model.addAttribute("pageTitle","Sponsorts" + currentSponsors.getName());
        return "pages/sponsors/sponsorsMainView";

    }

    /**
     * this controller is used to create a new sponsor
     *
     * @param congressId     this id is used to connect with the current conress
     * @param model          this id is used for update Sponsors
     * @return               redirect to Sponsors view
     */
    @PostMapping("/congress/{congressId}/sponsors/")
    public String createSponsors(@PathVariable long congressId, @Valid @ModelAttribute Sponsors currentSponsors, BindingResult bindingSponsors, Model model) {
        if(bindingSponsors.hasErrors()){
            model.addAttribute("httpMethod","POST");
            model.addAttribute("pathMethod", "/sponsors");
            model.addAttribute("newSponsors", currentSponsors);
            return "pages/sponsors/sponsorsFormView";
        }
        currentSponsors = sponsorsRepository.save(currentSponsors);
        return "redirect:/congress/"+congressId+"/sponsors/"+ currentSponsors.getId();
    }

    /**
     * this is a controlleur to update Sponsors
     *
     * @param id              the id of update Sponsors
     * @param currentSponsors the model of table sponsors
     * @return                 Redirect to sponsors view
     */
    @PutMapping("/{id}")
        public String updateSponsors(@PathVariable long congressId,@PathVariable long id,@Valid @ModelAttribute Sponsors currentSponsors, BindingResult bindingSponsors, Model model, RedirectAttributes redirectAttributes){
            if(bindingSponsors.hasErrors()){
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.currentSponsors", "PUT");
                redirectAttributes.addFlashAttribute("httpMethod","PUT");
                redirectAttributes.addFlashAttribute("newSponsors", currentSponsors);
                return "redirct:/sponsors"+ id + "edit";
            }
            currentSponsors = sponsorsRepository.save(currentSponsors);
            return "redirct:/sponsors"+ id + currentSponsors.getId();
        }

    /**
     * this controlleur is used to dellete a sponsors
     *
     * @param id                the id of sponsors
     * @param currentSponsors   the model of the page sponsors
     * @return                  redirect to the home page
     */
    @DeleteMapping("/{id}")
    public String deleteSponsors(@PathVariable long congressId, @PathVariable long id, @ModelAttribute Sponsors currentSponsors){
        sponsorsRepository.delete(currentSponsors);
        return "redirect:/";
    }
    @GetMapping("/{id}/edit")
    public String updateSponsorsForm(@PathVariable long congressId, @PathVariable Long id, Model model) throws Exception{
        Sponsors newSponsors;
        Optional<Sponsors> finded = sponsorsRepository.findById(id);
        if (finded.isPresent()){
            newSponsors = finded.get();
        } else {
            throw new Exception("filed are empty, please add some new element or return to home");
        }
        if (!model.containsAttribute("newSponsors")){
            model.addAttribute("newSponsors", newSponsors);
        }
        model.addAttribute("httpMethod","PUT");
        model.addAttribute("pathMethod","/about/" + id);
        model.addAttribute("pageTitle", "Update " + newSponsors.getName());

        return "pages/sponsors/sponsorsFormView";
    }
    @GetMapping("/create")
    public String createSponsorsForm(@PathVariable long congressId, Model model) throws Exception{
        model.addAttribute("httpMethod", "POST");
        model.addAttribute("pathMethod","/congress/" + congressId +"/sponsors");
        model.addAttribute("newSponsors", new Sponsors());
        return "pages/sponsors/sponsorsFormView";
    }

}

