package com.congress.services;

import com.congress.entity.About;
import com.congress.exception.AboutNotFoundExcepetion;
import com.congress.repository.AboutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AboutService implements CrudService<About> {
    private final AboutRepository aboutRepository;

    public AboutService(AboutRepository aboutRepository) {
        this.aboutRepository = aboutRepository;
    }

    @Override
    public List<About> findAll() {
        return aboutRepository.findAll();
    }

    public List<About> findByCongressId(long congressId) {
        return aboutRepository.findByCongressId(congressId);
    }

    @Override
    public About findById(long id) throws Exception {
        return aboutRepository.findById(id).orElseThrow(() -> new AboutNotFoundExcepetion(id));
    }

    @Override
    public About create(About entity) {
        return aboutRepository.save(entity);
    }

    @Override
    public About update(long id, About entity) throws Exception {
        if (!aboutRepository.existsById(id)) {
            throw new AboutNotFoundExcepetion(id);
        }
        entity.setId(id);
        return aboutRepository.save(entity);
    }

    @Override
    public About update(About entity) throws Exception {
        if (!aboutRepository.existsById(entity.getId())) {
            throw new AboutNotFoundExcepetion(entity.getId());
        }
        return aboutRepository.save(entity);
    }

    @Override
    public void delete(long id) throws Exception {
        if (!aboutRepository.existsById(id)) {
            throw new AboutNotFoundExcepetion(id);
        }
        aboutRepository.deleteById(id);
    }
}
