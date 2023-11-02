package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import tech.noetzold.model.BadLanguage;
import tech.noetzold.service.BadLanguageService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

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
            logger.info("The badLanguage: " + badLanguage.getWord() + "already exists.");
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
        return Response.status(Response.Status.ACCEPTED).entity(new BadLanguage(0L, "BadLanguage removed")).build();
    }

    @POST
    @Path("/upload")
    @RolesAllowed("admin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadBadLanguageFile(@MultipartForm MultipartFormDataInput input) {
        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");

            for (InputPart inputPart : inputParts) {
                String content = inputPart.getBody(String.class, null);
                String[] lines = content.split(",");

                for (String word : lines) {
                    if (!word.isBlank()) {

                        BadLanguage badLanguage = new BadLanguage();
                        badLanguage.setWord(word);

                        BadLanguage existingBadLanguage = badLanguageService.findBadLanguageByWord(badLanguage.getWord());

                        if (existingBadLanguage != null) {
                            logger.info("The badLanguage: " + badLanguage.getWord() + " already exists.");
                        }

                        badLanguageService.saveBadLanguage(badLanguage);
                        logger.info("Create badLanguage: " + badLanguage.getWord());
                    }
                }
            }
            logger.info("BadLanguages saved by file");
            return Response.ok("BadLanguage entries added from file").build();
        } catch (Exception e) {
            logger.error("Error to save badLanguages by file.");
            return Response.serverError().entity("Error uploading and processing file").build();
        }
    }

}
