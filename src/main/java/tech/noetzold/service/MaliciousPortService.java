package tech.noetzold.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.MaliciousPort;
import tech.noetzold.repository.MaliciousPortRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MaliciousPortService {

    @Inject
    MaliciousPortRepository maliciousPortRepository;

    @Transactional
    public List<MaliciousPort> findAllMaliciousPort(int page, int size, String sortBy){
        Sort sort = Sort.ascending(sortBy);
        PanacheQuery<MaliciousPort> query = maliciousPortRepository.findAll(sort);

        int offset = (page - 1) * size;
        return query.range(offset, size*page).list();
    }

    @Transactional
    public MaliciousPort findMaliciousPortById(Long id){
        Optional<MaliciousPort> optionalMaliciousPort = maliciousPortRepository.findByIdOptional(id);
        return optionalMaliciousPort.orElseThrow(() -> new WebApplicationException("MaliciousPort not found", Response.Status.NOT_FOUND));
    }

    @Transactional
    public MaliciousPort saveMaliciousPort(MaliciousPort maliciousPort){
        maliciousPortRepository.persist(maliciousPort);
        return maliciousPort;
    }

    @Transactional
    public MaliciousPort updateMaliciousPort(MaliciousPort updatedMaliciousPort) {
        if (updatedMaliciousPort == null || updatedMaliciousPort.getId() == null) {
            throw new WebApplicationException("Invalid data for MaliciousPort update", Response.Status.BAD_REQUEST);
        }

        MaliciousPort existingMaliciousPort = findMaliciousPortById(updatedMaliciousPort.getId());
        if (existingMaliciousPort == null) {
            throw new WebApplicationException("MaliciousPort not found", Response.Status.NOT_FOUND);
        }

        existingMaliciousPort.setVulnarableBanners(updatedMaliciousPort.getVulnarableBanners());
        maliciousPortRepository.persist(existingMaliciousPort);

        return existingMaliciousPort;
    }

    @Transactional
    public void deleteMaliciousPortById(Long id){
        maliciousPortRepository.deleteById(id);
    }

    @Transactional
    public MaliciousPort findMaliciousPortByVulnarableBanners(String vulnarableBanners) {
        return maliciousPortRepository.findByVulnarableBanners(vulnarableBanners).orElse(null);
    }
}
