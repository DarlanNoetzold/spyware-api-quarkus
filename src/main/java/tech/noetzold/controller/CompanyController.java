package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import tech.noetzold.model.Company;
import tech.noetzold.service.CompanyService;

import java.util.Collection;
import java.util.Date;

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyController {

    @Inject
    CompanyService companyService;

    private static final Logger logger = Logger.getLogger(CompanyController.class);

    @GET
    @RolesAllowed("admin")
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        Collection<Company> companys = companyService.findAll(page, size, sortBy);
        if (companys.isEmpty()) {
            logger.error("There is no company");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        logger.info("Company returned quantity: " + companys.size());
        return Response.ok(companys).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getCompanyById(@PathParam("id") long id) {
        if (id <= 0) {
            logger.error("Invalid id: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Company company = companyService.findCompanyById(id);
        if (company == null) {
            logger.error("There is no company with the id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        logger.info("Company returned: " + id);
        return Response.ok(company).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response update(@PathParam("id") long id, Company updatedCompany) {
        if (id <= 0 || updatedCompany == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Company existingCompany = companyService.findCompanyById(id);
        if (existingCompany == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Company updated = companyService.updateCompany(existingCompany);

        return Response.ok(updated).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response save(Company company) {
        if (company == null || company.getDocument() == null) {
            logger.error("Invalid to save company: " + company);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            company.setRegisterDate(new Date());
            company = companyService.saveCompany(company);
            logger.info("Create Company: " + company.getName());
            return Response.ok(company).status(Response.Status.CREATED).build();
        } catch (Exception e) {
            logger.error("Error to save company: " + company.getName());
            e.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(company).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("id") Long id) {
        logger.info("Remove Company: " + id);
        companyService.deleteCompanyById(id);
        return Response.status(Response.Status.ACCEPTED).entity(new Company(0L, "Company removed", new Date(),"")).build();
    }
}
