package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.Alert;
import tech.noetzold.model.Company;

import java.util.List;

@ApplicationScoped
public class AlertRepository implements PanacheRepository<Alert> {

    public List<Alert> findAllByPcId(String pcId) {
        return list("pcId", pcId);
    }

    public List<Alert> findByCompany(int page, int size, Long companyId) {
        return find("company.companyId = ?1 order by dataCadastro", companyId)
                .page(Page.of(page, size))
                .list();
    }
}