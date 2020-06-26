package com.congress.controller;

import com.congress.entity.Activity;
import com.congress.entity.Congress;
import com.congress.entity.FloorPlan;
import com.congress.services.CongressService;
import com.congress.services.FloorPlanService;
import com.congress.storage.StorageService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("congress/{congressId}/floorPlan")
public class FloorPlanController {
    private final FloorPlanService floorPlanService;
    private final CongressService congressService;
    private final StorageService storageService;

    public FloorPlanController(FloorPlanService floorPlanService, CongressService congressService, StorageService storageService) {
        this.floorPlanService = floorPlanService;
        this.congressService = congressService;
        this.storageService = storageService;

    }

    @GetMapping
    public String getFloorPlan(@PathVariable long congressId, Model model) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        model.addAttribute("page", "floorPlan");
        model.addAttribute("currentCongress", currentCongress);
        model.addAttribute("pageTitle", "Floor Plan");
        model.addAttribute("pathMethod", "/congress/" + currentCongress.getId() + "/floorPlan/create");
        model.addAttribute("newFloorPlan", new FloorPlan());
        return "pages/floorPlan/floorPlanListView";
    }

    @GetMapping("/{id}")
    public String getFloorPlan(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        FloorPlan currentFloorPlan = floorPlanService.findById(id);
        model.addAttribute("currentCongress", currentCongress);
        model.addAttribute("page", "floorPlan");
        model.addAttribute("currentFloorPlan", currentFloorPlan);
        model.addAttribute("pageTitle", "Floor Plan" + currentFloorPlan.getTitle());
        model.addAttribute("pathMethod", "/congress/" + currentCongress.getId() + "/floorPlan/" + id + "/edit");
        model.addAttribute("newFloorPlan", currentFloorPlan);
        return "pages/floorPlan/floorPlanMainView";
    }

    @PostMapping("/create")
    public String createFloorPlan(@PathVariable long congressId, @Valid @ModelAttribute FloorPlan currentFloorPlan, BindingResult bindingResult, Model model) throws NotFoundException, IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod", "/congress/" + congressId + "/floorPlan/create");
            model.addAttribute("newFloorPlan", currentFloorPlan);
            return "pages/floorPlan/floorPlanFormView";
        }
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addFloorPlan(currentFloorPlan);
        floorPlanService.create(currentFloorPlan);
        congressService.update(currentCongress);
        return "redirect:/congress/" + congressId + "/floorPlan/" + currentFloorPlan.getId();
    }

    @GetMapping("/{id}/delete")
    public String deleteFloorPlan(@PathVariable long congressId, @PathVariable long id, @ModelAttribute FloorPlan currentFloorPlan) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        FloorPlan toDelete = floorPlanService.findById(id);
        currentCongress.removeFloorPlan(toDelete);
        congressService.update(currentCongress);
        floorPlanService.delete(id);
        return "redirect:/";
    }

    @PostMapping("/{id}/edit")
    public String updateFloorPlanForm(@PathVariable long congressId, @PathVariable Long id, @ModelAttribute FloorPlan floorPlan, BindingResult bindingResult, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            Congress currentCongress = congressService.findById(congressId);
            FloorPlan currentFloorPlan = floorPlanService.findById(id);
            model.addAttribute("currentCongress", currentCongress);
            model.addAttribute("page", "floorPlans");
            model.addAttribute("floorPlan", currentFloorPlan);
            model.addAttribute("pageTitle", "Floor Plan" + currentFloorPlan.getTitle());
            model.addAttribute("pathMethod", "/congress/" + currentCongress.getId() + "/floorPlan/edit");
            model.addAttribute("newFloorPlan", currentFloorPlan);
            return "pages/floorPlan/floorPlanMainView";
        }
        floorPlanService.update(floorPlan);
        return "redirect:/congress/" + congressId + "/floorPlan/" + id;
    }

    @GetMapping("/create")
    public String createFloorPlanForm(@PathVariable long congressId, Model model) throws Exception {
        model.addAttribute("page", "floorPlan");
        model.addAttribute("currentCongress", congressService.findById(congressId));
        model.addAttribute("pathMethod", "/congress/" + congressId + "/floorPlan/create");
        model.addAttribute("newFloorPlan", new FloorPlan());
        return "pages/floorPlan/floorPlanFormView";
    }

}
