package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.Company;

@ApplicationScoped
public class CompanyRepository implements PanacheRepository<Company> {
}
