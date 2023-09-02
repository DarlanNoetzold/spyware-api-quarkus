package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.Alert;

import java.util.List;

@ApplicationScoped
public class AlertRepository implements PanacheRepository<Alert> {

    public List<Alert> findAllByPcId(String pcId) {
        return list("pcId", pcId);
    }
}