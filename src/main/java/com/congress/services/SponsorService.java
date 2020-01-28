package com.congress.services;

import com.congress.entity.Congress;
import com.congress.entity.Sponsor;
import com.congress.exception.SponsorNotFoundException;
import com.congress.repository.SponsorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorService implements CrudService<Sponsor> {

    private final SponsorRepository sponsorRepository;
    private final CongressService congressService;


    public SponsorService(SponsorRepository sponsorRepository, CongressService congressService) {
        this.sponsorRepository = sponsorRepository;
        this.congressService = congressService;
    }

    @Override
    public List<Sponsor> findAll() {
        return sponsorRepository.findAll();
    }

    @Override
    public Sponsor findById(long id) throws Exception {
        return sponsorRepository.findById(id).orElseThrow(() -> new SponsorNotFoundException(id));
    }

    @Override
    public Sponsor create(Sponsor entity) {
        return sponsorRepository.save(entity);
    }

    @Override
    public Sponsor update(long id, Sponsor entity) throws Exception {
        if (!sponsorRepository.existsById(id)) {
            throw new SponsorNotFoundException(id);
        }
        entity.setId(id);
        return sponsorRepository.save(entity);
    }

    @Override
    public Sponsor update(Sponsor entity) throws Exception {
        if (!sponsorRepository.existsById(entity.getId())) {
            throw new SponsorNotFoundException(entity.getId());
        }
        return sponsorRepository.save(entity);
    }

    @Override
    public void delete(long id) throws Exception {
        if (!sponsorRepository.existsById(id)) {
            throw new SponsorNotFoundException(id);
        }
        sponsorRepository.deleteById(id);
    }

    public void linkToCongress(Long congressId, long id) throws Exception {
        Congress congress = congressService.findById(congressId);
        Sponsor sponsor = this.findById(id);
        sponsor.addCongress(congress);
        this.update(sponsor);
        congressService.update(congress);
    }

    public void unLinkToCongress(Long congressId, long id) throws Exception {
        Congress congress = congressService.findById(congressId);
        Sponsor sponsor = this.findById(id);
        sponsor.removeCongress(congress);
        this.update(sponsor);
        congressService.update(congress);
    }
}
