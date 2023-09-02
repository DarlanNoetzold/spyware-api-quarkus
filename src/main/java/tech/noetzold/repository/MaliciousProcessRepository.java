package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.MaliciousProcess;

import java.util.Optional;

@ApplicationScoped
public class MaliciousProcessRepository implements PanacheRepository<MaliciousProcess> {

    public Optional<MaliciousProcess> findByNameExe(String nameExe) {
        return find("nameExe", nameExe).firstResultOptional();
    }
}

