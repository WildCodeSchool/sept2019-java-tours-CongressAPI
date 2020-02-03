package com.congress.controller;

import com.congress.entity.Congress;
import com.congress.entity.SocialLink;
import com.congress.services.CongressService;
import com.congress.services.SocialLinkService;
import com.congress.storage.StorageService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("congress/{congressId}/socialLink")
public class SocialLinkController {

	private final CongressService congressService;
	private final SocialLinkService socialLinkService;

	public SocialLinkController(CongressService congressService, SocialLinkService socialLinkService, StorageService storageService) {
		this.congressService = congressService;
		this.socialLinkService = socialLinkService;
	}


	/**
	 * This controller display the list of social link
	 *
	 * @param model
	 * @return Template of link view list
	 */
	@GetMapping
	public String getSocialLink(@PathVariable long congressId, Model model) throws IOException {
		model.addAttribute("page", "socialLink");
		model.addAttribute("currentCongress", congressService.findById(congressId));
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
    public String getSocialLink(@PathVariable long congressId, @PathVariable long id, @Valid @ModelAttribute SocialLink newSocialLink, Model model) throws Exception {
		Congress currentCongress = congressService.findById(congressId);
		SocialLink currentSocialLink = socialLinkService.findById(id);
		model.addAttribute("newSocialLink", currentSocialLink);
		model.addAttribute("currentCongress", currentCongress);
		model.addAttribute("page", "socialLinks");
		model.addAttribute("currentSocialLink", currentSocialLink);
		model.addAttribute("pageTitle", "SocialLink" + currentSocialLink.getId());
		return "pages/socialLink/socialLinkMainView";
	}

	/**
	 * This controller is used to create a social link
	 *
	 * @param currentSocialLink The model of the social link
	 * @param model
	 * @return Redirect to the social link view
	 */
	@PostMapping("/create")
	public String createSocialLink(@PathVariable long congressId, @Valid @ModelAttribute("newSocialLink") SocialLink currentSocialLink, BindingResult binding, Model model) throws NotFoundException, IOException {
		if (binding.hasErrors()) {
			model.addAttribute("httpMethod", "POST");
			model.addAttribute("pathMethod", "/congress/" + congressId + "/socialLink/create");
			model.addAttribute("newSocialLink", currentSocialLink);
			return "pages/socialLink/socialLinkFormView";
		}
		Congress currentCongress = congressService.findById(congressId);
		currentCongress.addSocialLink(currentSocialLink);
		currentSocialLink = socialLinkService.create(currentSocialLink);
		congressService.update(currentCongress);
		return "redirect:/congress/" + congressId + "/socialLink/" + currentSocialLink.getId();
	}


	/**
	 * This controller is used to delete a social link
	 *
	 * @param id                The id of the social link
	 * @param currentSocialLink The model of the social link
	 * @return Redirect to the home page
	 */
	@GetMapping("/{id}/delete")
	public String deleteSocialLink(@PathVariable long congressId, @PathVariable long id, @ModelAttribute SocialLink currentSocialLink) throws Exception {
		Congress currentCongress = congressService.findById(congressId);
		SocialLink toDelete = socialLinkService.findById(id);
		currentCongress.removeSocialLink(toDelete);
		congressService.update(currentCongress);
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
		SocialLink newSocialLink = socialLinkService.findById(id);
		model.addAttribute("newSocialLink", newSocialLink);
		model.addAttribute("pathMethod", "congress/" + congressId + "/socialLink/" + id + "/edit");
		model.addAttribute("pageTitle", "Update " + newSocialLink.getName());
		model.addAttribute("page", "currentSocialLink");
		model.addAttribute("currentCongress", congressService.findById(congressId));
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
		model.addAttribute("page", "socialLink");
		model.addAttribute("currentCongress", congressService.findById(congressId));
		model.addAttribute("pathMethod", "/congress/" + congressId + "/socialLink/create");
		model.addAttribute("newSocialLink", new SocialLink());
		return "pages/socialLink/socialLinkFormView";
	}

}

