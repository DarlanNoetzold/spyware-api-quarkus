package tech.noetzold.service;

import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.Alert;
import tech.noetzold.repository.AlertRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@CacheResult(cacheName = "alert")
public class AlertService {

    @Inject
    AlertRepository alertaRepository;

    public List<Alert> findAll(int page, int size, String sortBy) {
        Sort sort = Sort.ascending(sortBy);
        PanacheQuery<Alert> query = alertaRepository.findAll(sort);
        return query.page(Page.of(page, size)).list();
    }

    public Alert findAlertaById(Long id) {
        Optional<Alert> optionalAlert = alertaRepository.findByIdOptional(id);
        return optionalAlert.orElseThrow(() -> new WebApplicationException("Alert not found", Response.Status.NOT_FOUND));
    }

    public List<Alert> findAlertaByPcId(String pcId) {
        return alertaRepository.findAllByPcId(pcId);
    }

    @Transactional
    public Alert saveAlerta(Alert alert) {
        alertaRepository.persist(alert);
        return alert;
    }

    @Transactional
    public void deleteAlertaById(Long id) {
        alertaRepository.deleteById(id);
    }
}
