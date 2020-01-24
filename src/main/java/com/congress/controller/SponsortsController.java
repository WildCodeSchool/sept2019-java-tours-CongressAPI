package com.congress.controller;


import com.congress.entity.Congress;
import com.congress.entity.Sponsorts;
import com.congress.repository.CongressRepository;
import com.congress.repository.SponsortsRepository;
import com.congress.storage.StorageService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("sponsorts")
public class SponsortsController {

    @Autowired
    private CongressRepository congressRepository;

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

    @GetMapping
    public String getSponsors(Model model) {
        model.addAttribute("sponsortsList", sponsortsRepository.findAll());

        model.addAttribute("page", "sponsort");
        model.addAttribute("pageTitle", "List Sponsorts");
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
        Optional<Sponsorts> finded = sponsortsRepository.findById(id);
        Sponsorts currentSponsorts = finded.get();
        if (currentSponsorts == null) {
            throw new NotFoundException("Can't find sponsorts with : " + id);
        }
        model.addAttribute("currentSponsorts", currentSponsorts);
        model.addAttribute("congressList", congressRepository.findAll());
        model.addAttribute("page", "sponsort");
        model.addAttribute("pageTitle", "Sponsorts" + currentSponsorts.getName());
        return "pages/sponsorts/sponsortsMainView";

    }

    /**
     * this controller is used to create a new sponsor
     *
     * @param model this id is used for update Sponsors
     * @return redirect to Sponsors view
     */
    @PostMapping("/create")
    public String createSponsorts(@Valid @ModelAttribute("newSponsorts") Sponsorts currentSponsorts, BindingResult bindingSponsors, Model model) throws NotFoundException {
        if (bindingSponsors.hasErrors()) {
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod", "/sponsorts/create");
            model.addAttribute("newSponsors", currentSponsorts);
            return "pages/sponsorts/sponsortsFormView";
        }
        sponsortsRepository.save(currentSponsorts);
        return "redirect:/sponsorts/" + currentSponsorts.getId();
    }


    /**
     * this controller is used to delete a sponsors
     *
     * @param id               the id of sponsors
     * @param currentSponsorts the model of the page sponsors
     * @return redirect to the home page
     */
    @GetMapping("/{id}/delete")
    public String deleteSponsorts(@PathVariable long id, @ModelAttribute Sponsorts currentSponsorts) throws NotFoundException {
        Sponsorts toDelete = sponsortsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find sponsorts with id: " + id));
        sponsortsRepository.delete(toDelete);
        return "redirect:/";
    }

    /**
     * @param congressId
     * @param id
     * @param model
     * @return
     * @throws Exception
     */

    @GetMapping("/{id}/edit")
    public String updateSponsortsForm(@PathVariable Long id, Model model) throws Exception {
        Sponsorts newSponsorts;
        Optional<Sponsorts> finded = sponsortsRepository.findById(id);
        if (finded.isPresent()) {
            newSponsorts = finded.get();
        } else {
            throw new Exception("filed are empty, please add some new element or return to home");
        }
        if (!model.containsAttribute("newSponsorts")) {
            model.addAttribute("newSponsorts", newSponsorts);
        }
        model.addAttribute("page", "sponsort");
        model.addAttribute("pathMethod", "/sponsorts/" + id + "/edit");
        model.addAttribute("pageTitle", "Update " + newSponsorts.getName());

        return "pages/sponsorts/sponsortsFormView";
    }

    @GetMapping("/create")
    public String createSponsortsForm(Model model) throws Exception {
        model.addAttribute("httpMethod", "PUT");
        model.addAttribute("page", "sponsort");
        model.addAttribute("pathMethod", "/sponsorts/create");
        model.addAttribute("newSponsorts", new Sponsorts());
        return "pages/sponsorts/sponsortsFormView";
    }

    @PostMapping("/{id}/linktocongress")
    public String linkToCongress(@PathVariable long id, Long congressId) throws Exception {
        Congress cong = congressRepository.findById(congressId).get();
        Sponsorts spon = sponsortsRepository.findById(id).get();
        spon.addCongress(cong);
        sponsortsRepository.save(spon);
        congressRepository.save(cong);
        return "redirect:/sponsorts/" + id;
    }

    @PostMapping("/{id}/unlinktocongress")
    public String unlinkToCongress(@PathVariable long id, Long congressId) throws Exception {
        Congress cong = congressRepository.findById(congressId).get();
        Sponsorts spon = sponsortsRepository.findById(id).get();
        spon.removeCongress(cong);
        sponsortsRepository.save(spon);
        congressRepository.save(cong);

        return "redirect:/sponsorts/" + id;
    }
}

