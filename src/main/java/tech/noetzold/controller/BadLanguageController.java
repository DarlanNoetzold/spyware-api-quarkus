package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import org.jboss.logging.Logger;
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

    private static final Logger logger = Logger.getLogger(BadLanguageController.class);

    @GET
    @RolesAllowed("admin")
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        Collection<BadLanguage> badLanguages = badLanguageService.findAllBadLanguage(page, size, sortBy);
        if (badLanguages.isEmpty()) {
            logger.error("There is no badLanguage.");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        logger.info("BadLanguage returned quantity: " + badLanguages.size());
        return Response.ok(badLanguages).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getBadLanguageById(@PathParam("id") long id) {
        if (id <= 0) {
            logger.error("Invalid id: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        BadLanguage badLanguage = badLanguageService.findBadLanguageById(id);
        if (badLanguage == null) {
            logger.error("There is no badLanguage with id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        logger.info("BadLanguage returned: " + badLanguage.getWord());
        return Response.ok(badLanguage).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response save(BadLanguage badLanguage) {
        if (badLanguage == null) {
            logger.error("Error to save badlanguage");
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

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response update(@PathParam("id") long id, BadLanguage updatedBadLanguage) {
        if (id <= 0 || updatedBadLanguage == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        BadLanguage existingBadLanguage = badLanguageService.findBadLanguageById(id);
        if (existingBadLanguage == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existingBadLanguage.setWord(updatedBadLanguage.getWord());

        BadLanguage updated = badLanguageService.updateBadLanguage(existingBadLanguage);

        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("id") Long id) {
        badLanguageService.deleteBadLanguage(id);
        logger.info("Remove badLanguage: " + id);
        return Response.status(Response.Status.ACCEPTED).build();
    }
}
