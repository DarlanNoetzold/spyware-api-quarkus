package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.BadLanguage;


import java.util.Optional;

@ApplicationScoped
public class BadLanguageRepository implements PanacheRepository<BadLanguage> {

    public Optional<BadLanguage> findByWord(String word) {
        return find("word", word).firstResultOptional();
    }
}

