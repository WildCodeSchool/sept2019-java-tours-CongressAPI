package com.congress.controller;

import com.congress.entity.Congress;
import com.congress.entity.Map;
import com.congress.services.CongressService;
import com.congress.services.MapService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * voila
 */
@Controller
@RequestMapping("congress/{congressId}/map")
public class MapController {

    private final CongressService congressService;
    private final MapService mapService;

    public MapController(CongressService congressService, MapService mapService) {
        this.congressService = congressService;
        this.mapService = mapService;
    }

    /**
     * this controller disply list of Map containts
     *
     * @param congressId link map url and congress
     * @param model
     * @return template of map view list
     */
    @GetMapping
    public String getMap(@PathVariable long congressId, Model model) throws IOException {
        model.addAttribute("page", "map");
        model.addAttribute("currentCongress", congressService.findById(congressId));
        model.addAttribute("pageTitle", "Map");
        model.addAttribute("currentMap", new Map());
        model.addAttribute("pathMethod", "/congress/"+ congressId+ "/map/create");

        return "pages/map/mapListView";
    }

    /**
     * this controller display specific url in Map
     *
     * @param congressId
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}")
    public String getMap(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        Map currentMap = mapService.findById(id);
        model.addAttribute("currentCongress", congressService.findById(congressId));
        model.addAttribute("page", "map");
        model.addAttribute("currentMap", currentMap);
        model.addAttribute("pageTitle", "Map" + currentMap.getTitle());
        model.addAttribute("pathMethod", "/congress/"+ congressId+ "/map/" + id + "/edit");
        return "pages/map/mapMainView";
    }

    @PostMapping("/create")
    public String createMap(@PathVariable long congressId, @Valid @ModelAttribute("currentMap") Map currentMap, BindingResult bindingMap, Model model) throws NotFoundException, IOException {
        if (bindingMap.hasErrors()) {
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod", "/congress/" + congressId + "/map/create");
            model.addAttribute("currentMap", currentMap);
            return "pages/map/mapFormView";
        }
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addMap(currentMap);
        mapService.create(currentMap);
        congressService.update(currentCongress);
        return "redirect:/congress/" + congressId + "/map/" + currentMap.getId();
    }

    /**
     * this controller is used to delete one url and title form the map
     *
     * @param congressId
     * @param id         the id of map
     * @param currentMap the model of the page Map
     * @return
     * @throws NotFoundException
     */
    @GetMapping("/{id}/delete")
    public String deleteMap(@PathVariable long congressId, @PathVariable long id, @ModelAttribute Map currentMap) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        Map toDelete = mapService.findById(id);
        currentCongress.removeMap(toDelete);
        congressService.update(currentCongress);
        return "redirect:/";
    }

    /**
     * this controller is used to display the map update formulaire
     *
     * @param congressId
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}/edit")
    public String updateMapForm(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
        Map newMap = mapService.findById(id);
        model.addAttribute("newMap", newMap);
        model.addAttribute("pathMethod", "congress/" + congressId + "/map/" + id + "/edit");
        model.addAttribute("pageTitle", "Update" + newMap.getTitle());
        model.addAttribute("page", "map");
        model.addAttribute("currentCongress", congressService.findById(congressId));
        return "pages/map/mapFormView";
    }

    /**
     * this controller is used to
     *
     * @param congressId
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/create")
    public String createMapForm(@PathVariable long congressId, Model model) throws Exception {
        model.addAttribute("page", "map");
        model.addAttribute("currentCongress", congressService.findById(congressId));
        model.addAttribute("pathMethod", "/congress/" + congressId + "/about/create");
        model.addAttribute("newMap", new Map());
        return "pages/map/mapFormView";
    }

    @PostMapping("/{id}/edit")
    public String updateMapWithPost(@PathVariable long congressId, @PathVariable long id, @Valid @ModelAttribute Map currentMap, BindingResult bindingMap, Model model) throws Exception {
        if (bindingMap.hasErrors()) {
            return "redirect:/congress/" + congressId + "/map/" + id + "/edit";
        }
        Congress currentCongress = congressService.findById(congressId);
        //currentCongress.addMap(currentMap);
        mapService.update(currentMap);
       // congressService.update(currentCongress);
        return "redirect:/congress/" + congressId + "/map/" + currentMap.getId();

    }
}

