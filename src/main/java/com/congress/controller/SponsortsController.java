package com.congress.controller;


import com.congress.entity.Congress;
import com.congress.entity.Sponsorts;
import com.congress.repository.CongressRepository;
import com.congress.repository.SponsortsRepository;
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
@RequestMapping("congress/{congressId}/sponsorts")
public class SponsortsController {

    @Autowired
    private SponsortsRepository sponsortsRepository;

    @Autowired
    private CongressRepository congressRepository;

    private StorageService storageService;

    @Autowired
    public void FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }
    /*
        crud sponsors
     */
    /**
     * this controller display a list of sponsors
     *
     * @param model
     * @return Template of sponsorts view list
     */

    @GetMapping("/sponsorts")
    public String getSponsors(@PathVariable long congressId, Model model){
        model.addAttribute("sponsortsList", congressRepository.findById(congressId).get());
        model.addAttribute("pageTitle", "List Sponsorts");
        return"pages/sponsorts/sponsortsListView";

    }

    /**
     * this controller display specific description in sponsors
     *
     * @param congressId
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/sponsorts/{id}")
    public String getSponsorts(@PathVariable Long congressId, @PathVariable long id, Model model)throws Exception {
        Optional<Congress> finded = congressRepository.findById(congressId);
        Congress currentCongress= finded.get();
        if (finded.isPresent()){
            currentCongress = finded.get();
        }else {
            throw new Exception("Filed empty, please write a comment");
        }
        Sponsorts currentSponsorts = null;
        for(Sponsorts sponsorts : currentCongress.getSponsorts()){
            if (sponsorts.getId() == id){
                currentSponsorts = sponsorts;
            }
        }
        if (currentSponsorts == null){
            throw new NotFoundException("Can't find sponsorts with : " + id);
        }
        model.addAttribute("sponsorts",currentSponsorts);
        model.addAttribute("pageTitle","Sponsorts" + currentSponsorts.getName());
        return "pages/sponsorts/sponsortsMainView";

    }

    /**
     * this controller is used to create a new sponsor
     *
     * @param congressId     this id is used to connect with the current conress
     * @param model          this id is used for update Sponsors
     * @return               redirect to Sponsors view
     */
    @PostMapping("/create")
    public String createSponsorts(@PathVariable long congressId, @Valid @ModelAttribute("newSponsorts") Sponsorts currentSponsorts, BindingResult bindingSponsors, Model model) {
        if(bindingSponsors.hasErrors()){
            model.addAttribute("httpMethod","POST");
            model.addAttribute("pathMethod", "/congress/" + congressId +"/sponsorts/create");
            model.addAttribute("newSponsors", currentSponsorts);
            return "pages/sponsorts/sponsortsFormView";
        }
        Congress currentCongress = congressRepository.findById(congressId).orElseThrow() -> new NotFoundException("Can't find congress with id:" + congressId);
        currentCongress.addSponsorts(currentSponsorts);
        congressRepository.save(currentCongress);
        return "redirect:/congress/"+congressId+"/sponsorts/"+ currentSponsorts.getId();
    }


    /**
     * this controller is used to delete a sponsors
     *
     * @param id                the id of sponsors
     * @param currentSponsorts   the model of the page sponsors
     * @return                  redirect to the home page
     */
    @GetMapping("/{id}/delete")
    public String deleteSponsorts(@PathVariable long congressId, @PathVariable long id, @ModelAttribute Sponsorts currentSponsorts)throws NotFoundException{
        Congress currentCongress = congressRepository.findById(congressId)
                .orElseThrow() -> new NotFoundException("Can't find congress with id:" + congressId);
        Sponsorts toDelete = sponsortsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find sponsorts with id: " + id));
        currentCongress.removeSponsorts(toDelete);
        congressRepository.save(currentCongress);
        return "redirect:/";
    }

    /**
     *
     * @param congressId
     * @param id
     * @param model
     * @return
     * @throws Exception
     */

    @GetMapping("/{id}/edit")
    public String updateSponsortsForm(@PathVariable long congressId, @PathVariable Long id, Model model) throws Exception{
        Sponsorts newSponsorts;
        Optional<Sponsorts> finded = sponsortsRepository.findById(id);
        if (finded.isPresent()){
            newSponsorts = finded.get();
        } else {
            throw new Exception("filed are empty, please add some new element or return to home");
        }
        if (!model.containsAttribute("newSponsorts")){
            model.addAttribute("newSponsorts", newSponsorts);
        }
        model.addAttribute("pathMethod","/congress/" + congressId + "/sponsorts/" + id +"/edit");
        model.addAttribute("pageTitle", "Update " + newSponsorts.getName());

        return "pages/sponsorts/sponsortsFormView";
    }
    @GetMapping("/create")
    public String createSponsortsForm(@PathVariable long congressId, Model model) throws Exception{
        model.addAttribute("pathMethod","/congress/" + congressId +"/sponsorts/create");
        model.addAttribute("newSponsorts", new Sponsorts());
        return "pages/sponsorts/sponsortsFormView";
    }

}

