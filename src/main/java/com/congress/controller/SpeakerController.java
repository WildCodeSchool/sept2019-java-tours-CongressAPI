package com.congress.controller;

import com.congress.entity.Congress;
import com.congress.entity.Speaker;
import com.congress.services.CongressService;
import com.congress.services.SpeakerService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/speaker")
public class SpeakerController {

    private final SpeakerService speakerService;
    private final CongressService congressService;

    public SpeakerController(SpeakerService speakerService, CongressService congressService) {
        this.speakerService = speakerService;
        this.congressService = congressService;
    }

    /**
     * this controlleur display a list of speaker
     *
     * @param model
     * @return
     */
    @GetMapping
    public String getSpeakers(Model model) {
        model.addAttribute("congressList", congressService.findAll());
        model.addAttribute("speakerList", speakerService.findAll());
        model.addAttribute("page", "speaker");
        model.addAttribute("pageTitle", "List Speaker");
        model.addAttribute("newSpeaker", new Speaker());
        model.addAttribute("pathMethod", "/speaker/create");

        return "pages/speaker/speakerListView";
    }

    @GetMapping("/{id}")
    public String getSpeaker(@PathVariable long id, Model model) throws Exception {
        Speaker currentSpeaker = speakerService.findById(id);
        model.addAttribute("currentSpeaker", currentSpeaker);
        model.addAttribute("newSpeaker", currentSpeaker);
        model.addAttribute("congressList", congressService.findAll());
        model.addAttribute("page", "speaker");
        model.addAttribute("pageTitle", "Speaker" + currentSpeaker.getName());
        model.addAttribute("pathMethod", "/speaker/" + id + "/edit");
        return "pages/speaker/speakerMainView";

    }

    @GetMapping("/create")
    public String createSpeakerForm(Model model) {
        model.addAttribute("httpMethod", "PUT");
        model.addAttribute("page", "speaker");
        model.addAttribute("pathMethod", "/speaker/create");
        model.addAttribute("newSpeaker", new Speaker());
        return "pages/speaker/speakerFormView";
    }

    @PostMapping("/create")
    public String createSpeaker(@Valid @ModelAttribute("newSpeaker") Speaker currentSpeaker, BindingResult bindingResult, Model model) throws NotFoundException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod", "/speaker/create");
            model.addAttribute("newSpeakers", currentSpeaker);
            return "pages/speaker/speakerFormView";
        }
        speakerService.create(currentSpeaker);
        return "redirect:/speaker/" + currentSpeaker.getId();
    }

    @GetMapping("/{id}/delete")
    public String deleteSpeaker(@PathVariable long id, @ModelAttribute Speaker currentSpeaker) throws Exception {
        speakerService.delete(id);
        return "redirect:/";

    }

    @GetMapping("/{id}/edit")
    public String updateSpeakerForm(@PathVariable Long id, Model model) throws Exception {
        Speaker newSpeaker = speakerService.findById(id);
        model.addAttribute("page", "speaker");
        model.addAttribute("pageTitle", "Update" + newSpeaker.getName());
        model.addAttribute("pathMethod", "/speaker/" + id + "/edit");
        model.addAttribute("newSpeaker", newSpeaker);

        return "pages/speaker/speakerFormView";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable long id, @Valid @ModelAttribute("newSpeaker") Speaker newSpeaker, BindingResult bindingResult, Model model) throws Exception {
        Speaker currentSpeaker = speakerService.findById(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("currentSpeaker", currentSpeaker);
            model.addAttribute("newSpeaker", newSpeaker);
            model.addAttribute("congressList", congressService.findAll());
            model.addAttribute("page", "speaker");
            model.addAttribute("pageTitle", "Speaker" + currentSpeaker.getName());
            model.addAttribute("pathMethod", "/speaker/" + id + "/edit");
            return "pages/speaker/speakerMainView";
        }
        speakerService.update(newSpeaker);
        return "redirect:/speaker/" + id;
    }

    @PostMapping("/{id}/linktocongress")
    public String linkToCongress(@PathVariable long id, long congressId) throws Exception {
        speakerService.linkToCongress(congressId, id);
        return "redirect:/speaker/" + id;
    }

    @GetMapping("/{id}/unlinktocongress/{congressId}")
    public String unlinkToCongress(@PathVariable long id, @PathVariable long congressId) throws Exception {
        speakerService.unLinkToCongress(congressId, id);
        return "redirect:/speaker/" + id;
    }

    @PostMapping("/linktogether")
    public String linkTogether(Long speakerId, Long congressId) throws Exception {
        Congress congress = congressService.findById(congressId);
        Speaker speaker = speakerService.findById(congressId);
        speaker.addCongress(congress);
        speakerService.update(speaker);
        congressService.update(congress);
        return "redirect:/speaker/" + speakerId;
    }
}