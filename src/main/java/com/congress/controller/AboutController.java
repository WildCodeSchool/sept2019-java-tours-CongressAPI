package com.congress.controller;

import com.congress.entity.About;
import com.congress.repository.AboutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


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
    public String getAbout(Model model) {
        model.addAttribute("aboutList", aboutRepository.findAll());
        model.addAttribute("pageTitle", "About");
        return "/pages/about/aboutListView";
    }

    /**
     * this controller display a specific description in about
     */
    @GetMapping("/{id}")
    public String getAbout(@PathVariable long id, Model model) throws Exception {
        Optional<About> finded = aboutRepository.findById(id);
        About titleAbout = finded.get();
        if (finded.isPresent()) {
            titleAbout = finded.get();
        } else {
            throw new Exception("Can't find description, please write description ");
            model.addAttribute("titleAbout", titleAbout);
            model.addAttribute("pageTitle", "About" + titleAbout.getTitle());
            return "pages/about/aboutOneDescription";
        }

        /**
         * this controller is used to create description in About
         *
         */
        @PostMapping
        public String createAbout (@Valid @ModelAttribute About titleAbout, BindingResult bindingAbout, Model model){
            if (bindingAbout.hasErrors()) {
                model.addAttribute("httpMethod", "POST");
                model.addAttribute("pathMethod", "/about");
                model.addAttribute("newAbout", titleAbout);
                return "pages/about/aboutFormView";
            }
            titleAbout = aboutRepository.save(titleAbout);
            return "redirect:/about/" + titleAbout.getId();
        }
    }
}
