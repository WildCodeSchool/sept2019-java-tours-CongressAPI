package com.congress.services;

import com.congress.entity.Congress;
import com.congress.exception.entity.CongressNotFoundException;
import com.congress.repository.CongressRepository;
import com.congress.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CongressService implements CrudService<Congress> {
    private final CongressRepository congressRepository;
    private final StorageService storageService;

    public CongressService(CongressRepository congressRepository, StorageService storageService) {
        this.congressRepository = congressRepository;
        this.storageService = storageService;
    }

    @Override
    public List<Congress> findAll() {
        return congressRepository.findAll();
    }

    @Override
    public Congress findById(long id) throws IOException {
        return congressRepository.findById(id).orElseThrow(() -> new CongressNotFoundException(id));
    }

    @Override
    public Congress create(Congress congress) {
        this.storeFiles(congress);
        return congressRepository.save(congress);
    }

    public Congress create(Congress congress, MultipartFile logo, MultipartFile banner) {
        congress.setLogo(logo);
        congress.setBanner(banner);
        this.storeFiles(congress);
        return congressRepository.save(congress);
    }


    @Override
    public Congress update(long id, Congress entity) {
        if (!congressRepository.existsById(id)) {
            throw new CongressNotFoundException(id);
        }
        entity.setId(id);
        this.storeFiles(entity);
        return congressRepository.save(entity);
    }

    @Override
    public Congress update(Congress entity) {
        if (!congressRepository.existsById(entity.getId())) {
            throw new CongressNotFoundException(entity.getId());
        }
        this.storeFiles(entity);
        return congressRepository.save(entity);
    }

    public Congress update(long id, MultipartFile logo, MultipartFile banner) throws IOException {
        if (!congressRepository.existsById(id)) {
            throw new CongressNotFoundException(id);
        }
        Congress entity = this.findById(id);
        entity.setLogo(logo);
        entity.setBanner(banner);
        this.storeFiles(entity);
        return this.update(entity);
    }

    @Override
    public void delete(long id) {
        if (!congressRepository.existsById(id)) {
            throw new CongressNotFoundException(id);
        }
        congressRepository.deleteById(id);
    }

    public void storeFiles(Congress congress) {
        if (congress.getLogo() != null && !congress.getLogo().isEmpty()) {
            storageService.store(congress.getLogo());
            congress.setLogo_url("/files/" + congress.getLogo().getOriginalFilename());
        }
        if (congress.getLogo() != null && !congress.getBanner().isEmpty()) {
            storageService.store(congress.getBanner());
            congress.setBanner_url("/files/" + congress.getBanner().getOriginalFilename());
        }
    }
}
