package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.MaliciousWebsite;

import java.util.Optional;

@ApplicationScoped
public class MaliciousWebsiteRepository implements PanacheRepository<MaliciousWebsite> {

    public MaliciousWebsite findByName(String url) {
        return find("url", url).firstResult();
    }
}
