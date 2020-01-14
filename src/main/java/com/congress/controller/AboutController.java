package com.congress.controller;

import com.congress.entity.About;
import com.congress.repository.AboutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("congress/{congressId}/about")
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
    public String getAbout(@PathVariable long congressId, Model model) {
        model.addAttribute("aboutList", aboutRepository.findAll());
        model.addAttribute("pageTitle", "About");
        return "/pages/about/aboutListView";
    }

    /**
     * this controller display a specific description in about
     */
    @GetMapping("/{id}")
    public String getAbout(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
        Optional<About> finded = aboutRepository.findById(id);
        About titleAbout = finded.get();
        if (finded.isPresent()) {
            titleAbout = finded.get();
        } else {
            throw new Exception("Can't find description, please write description ");
        }
        model.addAttribute("titleAbout", titleAbout);
        model.addAttribute("pageTitle", "About" + titleAbout.getTitle());
        return "pages/about/aboutOneDescription";
    }

        /**
         * this controller is used to create description in About
         *
         * @param model         The id of update about
         * @param titleAbout    The model of About table
         * @return redirect to about view
         */
        @PostMapping
        public String createAbout(@PathVariable long congressId, @Valid @ModelAttribute About titleAbout, BindingResult bindingAbout, Model model){
            if (bindingAbout.hasErrors()) {
                model.addAttribute("httpMethod", "POST");
                model.addAttribute("pathMethod", "/about");
                model.addAttribute("newAbout", titleAbout);
                return "pages/about/aboutFormView";
            }
            titleAbout = aboutRepository.save(titleAbout);
            return "redirect:/about/" + titleAbout.getId();
        }

    /**
     * this controller is used to update the table about
     *
     * @param id            the id of updated congress
     * @param titleAbout    the model of table About
     * @return Redirect to about view
     */
    @PutMapping("/{id}")
    public String updatedAbout(@PathVariable long congressId, @PathVariable long id, @Valid @ModelAttribute About titleAbout, BindingResult binding, Model model, RedirectAttributes redirectAttributes){
        if(binding.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.titleAbout", "PUT");
            redirectAttributes.addFlashAttribute("httpMethod","PUT");
            redirectAttributes.addFlashAttribute("pathMethod","/about" + id);
            redirectAttributes.addFlashAttribute("newAbout", titleAbout);
            return "redirect:/about/" + id + "edit";
        }
        titleAbout = aboutRepository.save(titleAbout);
        return "redirect:/about" + titleAbout.getId();
    }

    /**
     * This controller is used to delete one categorie of about
     *
     * @param id         the id of about
     * @param titleAbout the model of the page About
     * @return           redirect to the home page
     */
    @DeleteMapping("/{id}")
    public String deleteAbout(@PathVariable long congressId, @PathVariable long id, @ModelAttribute About titleAbout) {
        aboutRepository.delete(titleAbout);
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
        model.addAttribute("httpMethod", "PUT");
        model.addAttribute("pathMethod", "/about/" + id);
        model.addAttribute("pageTitle", "Update " + newAbout.getTitle());

        return "pages/about/aboutFormView";
    }
    @GetMapping("/create")
    public String createAboutForm(@PathVariable long congressId, Model model) throws Exception{
        model.addAttribute("httpMethod", "POST");
        model.addAttribute("pathMethod", "/about");
        model.addAttribute("newAbout", new About());
        return "pages/about/aboutFormView";
    }
}

