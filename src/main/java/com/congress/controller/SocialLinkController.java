package com.congress.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.congress.entity.SocialLink;
import com.congress.repository.SocialLinkRepository;
import com.congress.storage.StorageService;

@Controller
@RequestMapping("congress/{congressId}/socialLink")
public class SocialLinkController {
	
	@Autowired
	private SocialLinkRepository sociallinkRepository;
	
	private StorageService storageService;
	@Autowired
    public void FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }
	/**
     * This controller display the list of social link
     *
     * @param model
     * @return Template of link view list
     */
	@GetMapping
	public String getSocialLink(@PathVariable long congressId, Model model) {
		model.addAttribute("SocialLinkList", sociallinkRepository.findAll());
		model.addAttribute("pageTitle", "SocialLink");
		return "pages/socialLink/socialLinkListView";
	}
	 /**
     * This controller display the social link with specific {id}
     *
     * @param id    The id of the social link
     * @param model
     * @return Template of social link view
     * @throws Exception
     */
	@GetMapping("/{id}")
	public String getSocialLink(@PathVariable long congressId,@PathVariable long id, Model model) throws Exception {
		Optional<SocialLink> finded = sociallinkRepository.findById(id);
		SocialLink currentSocialLink ;
		if (finded.isPresent()) {
			currentSocialLink = finded.get();
	}	else {
		throw new Exception("Can't find social link with id=\" + id);");
	}
		model.addAttribute("currentSocialLink", currentSocialLink);
		model.addAttribute("pageTitle", "SocialLink");
		return "pages/socialLink/socialLinkListView";
	}
	/**
     * This controller is used to create a social link
     *
     * @param currentSocialLink The model of the social link
     * @param model
     * @return Redirect to the social link view
     */
	@PostMapping
	public String createSocialLink(@PathVariable long congressId, @Valid @ModelAttribute("newSocialLink") SocialLink currentSocialLink, BindingResult binding, Model model) {
		if(binding.hasErrors() ) {
			model.addAttribute("httpMethod", "POST");
			model.addAttribute("pathMethod", "/socialLink");
			model.addAttribute("newSocialLink", currentSocialLink);
			return "pages/socialLink/socialLinkFormView";
		}
		storageService.store(currentSocialLink.getLogo());
		storageService.store(currentSocialLink.getSocial_link_url());
		currentSocialLink.setLogo_url("/files/" + currentSocialLink.getLogo().getOriginalFilename());
		
		currentSocialLink = sociallinkRepository.save(currentSocialLink);
		return "redirect:/congress/"+congressId+"/socialLink/" + currentSocialLink.getId();
	}
	/**
     * This controller is used to update a social link
     *
     * @param id              The id of the updated social link
     * @param currentSocialLink The model of the social link
     * @return Redirect to the social link view
     */
	@PutMapping("/{id}")
    public String updateSocialLink(@PathVariable long congressId, @PathVariable long id, @Valid @ModelAttribute SocialLink currentSocialLink, BindingResult binding, Model model, RedirectAttributes redirectAttributes) {
        if(binding.hasErrors()){
            
            return "redirect:/socialLink/" + id + "edit";
        }
        currentSocialLink = sociallinkRepository.save(currentSocialLink);
        return "redirect:/socialLink/" + currentSocialLink.getId();
        
	}
        /**
         * This controller is used to delete a social link
         *
         * @param id              The id of the social link
         * @param currentSocialLink The model of the social link
         * @return Redirect to the home page
         */
	@DeleteMapping("/{id}")
    public String deleteSocialLink(@PathVariable long congressId, @PathVariable long id, @ModelAttribute SocialLink currentSocialLink) {
        sociallinkRepository.delete(currentSocialLink);
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
     public String updateSocialLinkForm(@PathVariable long congressId, @PathVariable long id, Model model) throws Exception {
    	SocialLink newSocialLink;
    	Optional<SocialLink> finded = sociallinkRepository.findById(id);
    	if (finded.isPresent()) {
    		newSocialLink = finded.get();
    	} else { 
    		throw new Exception("Can't find poster with id=" + id);
    	}
    	if (!model.containsAttribute("newSocial"))
    		model.addAttribute("newSocialLink", newSocialLink);
    	model.addAttribute("httpMethod", "PUT");
    	model.addAttribute("pathMethod", "/socialLink/" + id);
    	model.addAttribute("pageTitle", "Update " + newSocialLink.getSocial_link_url());
    	
    	return "pages/socialLink/socialLinkFormView";
    }
     
     /**
      * This controller is used to display the poster creation form
      *
      * @param model
      * @return Template of poster creation view form
      * @throws Exception
      */
     @GetMapping("/create")
     public String createSocialLinkForm(@PathVariable long congressId, Model model) throws Exception {
    	 model.addAttribute("pathMethod", "/congress/"+congressId+"/socialLink/create");
    	 model.addAttribute("newSocialLink", new SocialLink());
    	 return "pages/socialLink/socialLinkFormView";
     }
	
}

