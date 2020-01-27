package com.congress.controller;

import com.congress.entity.About;
import com.congress.entity.Congress;
import com.congress.services.AboutService;
import com.congress.services.CongressService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;


@Controller
@RequestMapping("congress/{congressId}/about")
public class AboutController {
    private final AboutService aboutService;
    private final CongressService congressService;

    public AboutController(AboutService aboutService, CongressService congressService) {
        this.aboutService = aboutService;
        this.congressService = congressService;
    }

    /**
     * *this controller display the list of About cotaints
     *
     * @param model
     * @return Template of about view list
     */
    @GetMapping
    public String getAbout(@PathVariable long congressId, Model model) throws Exception {
        model.addAttribute("page", "about");
        model.addAttribute("currentCongress", congressService.findById(congressId));
        model.addAttribute("pageTitle", "About");
        model.addAttribute("newAbout", new About());
        return "/pages/about/aboutListView";
    }

    /**
     * this controller display a specific description in about
     */
    @GetMapping("/{id}")
    public String getAbout(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
        About currentAbout = aboutService.findById(id);
        model.addAttribute("currentCongress", congressService.findById(congressId));
        model.addAttribute("page", "abouts");
        model.addAttribute("about", currentAbout);
        model.addAttribute("pageTitle", "About" + currentAbout.getTitle());
        return "pages/about/aboutOneDescription";
    }

    /**
     * this controller is used to create description in About
     *
     * @param model      The id of update about
     * @param titleAbout The model of About table
     * @return redirect to about view
     */
    @PostMapping("/create")
    public String createAbout(@PathVariable long congressId, @Valid @ModelAttribute About titleAbout, BindingResult bindingAbout, Model model) throws NotFoundException, IOException {
        if (bindingAbout.hasErrors()) {
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod", "/congress/" + congressId + "/about/create");
            model.addAttribute("newAbout", titleAbout);
            return "pages/about/aboutFormView";
        }
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addAbout(titleAbout);
        aboutService.create(titleAbout);
        congressService.update(currentCongress);
        return "redirect:/congress/" + congressId + "/about/" + titleAbout.getId();
    }


    /**
     * This controller is used to delete one categorie of about
     *
     * @param id         the id of about
     * @param titleAbout the model of the page About
     * @return redirect to the home page
     */
    @GetMapping("/{id}/delete")
    public String deleteAbout(@PathVariable long congressId, @PathVariable long id, @ModelAttribute About titleAbout) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        About toDelete = aboutService.findById(id);
        currentCongress.removeAbout(toDelete);
        congressService.update(currentCongress);
        return "redirect:/";
    }

    /**
     * This controller is used to display the about update form
     *
     * @param id    the id of the table About
     * @param model
     * @return      template of about view form
     * @throws Exception
     */
    @GetMapping("/{id}/edit")
    public String updateAboutForm(@PathVariable long congressId, @PathVariable Long id, Model model) throws Exception {
        About newAbout = aboutService.findById(id);
        model.addAttribute("pathMethod", "congress/" + congressId + "/about/" + id + "/edit");
        model.addAttribute("pageTitle", "Update " + newAbout.getTitle());
        model.addAttribute("page", "about");
        model.addAttribute("currentCongress", congressService.findById(congressId));

        return "pages/about/aboutFormView";
    }
    @GetMapping("/create")
    public String createAboutForm(@PathVariable long congressId, Model model) throws Exception{
        model.addAttribute("page", "about");
        model.addAttribute("currentCongress", congressService.findById(congressId));
        model.addAttribute("pathMethod", "/congress/" + congressId + "/about/create");
        model.addAttribute("newAbout", new About());
        return "pages/about/aboutFormView";
    }
}

