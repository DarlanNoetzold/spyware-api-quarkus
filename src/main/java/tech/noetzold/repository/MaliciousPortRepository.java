package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.MaliciousPort;

import java.util.Optional;

@ApplicationScoped
public class MaliciousPortRepository implements PanacheRepository<MaliciousPort> {

    public Optional<MaliciousPort> findByVulnarableBanners(String vulnarableBanners) {
        return find("vulnarableBanners", vulnarableBanners).firstResultOptional();
    }
}

