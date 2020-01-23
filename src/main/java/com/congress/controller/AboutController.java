package com.congress.controller;

import com.congress.entity.About;
import com.congress.entity.Congress;
import com.congress.repository.AboutRepository;
import com.congress.repository.CongressRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("congress/{congressId}/about")
public class AboutController {

    @Autowired
    private CongressRepository congressRepository;

    @Autowired
    private AboutRepository aboutRepository;

    /**
     * *this controller display the list of About cotaints
     *
     * @param model
     * @return Template of about view list
     */
    @GetMapping
    public String getAbout(@PathVariable long congressId, Model model) {
        model.addAttribute("page", "about");
        model.addAttribute("currentCongress", congressRepository.findById(congressId).get());
        model.addAttribute("pageTitle", "About");
        return "/pages/about/aboutListView";
    }

    /**
     * this controller display a specific description in about
     */
    @GetMapping("/{id}")
    public String getAbout(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
        Optional<Congress> finded = congressRepository.findById(congressId);
        Congress currentCongress = finded.get();
        if (finded.isPresent()) {
            currentCongress = finded.get();
        } else {
            throw new Exception("Can't find description, please write description ");
        }
        About currentAbout = null;
        for (About about : currentCongress.getAbouts()) {
            if (about.getId() == id)
                currentAbout = about;
        }
        if (currentAbout == null)
            throw new NotFoundException("Can't find about with id: " + id);
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
    public String createAbout(@PathVariable long congressId, @Valid @ModelAttribute About titleAbout, BindingResult bindingAbout, Model model) throws NotFoundException {
        if (bindingAbout.hasErrors()) {
            model.addAttribute("httpMethod", "POST");
            model.addAttribute("pathMethod", "/congress/" + congressId + "/about/create");
            model.addAttribute("newAbout", titleAbout);
            return "pages/about/aboutFormView";
        }
        Congress currentCongress = congressRepository.findById(congressId).orElseThrow(() -> new NotFoundException("Can't find congress with id:" + congressId));
        currentCongress.addAbout(titleAbout);
        aboutRepository.save(titleAbout);
        congressRepository.save(currentCongress);
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
    public String deleteAbout(@PathVariable long congressId, @PathVariable long id, @ModelAttribute About titleAbout) throws NotFoundException {
        Congress currentCongress = congressRepository.findById(congressId)
                .orElseThrow(() -> new NotFoundException("Can't find congress with id:" + congressId));
        About toDelete = aboutRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find about with id: " + id));
        currentCongress.removeAbout(toDelete);
        congressRepository.save(currentCongress);
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
        About newAbout;
        Optional<About> finded = aboutRepository.findById(id);
        if (finded.isPresent()){
            newAbout = finded.get();
        } else {
            throw new Exception("There is no text please write something or return to home");
        }
        if(!model.containsAttribute("newAbout")) {
            model.addAttribute("newAbout", newAbout);
        }
        model.addAttribute("pathMethod", "congress/" + congressId + "/about/" + id + "/edit");
        model.addAttribute("pageTitle", "Update " + newAbout.getTitle());
        model.addAttribute("page", "about");
        model.addAttribute("currentCongress", congressRepository.findById(congressId).get());

        return "pages/about/aboutFormView";
    }
    @GetMapping("/create")
    public String createAboutForm(@PathVariable long congressId, Model model) throws Exception{
        model.addAttribute("page", "about");
        model.addAttribute("currentCongress", congressRepository.findById(congressId));
        model.addAttribute("pathMethod", "/congress/"+ congressId +"/about/create");
        model.addAttribute("newAbout", new About());
        return "pages/about/aboutFormView";
    }
}

