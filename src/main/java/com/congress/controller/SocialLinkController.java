package com.congress.controller;

import com.congress.entity.Congress;
import com.congress.entity.SocialLink;
import com.congress.repository.CongressRepository;
import com.congress.repository.SocialLinkRepository;
import com.congress.storage.StorageService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("congress/{congressId}/socialLink")
public class SocialLinkController {

	@Autowired
	private CongressRepository congressRepository;

	@Autowired
	private SocialLinkRepository socialLinkRepository;

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
		model.addAttribute("page", "socialLink");
		model.addAttribute("currentCongress", congressRepository.findById(congressId).get());
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
        Optional<Congress> finded = congressRepository.findById(congressId);
        Congress currentCongress = finded.get();
        if (finded.isPresent()) {
            currentCongress = finded.get();
        } else {
            throw new Exception("Can't find social link with id=\" + id);");
        }
        SocialLink currentSocialLink = null;
        for (SocialLink socialLink : currentCongress.getSocialLinks()) {
            if (socialLink.getId() == id) {
                currentSocialLink = socialLink;
            }
        }

        if (currentSocialLink == null) {
            throw new NotFoundException("Can't find social link : " + id);
        }
        model.addAttribute("newSocialLink", currentSocialLink);
        model.addAttribute("currentCongress", congressRepository.findById(congressId).get());
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
	public String createSocialLink(@PathVariable long congressId, @Valid @ModelAttribute("newSocialLink") SocialLink currentSocialLink, BindingResult binding, Model model) throws NotFoundException {
		if (binding.hasErrors()) {
			model.addAttribute("httpMethod", "POST");
			model.addAttribute("pathMethod", "/congress/" + congressId + "/socialLink/create");
			model.addAttribute("newSocialLink", currentSocialLink);
			return "pages/socialLink/socialLinkFormView";
		}
		storageService.store(currentSocialLink.getLogo());

		currentSocialLink.setLogoUrl("/files/" + currentSocialLink.getLogo().getOriginalFilename());
		Congress currentCongress = congressRepository.findById(congressId).orElseThrow(() -> new NotFoundException("Can't find congress with id:" + congressId));
		currentCongress.addSocialLink(currentSocialLink);
		socialLinkRepository.save(currentSocialLink);
		congressRepository.save(currentCongress);
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
	public String deleteSocialLink(@PathVariable long congressId, @PathVariable long id, @ModelAttribute SocialLink currentSocialLink) throws NotFoundException {
		Congress currentCongress = congressRepository.findById(congressId)
				.orElseThrow(() -> new NotFoundException("Can't find congress with id:" + congressId));
		SocialLink toDelete = socialLinkRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Can't find social link with id: " + id));
		currentCongress.removeSocialLink(toDelete);
		congressRepository.save(currentCongress);
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
		Optional<SocialLink> finded = socialLinkRepository.findById(id);
		if (finded.isPresent()) {
			newSocialLink = finded.get();
		} else {
			throw new Exception("Can't find poster with id=" + id);
		}
		if (!model.containsAttribute("newSocialLink"))
			model.addAttribute("newSocialLink", newSocialLink);

		model.addAttribute("pathMethod", "congress/" + congressId + "/socialLink/" + id + "/edit");
		model.addAttribute("pageTitle", "Update " + newSocialLink.getName());
		model.addAttribute("page", "currentSocialLink");
		model.addAttribute("currentCongress", congressRepository.findById(congressId).get());

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
		model.addAttribute("currentCongress", congressRepository.findById(congressId).get());
		model.addAttribute("pathMethod", "/congress/" + congressId + "/socialLink/create");
		model.addAttribute("newSocialLink", new SocialLink());
		return "pages/socialLink/socialLinkFormView";
	}

}

