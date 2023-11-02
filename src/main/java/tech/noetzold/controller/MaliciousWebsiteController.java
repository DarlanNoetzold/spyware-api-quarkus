package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import tech.noetzold.model.MaliciousWebsite;
import tech.noetzold.model.MaliciousWebsite;
import tech.noetzold.service.MaliciousWebsiteService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Path("/website")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaliciousWebsiteController {
    @Inject
    MaliciousWebsiteService maliciousWebsiteService;

    private static final Logger logger = Logger.getLogger(MaliciousWebsiteController.class);

    @GET
    @RolesAllowed("admin")
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        Collection<MaliciousWebsite> maliciousWebsites = maliciousWebsiteService.findAllMaliciousWebsite(page, size, sortBy);
        if (maliciousWebsites.isEmpty()) {
            logger.error("There is no maliciousWebsite.");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        logger.info("Returned maliciousWebsite quantity: " + maliciousWebsites.size());
        return Response.ok(maliciousWebsites).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getMaliciousWebsiteById(@PathParam("id") long id) {
        if (id <= 0) {
            logger.error("Invalid id: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        MaliciousWebsite maliciousWebsite = maliciousWebsiteService.findMaliciousWebsiteById(id);
        if (maliciousWebsite == null) {
            logger.error("There is no maliciousWebsite with id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        logger.info("Returned maliciousWebsite: " + maliciousWebsite.getUrl());
        return Response.ok(maliciousWebsite).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response save(MaliciousWebsite maliciousWebsite) {
        if (maliciousWebsite == null) {
            logger.error("Invalid MaliciousWebsite.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        MaliciousWebsite existingMaliciousWebsite = maliciousWebsiteService.findMaliciousWebsiteByUrl(maliciousWebsite.getUrl());

        if (existingMaliciousWebsite != null) {
            logger.info("Create MaliciousWebsite: " + maliciousWebsite.getUrl());
            return Response.ok(existingMaliciousWebsite).status(Response.Status.CREATED).build();
        }

        try {
            maliciousWebsite = maliciousWebsiteService.saveMaliciousWebsite(maliciousWebsite);
            logger.info("Create MaliciousWebsite: " + maliciousWebsite.getUrl());
            return Response.ok(maliciousWebsite).status(Response.Status.CREATED).build();
        } catch (Exception e) {
            logger.error("Error to save MaliciousWebsite");
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response update(@PathParam("id") long id, MaliciousWebsite updatedMaliciousWebsite) {
        if (id <= 0 || updatedMaliciousWebsite == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        MaliciousWebsite existingMaliciousWebsite = maliciousWebsiteService.findMaliciousWebsiteById(id);
        if (updatedMaliciousWebsite == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existingMaliciousWebsite.setUrl(updatedMaliciousWebsite.getUrl());
        MaliciousWebsite updated = maliciousWebsiteService.updateMaliciousWebsite(existingMaliciousWebsite);

        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("id") Long id) {
        maliciousWebsiteService.deleteWebsiteById(id);
        logger.info("Remove MaliciousWebsite: " + id);
        return Response.status(Response.Status.ACCEPTED).entity(new MaliciousWebsite(0L ,"MaliciousWebsite removed")).build();
    }

    @POST
    @Path("/upload")
    @RolesAllowed("admin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadMaliciousWebsiteFile(@MultipartForm MultipartFormDataInput input) {
        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");

            for (InputPart inputPart : inputParts) {
                String content = inputPart.getBody(String.class, null);
                String[] lines = content.split(",");

                for (String webSite : lines) {
                    if (!webSite.isBlank()) {

                        MaliciousWebsite maliciousWebsite = new MaliciousWebsite();
                        maliciousWebsite.setUrl(webSite);

                        MaliciousWebsite existingMaliciousWebsite = maliciousWebsiteService.findMaliciousWebsiteByUrl(maliciousWebsite.getUrl());

                        if (existingMaliciousWebsite != null) {
                            logger.info("The maliciousWebsite: " + maliciousWebsite.getUrl() + " already exists.");
                        }

                        maliciousWebsiteService.saveMaliciousWebsite(maliciousWebsite);
                        logger.info("Create maliciousWebsite: " + maliciousWebsite.getUrl());
                    }
                }
            }
            logger.info("MaliciousWebsites saved by file");
            return Response.ok("MaliciousWebsite entries added from file").build();
        } catch (Exception e) {
            logger.error("Error to save maliciousWebsites by file.");
            return Response.serverError().entity("Error uploading and processing file").build();
        }
    }
    
}

