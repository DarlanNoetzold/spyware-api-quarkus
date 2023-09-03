package tech.noetzold.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.BadLanguage;
import tech.noetzold.repository.BadLanguageRepository;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class BadLanguageService {

    @Inject
    BadLanguageRepository badLanguageRepository;

    public List<BadLanguage> findAllBadLanguage(int page, int size, String sortBy){
        Sort sort = Sort.ascending(sortBy);
        PanacheQuery<BadLanguage> query = badLanguageRepository.findAll(sort);
        return query.page(Page.of(page, size)).list();
    }

    public BadLanguage findBadLanguageById(Long id){
        Optional<BadLanguage> optionalBadLanguage = badLanguageRepository.findByIdOptional(id);
        return optionalBadLanguage.orElseThrow(() -> new WebApplicationException("BadLanguage not found", Response.Status.NOT_FOUND));
    }
    @Transactional
    public BadLanguage saveBadLanguage(BadLanguage badLanguage){
        badLanguageRepository.persist(badLanguage);
        return badLanguage;
    }
    @Transactional
    public void deleteBadLanguage(Long id){
        badLanguageRepository.deleteById(id);
    }

    public BadLanguage findBadLanguageByWord(String word) {
        return badLanguageRepository.findByWord(word).orElse(null);
    }
}
