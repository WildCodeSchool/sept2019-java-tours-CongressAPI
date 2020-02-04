package com.congress.controller;


import com.congress.entity.Congress;
import com.congress.entity.Sponsor;
import com.congress.services.CongressService;
import com.congress.services.SponsorService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/sponsor")
public class SponsorController {

    private final SponsorService sponsorService;
    private final CongressService congressService;

    public SponsorController(SponsorService sponsorService, CongressService congressService) {
        this.sponsorService = sponsorService;
        this.congressService = congressService;
    }
    /*
        crud sponsors
     */

    /**
     * this controller display a list of sponsors
     *
     * @param model
     * @return Template of sponsor view list
     */

    @GetMapping
    public String getSponsors(Model model) {
        model.addAttribute("congressList", congressService.findAll());
        model.addAttribute("sponsorList", sponsorService.findAll());
        model.addAttribute("page", "sponsor");
        model.addAttribute("pageTitle", "List Sponsor");
        return "pages/sponsor/sponsorListView";

    }

    /**
     * this controller display specific description in sponsors
     *
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}")
    public String getSponsor(@PathVariable long id, Model model) throws Exception {
        Sponsor currentSponsor = sponsorService.findById(id);
        model.addAttribute("currentSponsor", currentSponsor);
        model.addAttribute("newSponsor", currentSponsor);
        model.addAttribute("congressList", congressService.findAll());
        model.addAttribute("page", "sponsor");
        model.addAttribute("pageTitle", "Sponsor" + currentSponsor.getName());
        model.addAttribute("pathMethod", "/sponsor/" + id + "/edit");
        return "pages/sponsor/sponsorMainView";

    }

    /**
     * this controller is used to create a new sponsor
     *
     * @param model this id is used for update Sponsors
     * @return redirect to Sponsors view
     */
    @PostMapping("/create")
    public String createSponsor(@Valid @ModelAttribute("newSponsor") Sponsor currentSponsor, BindingResult bindingSponsors, Model model) throws NotFoundException {
        if (bindingSponsors.hasErrors()) {
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod", "/sponsor/create");
            model.addAttribute("newSponsors", currentSponsor);
            return "pages/sponsor/sponsorFormView";
        }
        sponsorService.create(currentSponsor);
        return "redirect:/sponsor/" + currentSponsor.getId();
    }

    @GetMapping("/{id}/delete")
    public String deleteSponsor(@PathVariable long id, @ModelAttribute Sponsor currentSponsor) throws Exception {
        sponsorService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String updateSponsorForm(@PathVariable Long id, Model model) throws Exception {
        Sponsor newSponsor = sponsorService.findById(id);
        model.addAttribute("page", "sponsor");
        model.addAttribute("pathMethod", "/sponsor/" + id + "/edit");
        model.addAttribute("pageTitle", "Update " + newSponsor.getName());
        model.addAttribute("newSponsor", newSponsor);

        return "pages/sponsor/sponsorFormView";
    }
    @PostMapping("/{id}/edit")
    public String update(@PathVariable long id,@Valid @ModelAttribute("newSponsor") Sponsor newSponsor,BindingResult bindingResult,  Model model) throws Exception {
        Sponsor currentSponsor = sponsorService.findById(id);
        if(bindingResult.hasErrors()){
            model.addAttribute("currentSponsor", currentSponsor);
            model.addAttribute("newSponsor", newSponsor);
            model.addAttribute("congressList", congressService.findAll());
            model.addAttribute("page", "sponsor");
            model.addAttribute("pageTitle", "Sponsor " + currentSponsor.getName());
            model.addAttribute("pathMethod", "/sponsor/" + id + "/edit");
            return "pages/sponsor/sponsorMainView";
        }
        sponsorService.update(newSponsor);
        return "redirect:/sponsor/" + id;
    }

    @GetMapping("/create")
    public String createSponsorForm(Model model) throws Exception {
        model.addAttribute("httpMethod", "PUT");
        model.addAttribute("page", "sponsor");
        model.addAttribute("pathMethod", "/sponsor/create");
        model.addAttribute("newSponsor", new Sponsor());
        return "pages/sponsor/sponsorFormView";
    }

    @PostMapping("/{id}/linktocongress")
    public String linkToCongress(@PathVariable long id, Long congressId) throws Exception {
        sponsorService.linkToCongress(congressId, id);
        return "redirect:/sponsor/" + id;
    }

    @GetMapping("/{id}/unlinktocongress/{congressId}")
    public String unlinkToCongress(@PathVariable Long id, @PathVariable Long congressId) throws Exception {
        sponsorService.unLinkToCongress(congressId, id);
        return "redirect:/sponsor/" + id;
    }

    @PostMapping("/linktogether")
    public String linkTogether(Long sponsorId, Long congressId) throws Exception {
        Congress congress = congressService.findById(congressId);
        Sponsor sponsor = sponsorService.findById(sponsorId);
        sponsor.addCongress(congress);
        sponsorService.update(sponsor);
        congressService.update(congress);
        return "redirect:/sponsor/" + sponsorId;
    }
}

