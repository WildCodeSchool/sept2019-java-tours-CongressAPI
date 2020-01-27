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
@RequestMapping("sponsor")
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
     * @return Template of sponsorts view list
     */

    @GetMapping
    public String getSponsors(Model model) {
        model.addAttribute("sponsortsList", sponsorService.findAll());
        model.addAttribute("page", "sponsort");
        model.addAttribute("pageTitle", "List Sponsor");
        return "pages/sponsorts/sponsortsListView";

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
    public String getSponsorts(@PathVariable long id, Model model) throws Exception {
        Sponsor currentSponsor = sponsorService.findById(id);
        model.addAttribute("currentSponsorts", currentSponsor);
        model.addAttribute("congressList", sponsorService.findAll());
        model.addAttribute("page", "sponsort");
        model.addAttribute("pageTitle", "Sponsor" + currentSponsor.getName());
        return "pages/sponsorts/sponsortsMainView";

    }

    /**
     * this controller is used to create a new sponsor
     *
     * @param model this id is used for update Sponsors
     * @return redirect to Sponsors view
     */
    @PostMapping("/create")
    public String createSponsor(@Valid @ModelAttribute("newSponsorts") Sponsor currentSponsor, BindingResult bindingSponsors, Model model) throws NotFoundException {
        if (bindingSponsors.hasErrors()) {
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod", "/sponsorts/create");
            model.addAttribute("newSponsors", currentSponsor);
            return "pages/sponsorts/sponsortsFormView";
        }
        sponsorService.create(currentSponsor);
        return "redirect:/sponsorts/" + currentSponsor.getId();
    }

    @GetMapping("/{id}/delete")
    public String deleteSponsorts(@PathVariable long id, @ModelAttribute Sponsor currentSponsor) throws Exception {
        sponsorService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String updateSponsortsForm(@PathVariable Long id, Model model) throws Exception {
        Sponsor newSponsor = sponsorService.findById(id);
        model.addAttribute("page", "sponsort");
        model.addAttribute("pathMethod", "/sponsorts/" + id + "/edit");
        model.addAttribute("pageTitle", "Update " + newSponsor.getName());

        return "pages/sponsorts/sponsortsFormView";
    }

    @GetMapping("/create")
    public String createSponsortsForm(Model model) throws Exception {
        model.addAttribute("httpMethod", "PUT");
        model.addAttribute("page", "sponsort");
        model.addAttribute("pathMethod", "/sponsorts/create");
        model.addAttribute("newSponsorts", new Sponsor());
        return "pages/sponsorts/sponsortsFormView";
    }

    @PostMapping("/{id}/linktocongress")
    public String linkToCongress(@PathVariable long id, Long congressId) throws Exception {
        Congress congress = congressService.findById(congressId);
        Sponsor sponsor = sponsorService.findById(id);
        sponsor.addCongress(congress);
        sponsorService.update(sponsor);
        congressService.update(congress);
        return "redirect:/sponsorts/" + id;
    }

    @PostMapping("/{id}/unlinktocongress")
    public String unlinkToCongress(@PathVariable long id, Long congressId) throws Exception {
        Congress congress = congressService.findById(congressId);
        Sponsor sponsor = sponsorService.findById(id);
        sponsor.removeCongress(congress);
        sponsorService.update(sponsor);
        congressService.update(congress);
        return "redirect:/sponsorts/" + id;
    }
}

