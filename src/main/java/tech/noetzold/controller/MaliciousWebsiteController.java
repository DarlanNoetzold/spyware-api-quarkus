package tech.noetzold.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.MaliciousWebsite;
import tech.noetzold.service.MaliciousWebsiteService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collection;

@Path("/website")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaliciousWebsiteController {
    @Inject
    MaliciousWebsiteService maliciousWebsiteService;

    private static final Logger logger = LoggerFactory.getLogger(MaliciousWebsiteController.class);

    @GET
    @Path("/getAll")
    @Transactional
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        Collection<MaliciousWebsite> maliciousWebsites = maliciousWebsiteService.findAllMaliciousWebsite(page, size, sortBy);
        if (maliciousWebsites.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(maliciousWebsites).build();
    }

    @GET
    @Path("/get/{id}")
    @Transactional
    public Response getMaliciousWebsiteById(@PathParam("id") long id) {
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        MaliciousWebsite maliciousWebsite = maliciousWebsiteService.findMaliciousWebsiteById(id);
        if (maliciousWebsite == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(maliciousWebsite).build();
    }

    @POST
    @Path("/save")
    public Response save(MaliciousWebsite maliciousWebsite) {
        if (maliciousWebsite == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        MaliciousWebsite existingMaliciousWebsite = maliciousWebsiteService.findMaliciousWebsiteByUrl(maliciousWebsite.getUrl());

        if (existingMaliciousWebsite != null) {
            return Response.ok(existingMaliciousWebsite).status(Response.Status.CREATED).build();
        }

        try {
            maliciousWebsite = maliciousWebsiteService.saveMaliciousWebsite(maliciousWebsite);
            logger.info("Create MaliciousWebsite: " + maliciousWebsite.getUrl());
            return Response.ok(maliciousWebsite).status(Response.Status.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/remove/{id}")
    public Response remover(@PathParam("id") Long id) {
        maliciousWebsiteService.deleteWebsiteById(id);
        logger.info("Remove MaliciousWebsite: " + id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}

