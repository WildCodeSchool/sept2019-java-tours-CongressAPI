package com.congress.controller;

import com.congress.entity.Congress;
import com.congress.entity.Hotel;
import com.congress.services.CongressService;
import com.congress.services.HotelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    private final HotelService hotelService;
    private final CongressService congressService;

    public HotelController(HotelService hotelService, CongressService congressService) {
        this.hotelService = hotelService;
        this.congressService = congressService;
    }

    @GetMapping
    public String getAll(Model model) throws Exception {
        model.addAttribute("page", "hotel");
        model.addAttribute("pathMethod", "/hotel/create");
        model.addAttribute("newHotel", new Hotel());
        model.addAttribute("congressList", congressService.findAll());
        model.addAttribute("hotelList", hotelService.findAll());
        return "pages/hotel/hotelListView";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable long id, Model model) throws Exception {
        Hotel currentHotel = hotelService.findById(id);
        model.addAttribute("page", "hotel");
        model.addAttribute("pathMethod", "/hotel/" + currentHotel.getId() + "/edit");
        model.addAttribute("newHotel", currentHotel);
        model.addAttribute("currentHotel", currentHotel);
        model.addAttribute("congressList", congressService.findAll());
        return "pages/hotel/hotelMainView";
    }

    @PostMapping("/{id}/edit")
    public String update(@ModelAttribute Hotel newHotel, Model model) throws Exception {
        newHotel = hotelService.update(newHotel);
        return "redirect:/hotel/" + newHotel.getId();
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Hotel newHotel, Model model) throws Exception {
        newHotel = hotelService.create(newHotel);
        return "redirect:/hotel/" + newHotel.getId();
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
        hotelService.delete(id);
        return "redirect:/hotel";
    }

    @PostMapping("/{id}/linktocongress")
    public String linkToCongress(@PathVariable long id, Long congressId) throws Exception {
        hotelService.linkToCongress(congressId, id);
        return "redirect:/hotel/" + id;
    }

    @GetMapping("/{id}/unlinktocongress/{congressId}")
    public String unlinkToCongress(@PathVariable Long id, @PathVariable Long congressId) throws Exception {
        hotelService.unLinkToCongress(congressId, id);
        return "redirect:/hotel/" + id;
    }

    @PostMapping("/linktogether")
    public String linkTogether(Long sponsorId, Long congressId) throws Exception {
        Congress congress = congressService.findById(congressId);
        Hotel sponsor = hotelService.findById(sponsorId);
        sponsor.addCongress(congress);
        hotelService.update(sponsor);
        congressService.update(congress);
        return "redirect:/hotel/" + sponsorId;
    }
}