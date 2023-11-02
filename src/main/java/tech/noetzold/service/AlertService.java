package tech.noetzold.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import tech.noetzold.model.Alert;
import tech.noetzold.model.Company;
import tech.noetzold.repository.AlertRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AlertService {

    @Inject
    AlertRepository alertaRepository;


    @Transactional
    public List<Alert> findAll(int page, int size, String sortBy) {
        Sort sort = Sort.ascending(sortBy);
        PanacheQuery<Alert> query = alertaRepository.findAll(sort);

        int offset = (page - 1) * size;
        return query.range(offset, (size-1)*page).list();
    }

    @CacheResult(cacheName = "alert")
    @Transactional
    public Alert findAlertaById(Long id) {
        Optional<Alert> optionalAlert = alertaRepository.findByIdOptional(id);
        return optionalAlert.orElse(null);
    }

    @Transactional
    public List<Alert> findAlertaByPcId(String pcId) {
        return alertaRepository.findAllByPcId(pcId);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "listalert")
    @CacheInvalidateAll(cacheName = "alert")
    public Alert saveAlert(Alert alert) {
        alertaRepository.persist(alert);
        return alert;
    }

    @Transactional
    public void deleteAlertaById(Long id) {
        alertaRepository.deleteById(id);
    }

    @Transactional
    public List<Alert> findAllByCompany(int page, int size, Long companyId) {
        return alertaRepository.findByCompany(page, size, companyId);
    }
}
