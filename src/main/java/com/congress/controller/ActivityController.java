package com.congress.controller;

import com.congress.entity.Activity;
import com.congress.entity.Congress;
import com.congress.entity.Map;
import com.congress.entity.Speaker;
import com.congress.services.ActivityService;
import com.congress.services.CongressService;
import com.congress.services.SpeakerService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        Congress currentCongress = congressService.findById(congressId);
        model.addAttribute("currentCongress", currentCongress);
        model.addAttribute("newActivity", new Activity());
        model.addAttribute("pathMethod", "/congress/" + currentCongress.getId() + "/activity/create");
        return "pages/activity/activityListView";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable long congressId, @PathVariable long id, Model model) throws IOException{
        Congress currentCongress = congressService.findById(congressId);
        Activity currentActivity = activityService.findById(id);
        model.addAttribute("currentActivity", currentActivity);
        model.addAttribute("currentCongress", currentCongress);
        model.addAttribute("newActivity", currentActivity);
        model.addAttribute("pathMethod", "/congress/" + currentCongress.getId() + "/activity/" + id + "/edit");
        return "pages/activity/activityMainView";
    }
    @GetMapping("/create")
    public String createActivityForm(@PathVariable long congressId, Model model) throws Exception{
        model.addAttribute("currentCongress",congressService.findById(congressId));
        model.addAttribute("newActivity", new Activity());
        model.addAttribute("pathMethod", "/congress/" + congressId + "/activity/create");
        return "pages/activity/activityFormView";
    }

    @PostMapping("/create")
    public String create(@PathVariable long congressId, @Valid @ModelAttribute Activity currentActivity, BindingResult bindingResult, Model model) throws NotFoundException, IOException {
        if (bindingResult.hasErrors()){
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod","/congress/" + congressId + "/activity/create");
            model.addAttribute("newActivity", currentActivity);
            return "pages/activity/activityFormView";
        }
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addActivity(currentActivity);
        currentActivity = activityService.create(currentActivity);
        congressService.update(currentCongress);
        return "redirect:/congress/" + congressId + "/activity/" + currentActivity.getId();
    }
    @GetMapping("/{id}/delete")
    public String deleteActivity(@PathVariable long congressId, @PathVariable long id,@ModelAttribute Activity currentActivity)throws Exception{
        Congress currentCongress = congressService.findById(congressId);
        Activity toDelete = activityService.findById(id);
        currentCongress.removeActivity(toDelete);
        congressService.update(currentCongress);
        activityService.delete(id);
        return "redirect:/congress/{congressId}/activity";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable long congressId, @PathVariable long id, @Valid @ModelAttribute Activity activity, BindingResult bindingResult, Model model) throws Exception{
        if(bindingResult.hasErrors()){
            Congress currentCongress = congressService.findById(congressId);
            Activity currentActivity = activityService.findById(id);
            model.addAttribute("newActivity",new Activity());
            model.addAttribute("currentCongress", currentCongress);
            model.addAttribute("activity", currentActivity);
            model.addAttribute("newActivity", currentActivity);
            model.addAttribute("pathMethod", "/congress/" + currentCongress.getId() + "/activity/edit");
            return "pages/activity/activityMainView";
        }

        activityService.update(id, activity);
        return "redirect:/congress/" + congressId + "/activity/" +id;
    }
    @GetMapping("/{id}/link")
    public String link(long speakerId, long activityId) throws Exception {
        Activity activity = activityService.findById(activityId);
        Speaker speaker = speakerService.findById(speakerId);
        speaker.addActivity(activity);
        return "";
    }
}
