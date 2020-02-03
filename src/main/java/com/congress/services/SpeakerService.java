package com.congress.services;

import com.congress.entity.Congress;
import com.congress.entity.Speaker;
import com.congress.exception.SpeakerNotFoundException;
import com.congress.repository.SpeakerRepository;
import com.congress.storage.StorageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpeakerService implements CrudService<Speaker> {

    private final SpeakerRepository speakerRepository;
    private final CongressService congressService;
    private final StorageService storageService;

    public SpeakerService(SpeakerRepository speakerRepository, CongressService congressService, StorageService storageService){
        this.speakerRepository = speakerRepository;
        this.congressService = congressService;
        this.storageService = storageService;

    }

    @Override
    public List<Speaker> findAll() {
        return speakerRepository.findAll();
    }
    public List<Congress> findByCongressId(long congressId){
        return speakerRepository.findByCongressId(congressId);
    }

    @Override
    public Speaker findById(long id) throws Exception {
        return speakerRepository.findById(id).orElseThrow(() -> new SpeakerNotFoundException(id));
    }

    @Override
    public Speaker create(Speaker entity) {
        storageService.store(entity.getPhoto());
        entity.setPhoto_url("/files/" + entity.getPhoto().getOriginalFilename());
        return speakerRepository.save(entity);
    }

    @Override
    public Speaker update(long id, Speaker entity) throws Exception {
        if(!speakerRepository.existsById(id)){
            throw new SpeakerNotFoundException(id);
        }
        entity.setId(id);
        return speakerRepository.save(entity);
    }

    @Override
    public Speaker update(Speaker entity) throws Exception {
        if (!speakerRepository.existsById(entity.getId())){
            throw new SpeakerNotFoundException(entity.getId());
        }
        if (entity.getPhoto() !=null && !entity.getPhoto().isEmpty()) {
            storageService.store(entity.getPhoto());
            entity.setPhoto_url("/files/" + entity.getPhoto().getOriginalFilename());
        }
        return speakerRepository.save(entity);
    }

    @Override
    public void delete(long id) throws Exception {
        if(!speakerRepository.existsById(id)){
            throw new SpeakerNotFoundException(id);
        }
        speakerRepository.deleteById(id);
    }

    public void linkToCongress(Long congressId, long id) throws Exception{
        Congress congress = congressService.findById(congressId);
        Speaker speaker = this.findById(id);
        speaker.addCongress(congress);
        this.update(speaker);
        congressService.update(congress);
    }

    public void unLinkToCongress(Long congressId, long id) throws Exception{
        Congress congress = congressService.findById(congressId);
        Speaker speaker = this.findById(id);
        speaker.removeCongress(congress);
        this.update(speaker);
        congressService.update(congress);
    }
}
