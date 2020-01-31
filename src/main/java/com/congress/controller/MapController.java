package com.congress.controller;

import com.congress.entity.Congress;
import com.congress.entity.Map;
import com.congress.repository.MapRepository;
import com.congress.services.CongressService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

/**
 * voila
 */
@Controller
@RequestMapping("congress/{congressId}/map")
public class MapController {

    private final CongressService congressService;

    @Autowired
    private MapRepository mapRepository;

    public MapController(CongressService congressService) {
        this.congressService = congressService;
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
        Map currentMap = null;
        for (Map map : currentCongress.getMaps()) {
            if (map.getId() == id) {
                currentMap = map;
            }
        }
        if (currentMap == null) {
            throw new NotFoundException("Can't find map xith id : " + id);
        }
        model.addAttribute("currentCongress", congressService.findById(congressId));
        model.addAttribute("page", "maps");
        model.addAttribute("currentMap", currentMap);
        model.addAttribute("pageTitle", "Map" + currentMap.getTitle());

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
        mapRepository.save(currentMap);
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
    public String deleteMap(@PathVariable long congressId, @PathVariable long id, @ModelAttribute Map currentMap) throws NotFoundException, IOException {
        Congress currentCongress = congressService.findById(congressId);

        Map toDelete = mapRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find map With id : " + id));
        currentCongress.removeMap(toDelete);
        congressService.update(currentCongress);
        return "redirect:/";
    }

    /**
     * this controller is used to display the about update formulaire
     *
     * @param congressId
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}/edit")
    public String updateMapForm(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
        Map newMap;
        Optional<Map> finded = mapRepository.findById(id);
        if (finded.isPresent()) {
            newMap = finded.get();
        } else {
            throw new Exception("No links existing, please create a link");
        }
        if (!model.containsAttribute("newMap")) {
            model.addAttribute("newMap", newMap);
        }
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
    public String updateMapWithPost(@PathVariable long congressId, @PathVariable long id, @Valid @ModelAttribute Map currentMap, BindingResult bindingMap, Model model) throws NotFoundException, IOException {
        if (bindingMap.hasErrors()) {
            return "redirect:/congress/" + congressId + "/map/" + id + "/edit";
        }
        Congress currentCongress = congressService.findById(congressId);        currentCongress.addMap(currentMap);
        mapRepository.save(currentMap);
        congressService.update(currentCongress);
        return "redirect:/congress/" + congressId + "/map/" + currentMap.getId();

    }
}

