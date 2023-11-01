package tech.noetzold.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.Company;
import tech.noetzold.model.Company;
import tech.noetzold.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CompanyService {

    @Inject
    CompanyRepository companyRepository;

    @Transactional
    public List<Company> findAll(int page, int size, String sortBy) {
        Sort sort = Sort.ascending(sortBy);
        PanacheQuery<Company> query = companyRepository.findAll(sort);

        int offset = (page - 1) * size;
        return query.range(offset, (size-1)*page).list();
    }

    @Transactional
    public Company findCompanyById(Long id) {
        Optional<Company> optionalCompany = companyRepository.findByIdOptional(id);
        return optionalCompany.orElse(null);
    }

    @Transactional
    public Company saveCompany(Company alert) {
        companyRepository.persist(alert);
        return alert;
    }

    @Transactional
    public Company updateCompany(Company updatedCompany) {
        if (updatedCompany == null || updatedCompany.getCompanyId() == null) {
            throw new WebApplicationException("Invalid data for Company update", Response.Status.BAD_REQUEST);
        }

        Company existingCompany = findCompanyById(updatedCompany.getCompanyId());
        if (existingCompany == null) {
            throw new WebApplicationException("Company not found", Response.Status.NOT_FOUND);
        }

        existingCompany.setDocument(updatedCompany.getDocument());
        existingCompany.setName(updatedCompany.getName());
        companyRepository.persist(existingCompany);

        return existingCompany;
    }

    @Transactional
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }
}
