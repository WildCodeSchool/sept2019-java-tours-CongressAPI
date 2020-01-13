package com.congress.controller;

import com.congress.repository.AboutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/about")
public class AboutController {

    @Autowired
    private AboutRepository aboutRepository;


    /**
     * *this controller display the list of About cotaints
     *
     * @param model
     * @return Template of about view list
     */
    @GetMapping
    public String getAbout(Model model){
        model.addAttribute("aboutList", aboutRepository.findAll());
        model.addAttribute("pageTitle", "About");
        return "/pages/about/aboutListView";
    }
}
