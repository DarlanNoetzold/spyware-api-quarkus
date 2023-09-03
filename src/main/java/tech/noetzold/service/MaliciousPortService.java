package tech.noetzold.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
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

    public List<MaliciousPort> findAllMaliciousPort(int page, int size, String sortBy){
        Sort sort = Sort.ascending(sortBy);
        PanacheQuery<MaliciousPort> query = maliciousPortRepository.findAll(sort);
        return query.page(Page.of(page, size)).list();
    }

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
    public void deleteMaliciousPortById(Long id){
        maliciousPortRepository.deleteById(id);
    }

    public MaliciousPort findMaliciousPortByVulnarableBanners(String vulnarableBanners) {
        return maliciousPortRepository.findByVulnarableBanners(vulnarableBanners).orElse(null);
    }
}
