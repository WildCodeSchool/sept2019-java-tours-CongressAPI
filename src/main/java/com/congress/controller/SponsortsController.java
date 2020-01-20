package com.congress.controller;


import com.congress.entity.Sponsorts;
import com.congress.repository.SponsortsRepository;
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
@RequestMapping("congress/{congressId}/")
public class SponsortsController {

    @Autowired
    private SponsortsRepository sponsortsRepository;

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

    @GetMapping("/sponsorts")
    public String getSponsors(@PathVariable long congressId, Model model){
        model.addAttribute("sponsortsList", sponsortsRepository.findAll());
        model.addAttribute("pageTitle", "List Sponsorts");
        return"pages/sponsorts/sponsort sListView";

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
    @GetMapping("/sponsorts/{id}")
    public String getSponsorts(@PathVariable Long congressId, @PathVariable long id, Model model)throws Exception {
        Optional<Sponsorts> finded = sponsortsRepository.findById(id);
        Sponsorts currentSponsorts = finded.get();
        if (finded.isPresent()){
            currentSponsorts = finded.get();
        }else {
            throw new Exception("Filed empty, please write a comment");
        }
        model.addAttribute("sponsorts",currentSponsorts);
        model.addAttribute("pageTitle","Sponsorts" + currentSponsorts.getName());
        return "pages/sponsorts/sponsortsMainView";

    }

    /**
     * this controller is used to create a new sponsor
     *
     * @param congressId     this id is used to connect with the current conress
     * @param model          this id is used for update Sponsors
     * @return               redirect to Sponsors view
     */
    @PostMapping("/sponsorts")
    public String createSponsorts(@PathVariable long congressId, @Valid @ModelAttribute("newSponsorts") Sponsorts currentSponsorts, BindingResult bindingSponsors, Model model) {
        if(bindingSponsors.hasErrors()){
            model.addAttribute("httpMethod","POST");
            model.addAttribute("pathMethod", "/sponsorts");
            model.addAttribute("newSponsors", currentSponsorts);
            return "pages/sponsorts/sponsortsFormView";
        }
        currentSponsorts = sponsortsRepository.save(currentSponsorts);
        return "redirect:/congress/"+congressId+"/sponsorts/"+ currentSponsorts.getId();
    }

    /**
     * this is a controller to update Sponsors
     *
     * @param id              the id of update Sponsors
     * @param currentSponsorts the model of table sponsors
     * @return                 Redirect to sponsors view
     */
    @PutMapping("/sponsorts/{id}")
        public String updateSponsorts(@PathVariable long congressId, @PathVariable long id, @Valid @ModelAttribute Sponsorts currentSponsorts, BindingResult bindingSponsorts, Model model, RedirectAttributes redirectAttributes){
            if(bindingSponsorts.hasErrors()){
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.currentSponsorts", "PUT");
                redirectAttributes.addFlashAttribute("httpMethod","PUT");
                redirectAttributes.addFlashAttribute("newSponsors", currentSponsorts);
                return "redirct:/sponsorts"+ id + "edit";
            }
            currentSponsorts = sponsortsRepository.save(currentSponsorts);
            return "redirct:/sponsorts"+ id + currentSponsorts.getId();
        }

    /**
     * this controller is used to delete a sponsors
     *
     * @param id                the id of sponsors
     * @param currentSponsorts   the model of the page sponsors
     * @return                  redirect to the home page
     */
    @DeleteMapping("/sponsorts/{id}")
    public String deleteSponsorts(@PathVariable long congressId, @PathVariable long id, @ModelAttribute Sponsorts currentSponsorts){
        sponsortsRepository.delete(currentSponsorts);
        return "redirect:/";
    }
    @GetMapping("/sponsorts/{id}/edit")
    public String updateSponsortsForm(@PathVariable long congressId, @PathVariable Long id, Model model) throws Exception{
        Sponsorts newSponsorts;
        Optional<Sponsorts> finded = sponsortsRepository.findById(id);
        if (finded.isPresent()){
            newSponsorts = finded.get();
        } else {
            throw new Exception("filed are empty, please add some new element or return to home");
        }
        if (!model.containsAttribute("newSponsorts")){
            model.addAttribute("newSponsorts", newSponsorts);
        }
        model.addAttribute("httpMethod","PUT");
        model.addAttribute("pathMethod","/about/" + id);
        model.addAttribute("pageTitle", "Update " + newSponsorts.getName());

        return "pages/sponsorts/sponsortsFormView";
    }
    @GetMapping("/sponsorts/create")
    public String createSponsortsForm(@PathVariable long congressId, Model model) throws Exception{
        model.addAttribute("httpMethod", "POST");
        model.addAttribute("pathMethod","/congress/" + congressId +"/sponsorts");
        model.addAttribute("newSponsorts", new Sponsorts());
        return "pages/sponsorts/sponsortsFormView";
    }

}

