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
    public String getAbouts(@PathVariable long congressId, Model model) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        model.addAttribute("page", "about");
        model.addAttribute("currentCongress", currentCongress);
        model.addAttribute("pageTitle", "About");
        model.addAttribute("pathMethod", "/congress/" + currentCongress.getId() + "/about/create");
        model.addAttribute("newAbout", new About());
        return "pages/about/aboutListView";
    }

    /**
     * this controller display a specific description in about
     */
    @GetMapping("/{id}")
    public String getAbout(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        About currentAbout = aboutService.findById(id);
        model.addAttribute("currentCongress", currentCongress);
        model.addAttribute("page", "about");
        model.addAttribute("about", currentAbout);
        model.addAttribute("pageTitle", "About" + currentAbout.getTitle());
        model.addAttribute("pathMethod",         "/congress/"+ currentCongress.getId()+ "/about/"+id+"/edit");
        model.addAttribute("newAbout", currentAbout);
        return "pages/about/aboutOneDescription";
    }

    /**
     * this controller is used to create description in About
     *
     * @param model      The id of update about
     * @return redirect to about view
     */
    @PostMapping("/create")
    public String createAbout(@PathVariable long congressId, @Valid @ModelAttribute About currentAbout, BindingResult bindingAbout, Model model) throws NotFoundException, IOException {
        if (bindingAbout.hasErrors()) {
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod", "/congress/" + congressId + "/about/create");
            model.addAttribute("newAbout", currentAbout);
            return "pages/about/aboutFormView";
        }
        Congress currentCongress = congressService.findById(congressId);
        aboutService.create(currentAbout);
        congressService.update(currentCongress);
        return "redirect:/congress/" + congressId + "/about/" + currentAbout.getId();
    }


    /**
     * This controller is used to delete one categorie of about
     *
     * @param id         the id of about
     * @param titleAbout the model of the page About
     * @return redirect to the home page
     */
    @GetMapping("/{id}/delete"  )
    public String deleteAbout(@PathVariable long congressId, @PathVariable long id, @ModelAttribute About titleAbout) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        About toDelete = aboutService.findById(id);
        currentCongress.removeAbout(toDelete);
        aboutService.delete(id);
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
    @PostMapping("/{id}/edit")
    public String updateAboutForm(@PathVariable long congressId, @PathVariable Long id, @ModelAttribute("newAbout") About newAbout,BindingResult bindingResult, Model model) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        About currentAbout = aboutService.findById(id);
        if(bindingResult.hasErrors()){
            model.addAttribute("currentCongress", currentCongress);
            model.addAttribute("page", "abouts");
            model.addAttribute("currentAbout", currentAbout);
            model.addAttribute("pageTitle", "About" + currentAbout.getTitle());
            model.addAttribute("pathMethod","/congress/"+ currentCongress.getId()+ "/about/edit");
            model.addAttribute("newAbout", newAbout);
            return "pages/about/aboutOneDescription";
        }
        congressService.update(currentCongress);
        aboutService.update(id, newAbout);
        return "redirect:/congress/" + congressId + "/about/"+ id;
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