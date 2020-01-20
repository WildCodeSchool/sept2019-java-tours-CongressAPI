package com.congress.controller;

import java.util.Optional;
import javax.validation.Valid;

import com.congress.entity.Poster;
import com.congress.repository.PosterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("congress/{congressId}/poster")
public class PosterController {

	
	@Autowired
	private PosterRepository posterRepository;

	/**
     * This controller display the list of poster
     *
     * @param model
     * @return Template of poster view list
     */

	@GetMapping
	public String getPoster(@PathVariable long congressId, Model model) {
		model.addAttribute("posterList", posterRepository.findAll());
		model.addAttribute("pageTitle", "Poster");
		return "pages/poster/posterListView";
	}
	 /**
     * This controller display the poster with specific {id}
     *
     * @param id    The id of the poster
     * @param model
     * @return Template of poster view
     * @throws Exception
     */
	@GetMapping("/{id}")
	public String getPoster(@PathVariable long congressId,@PathVariable long id, Model model) throws Exception {
		Optional<Poster> finded = posterRepository.findById(id);
		Poster currentPoster;
		if (finded.isPresent()) {
			currentPoster = finded.get();
	}	else {
			throw new Exception("Can't find poster with id=" + id);
	}
		model.addAttribute("currentPoster", currentPoster);
		model.addAttribute("pageTitle", "Poster");
		return "pages/poster/posterListView";
	}
	
	 /**
     * This controller is used to create a poster
     *
     * @param currentPoster The model of the poster
     * @param model
     * @return Redirect to the poster view
     */
	@PostMapping
	public String createPoster(@PathVariable long congressId, @Valid @ModelAttribute("newPoster") Poster currentPoster, BindingResult binding, Model model) {
		if(binding.hasErrors()) {
			model.addAttribute("httpMethod", "POST");
			model.addAttribute("pathMethod",  "/poster");
			model.addAttribute("newPoster", currentPoster);
			return "pages/poster/posterFormView";
		}
		currentPoster = posterRepository.save(currentPoster);
        return "redirect:/congress/"+congressId+"/poster/" + currentPoster.getId();
	}
		/**
	     * This controller is used to update a poster
	     *
	     * @param id              The id of the updated poster
	     * @param currentPoster The model of the poster
	     * @return Redirect to the poster view
	     */
	@PutMapping("/{id}")
    public String updatePoster(@PathVariable long congressId, @PathVariable long id, @Valid @ModelAttribute Poster currentPoster, BindingResult binding, Model model, RedirectAttributes redirectAttributes) {
        if(binding.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.currentCongress", "PUT");
            redirectAttributes.addFlashAttribute("httpMethod", "PUT");
            redirectAttributes.addFlashAttribute("pathMethod", "/poster/" +id);
            redirectAttributes.addFlashAttribute("newPoster", currentPoster);
            return "redirect:/poster/" + id + "edit";
        }
        currentPoster = posterRepository.save(currentPoster);
        return "redirect:/poster/" + currentPoster.getId();
        
	}
        /**
         * This controller is used to delete a poster
         *
         * @param id              The id of the poster
         * @param currentPoster The model of the poster
         * @return Redirect to the home page
         */
	@DeleteMapping("/{id}")
    public String deletePoster(@PathVariable long congressId, @PathVariable long id, @ModelAttribute Poster currentPoster) {
        posterRepository.delete(currentPoster);
        return "redirect:/";
    }
	
	/**
     * This controller is used to display the congress update form
     *
     * @param id    The id of the congress
     * @param model
     * @return Template of congress update view form
     * @throws Exception
     */
	@GetMapping("/{id}/edit")
     public String updatePosterForm(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
    	Poster newPoster;
    	Optional<Poster> finded = posterRepository.findById(id);
    	if (finded.isPresent()) {
    		newPoster = finded.get();
    	} else { 
    		throw new Exception("Can't find poster with id=" + id);
    	}
    	if (!model.containsAttribute("newPoster"))
    		model.addAttribute("newPoster", newPoster);
    	model.addAttribute("httpMethod", "PUT");
    	model.addAttribute("pathMethod", "/poster/" + id);
    	model.addAttribute("pageTitle", "Update " + newPoster.getPoster_url());
    	
    	return "pages/poster/posterFormView";
    }
     
     /**
      * This controller is used to display the poster creation form
      *
      * @param model
      * @return Template of poster creation view form
      * @throws Exception
      */
     @GetMapping("/create")
     public String createPosterForm(@PathVariable long congressId, Model model) throws Exception {
    	 model.addAttribute("httpMethod", "POST");
    	 model.addAttribute("pathMethod", "/congress/"+congressId+"/poster");
    	 model.addAttribute("newPoster", new Poster());
    	 return "pages/poster/posterFormView";
     }
	
}