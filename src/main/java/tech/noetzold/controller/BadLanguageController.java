package tech.noetzold.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.BadLanguage;
import tech.noetzold.service.BadLanguageService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collection;

@Path("/language")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BadLanguageController {

    @Inject
    BadLanguageService badLanguageService;

    private static final Logger logger = LoggerFactory.getLogger(BadLanguageController.class);

    @GET
    @Path("/getAll")
    @Transactional
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        Collection<BadLanguage> badLanguages = badLanguageService.findAllBadLanguage(page, size, sortBy);
        if (badLanguages.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(badLanguages).build();
    }

    @GET
    @Path("/get/{id}")
    @Transactional
    public Response getBadLanguageById(@PathParam("id") long id) {
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        BadLanguage badLanguage = badLanguageService.findBadLanguageById(id);
        if (badLanguage == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(badLanguage).build();
    }

    @POST
    @Path("/save")
    public Response save(BadLanguage badLanguage) {
        if (badLanguage == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        BadLanguage existingBadLanguage = badLanguageService.findBadLanguageByWord(badLanguage.getWord());

        if (existingBadLanguage != null) {
            return Response.ok(existingBadLanguage).status(Response.Status.CREATED).build();
        }

        badLanguage = badLanguageService.saveBadLanguage(badLanguage);
        logger.info("Create badLanguage: " + badLanguage.getWord());
        return Response.ok(badLanguage).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/remove/{id}")
    public Response remover(@PathParam("id") Long id) {
        badLanguageService.deleteBadLanguage(id);
        logger.info("Remove badLanguage: " + id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}