package com.congress.controller;

import com.congress.entity.Activity;
import com.congress.entity.Speaker;
import com.congress.services.ActivityService;
import com.congress.services.CongressService;
import com.congress.services.SpeakerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("congress/{congressId}/activity")
public class ActivityController {
    private final CongressService congressService;
    private final ActivityService activityService;
    private final SpeakerService speakerService;

    public ActivityController(CongressService congressService, ActivityService activityService, SpeakerService speakerService) {
        this.congressService = congressService;
        this.activityService = activityService;
        this.speakerService = speakerService;
    }

    @GetMapping
    public String getAll(@PathVariable long congressId, Model model) throws IOException {
        model.addAttribute("currentCongress", congressService.findById(congressId));
        model.addAttribute("newActivity", new Activity());
        return "/pages/activity/activityListView";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable long congressId, @PathVariable long id, Model model) throws IOException {
        model.addAttribute("currentCongress", congressService.findById(congressId));
        model.addAttribute("currentActivity", activityService.findById(id));
        model.addAttribute("newActivity", new Activity());
        return "/pages/activity/activityListView";
    }

    @PostMapping("/create")
    public String create(@PathVariable long congressId, @ModelAttribute Activity newActivity) throws IOException {
        newActivity.setCongress(congressService.findById(congressId));

        newActivity = activityService.create(newActivity);
        return "redirect:/congress/" + congressId + "/activity/" + newActivity.getId();
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable long congressId, @ModelAttribute Activity newActivity) throws Exception {
        newActivity = activityService.update(newActivity);
        return "redirect:/congress/" + congressId + "/activity/" + newActivity.getId();
    }

    public String link(long speakerId, long activityId) throws Exception {
        Activity activity = activityService.findById(activityId);
        Speaker speaker = speakerService.findById(speakerId);
        speaker.addActivity(activity);
        return "";
    }
}
