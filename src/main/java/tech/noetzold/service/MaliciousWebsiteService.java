package tech.noetzold.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.MaliciousWebsite;
import tech.noetzold.repository.MaliciousWebsiteRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MaliciousWebsiteService {

    @Inject
    MaliciousWebsiteRepository maliciousWebsiteRepository;

    @Transactional
    public List<MaliciousWebsite> findAllMaliciousWebsite(int page, int size, String sortBy){
        Sort sort = Sort.ascending(sortBy);
        PanacheQuery<MaliciousWebsite> query = maliciousWebsiteRepository.findAll(sort);

        int offset = (page - 1) * size;
        return query.range(offset, (size-1)*page).list();
    }

    @Transactional
    public MaliciousWebsite findMaliciousWebsiteById(Long id){
        Optional<MaliciousWebsite> optionalMaliciousWebsite = maliciousWebsiteRepository.findByIdOptional(id);
        return optionalMaliciousWebsite.orElseThrow(() -> new WebApplicationException("MaliciousWebsite not found", Response.Status.NOT_FOUND));
    }

    @Transactional
    public MaliciousWebsite saveMaliciousWebsite(MaliciousWebsite maliciousPort){
        maliciousWebsiteRepository.persist(maliciousPort);
        return maliciousPort;
    }

    @Transactional
    public MaliciousWebsite updateMaliciousWebsite(MaliciousWebsite updatedMaliciousWebsite) {
        if (updatedMaliciousWebsite == null || updatedMaliciousWebsite.getId() == null) {
            throw new WebApplicationException("Invalid data for MaliciousProcess update", Response.Status.BAD_REQUEST);
        }

        MaliciousWebsite existingMaliciousWebsite = findMaliciousWebsiteById(updatedMaliciousWebsite.getId());
        if (existingMaliciousWebsite == null) {
            throw new WebApplicationException("MaliciousProcess not found", Response.Status.NOT_FOUND);
        }

        existingMaliciousWebsite.setUrl(updatedMaliciousWebsite.getUrl());
        maliciousWebsiteRepository.persist(existingMaliciousWebsite);

        return existingMaliciousWebsite;
    }

    @Transactional
    public void deleteWebsiteById(Long id){
        maliciousWebsiteRepository.deleteById(id);
    }

    @Transactional
    public MaliciousWebsite findMaliciousWebsiteByUrl(String url) {
        return maliciousWebsiteRepository.findByUrl(url).orElse(null);
    }
}
