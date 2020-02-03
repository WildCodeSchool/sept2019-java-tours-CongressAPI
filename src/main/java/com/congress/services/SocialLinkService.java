package com.congress.services;

import com.congress.entity.SocialLink;
import com.congress.exception.entity.MapNotFoundException;
import com.congress.exception.entity.SocialLinkNotFoundException;
import com.congress.repository.SocialLinkRepository;
import com.congress.storage.StorageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocialLinkService implements CrudService<SocialLink> {
    private final SocialLinkRepository socialLinkRepository;
    private final StorageService storageService;

    public SocialLinkService(SocialLinkRepository socialLinkRepository, StorageService storageService) {
        this.socialLinkRepository = socialLinkRepository;
        this.storageService = storageService;
    }

    @Override
    public List<SocialLink> findAll() {
        return socialLinkRepository.findAll();
    }

    @Override
    public SocialLink findById(long id) throws Exception {
        return socialLinkRepository.findById(id).orElseThrow(() -> new SocialLinkNotFoundException(id));
    }

    @Override
    public SocialLink create(SocialLink entity) {
        if (!entity.getLogo().isEmpty()) {
            storageService.store(entity.getLogo());
            entity.setLogoUrl("/files/" + entity.getLogo().getOriginalFilename());
        }
        return socialLinkRepository.save(entity);
    }

    @Override
    public SocialLink update(long id, SocialLink entity) throws Exception {
        entity.setId(id);
        return this.update(entity);
    }

    @Override
    public SocialLink update(SocialLink entity) throws Exception {
        if (!socialLinkRepository.existsById(entity.getId())) {
            throw new MapNotFoundException(entity.getId());
        }
        if (!entity.getLogo().isEmpty()) {
            storageService.store(entity.getLogo());
            entity.setLogoUrl("/files/" + entity.getLogo().getOriginalFilename());
        }
        return socialLinkRepository.save(entity);
    }

    @Override
    public void delete(long id) throws Exception {

    }
}
