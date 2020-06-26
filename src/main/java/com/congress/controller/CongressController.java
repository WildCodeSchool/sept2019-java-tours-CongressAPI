package com.congress.controller;

import com.congress.entity.About;
import com.congress.entity.Congress;
import com.congress.entity.SocialLink;
import com.congress.services.CongressService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/congress")
public class CongressController {

    private final CongressService service;

    public CongressController(CongressService service) {
        this.service = service;
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
        model.addAttribute("page", "congressListPage");
        model.addAttribute("pathMethod", "/congress/create");
        model.addAttribute("newCongress", new Congress());
        return "pages/congress/congressFormView";
    }

    /**
     * This controller is used to create a congress
     *
     * @param newCongress The model of the congress
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
        newCongress = service.create(newCongress);
        return "redirect:/congress/" + newCongress.getId();
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
        Congress currentCongress = service.findById(id);
        model.addAttribute("page", "congress");
        model.addAttribute("currentCongress", currentCongress);
        model.addAttribute("pageTitle", "Congress" + currentCongress.getName());
        model.addAttribute("newSocialLink", new SocialLink());
        model.addAttribute("newAbout", new About());
        model.addAttribute("pathMethod", "/congress/" + id + "/socialLink/create");

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
        model.addAttribute("page", "congressListPage");
        model.addAttribute("congressList", service.findAll());
        model.addAttribute("pageTitle", "List Congress");
        return "pages/congress/congressListView";
    }

    @PostMapping("/{id}")
    public String updateCongressWithPost(@PathVariable long id, @Valid @ModelAttribute Congress currentCongress, BindingResult binding, Model model) throws IOException {
        if (binding.hasErrors()) {
            return "redirect:/congress/" + id + "/edit";
        }
        Congress previous = service.findById(id);
        currentCongress.setMaps(previous.getMaps());
        currentCongress.setFloorPlans(previous.getFloorPlans());
        currentCongress.setSocialLinks(previous.getSocialLinks());
        currentCongress.setAbouts(previous.getAbouts());
        currentCongress.setSocialLinks(previous.getSocialLinks());
        service.update(currentCongress);
        return "redirect:/congress/" + currentCongress.getId();
    }

    @GetMapping("/{id}/delete")
    public String deleteCongressWithPost(@PathVariable long id) throws Exception {
        service.delete(id);
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
        Congress newCongress = service.findById(id);
        model.addAttribute("newCongress", newCongress);
        model.addAttribute("page", "congress");
        model.addAttribute("pathMethod", "/congress/" + id);
        model.addAttribute("pageTitle", "Update " + newCongress.getName());
        return "pages/congress/congressFormView";
    }
}
