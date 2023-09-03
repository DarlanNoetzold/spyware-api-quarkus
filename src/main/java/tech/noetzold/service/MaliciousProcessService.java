package tech.noetzold.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.MaliciousProcess;
import tech.noetzold.repository.MaliciousProcessRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MaliciousProcessService {

    @Inject
    MaliciousProcessRepository maliciousProcessRepository;

    public List<MaliciousProcess> findAllMaliciousProcess(int page, int size, String sortBy){
        Sort sort = Sort.ascending(sortBy);
        PanacheQuery<MaliciousProcess> query = maliciousProcessRepository.findAll(sort);
        return query.page(Page.of(page, size)).list();
    }

    public MaliciousProcess findMaliciousProcessById(Long id){
        Optional<MaliciousProcess> optionalMaliciousProcess = maliciousProcessRepository.findByIdOptional(id);
        return optionalMaliciousProcess.orElseThrow(() -> new WebApplicationException("MaliciousProcess not found", Response.Status.NOT_FOUND));
    }

    @Transactional
    public MaliciousProcess saveMaliciousProcess(MaliciousProcess maliciousPort){
        maliciousProcessRepository.persist(maliciousPort);
        return maliciousPort;
    }

    @Transactional
    public void deleteProcessById(Long id){
        maliciousProcessRepository.deleteById(id);
    }

    public MaliciousProcess findMaliciousProcessByNameExe(String nameExe) {
        return maliciousProcessRepository.findByNameExe(nameExe).orElse(null);
    }
}
