package com.congress.services;

import com.congress.entity.Congress;
import com.congress.exception.CongressNotFoundException;
import com.congress.repository.CongressRepository;
import com.congress.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CongressService implements CrudService<Congress> {
    @Autowired
    private CongressRepository congressRepository;

    private StorageService storageService;

    @Autowired
    public void FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public List<Congress> findAll() {
        return congressRepository.findAll();
    }

    @Override
    public ResponseEntity<Congress> findById(long id) throws Exception {
        return ResponseEntity.ok(congressRepository.findById(id).orElseThrow(() -> new CongressNotFoundException(id)));
    }

    @Override
    public ResponseEntity<Congress> create(Congress congress) {
        this.storeFiles(congress);
        return ResponseEntity.ok(congressRepository.save(congress));
    }

    public void storeFiles(Congress congress) {
        if (!congress.getLogo().isEmpty()) {
            storageService.store(congress.getLogo());
            congress.setLogo_url("/files/" + congress.getLogo().getOriginalFilename());
        }
        if (!congress.getBanner().isEmpty()) {
            storageService.store(congress.getBanner());
            congress.setBanner_url("/files/" + congress.getBanner().getOriginalFilename());
        }
    }

    @Override
    public ResponseEntity<Congress> update(long id, Congress entity) {
        this.storeFiles(entity);
        entity.setId(id);

        return ResponseEntity.ok(congressRepository.save(entity));
    }

    @Override
    public ResponseEntity<Congress> delete(long id) throws Exception {
        return ResponseEntity.ok(congressRepository.findById(id).orElseThrow(() -> new CongressNotFoundException(id)));
    }

    public ResponseEntity<Congress> update(long id, MultipartFile logo, MultipartFile banner) {
        Congress entity = congressRepository.findById(id).orElseThrow(() -> new CongressNotFoundException(id));
        entity.setLogo(logo);
        entity.setBanner(banner);
        this.storeFiles(entity);
        return ResponseEntity.ok(congressRepository.save(entity));
    }
}
