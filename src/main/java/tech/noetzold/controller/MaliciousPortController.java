package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import tech.noetzold.model.MaliciousPort;
import tech.noetzold.model.MaliciousPort;
import tech.noetzold.service.MaliciousPortService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Path("/port")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaliciousPortController {

    @Inject
    MaliciousPortService maliciousPortService;

    private static final Logger logger = Logger.getLogger(MaliciousPortController.class);

    @GET
    @RolesAllowed("admin")
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        Collection<MaliciousPort> maliciousPorts = maliciousPortService.findAllMaliciousPort(page, size, sortBy);
        if (maliciousPorts.isEmpty()) {
            logger.error("There is no maliciousPort");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        logger.info("MaliciousPort returned quantity: " + maliciousPorts.size());
        return Response.ok(maliciousPorts).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getMaliciousPortById(@PathParam("id") long id) {
        if (id <= 0) {
            logger.error("Invalid id: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        MaliciousPort maliciousPort = maliciousPortService.findMaliciousPortById(id);
        if (maliciousPort == null) {
            logger.error("There is no maliciousPort with the id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        logger.info("MaliciousPort returned: " + id);
        return Response.ok(maliciousPort).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response update(@PathParam("id") long id, MaliciousPort updatedMaliciousPort) {
        if (id <= 0 || updatedMaliciousPort == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        MaliciousPort existingMaliciousPort = maliciousPortService.findMaliciousPortById(id);
        if (existingMaliciousPort == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existingMaliciousPort.setVulnarableBanners(updatedMaliciousPort.getVulnarableBanners());
        MaliciousPort updated = maliciousPortService.updateMaliciousPort(existingMaliciousPort);

        return Response.ok(updated).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response save(MaliciousPort maliciousPort) {
        if (maliciousPort == null || maliciousPort.getVulnarableBanners() == null) {
            logger.error("Invalid to save maliciousPort: " + maliciousPort);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        MaliciousPort existingMaliciousPort = maliciousPortService.findMaliciousPortByVulnarableBanners(maliciousPort.getVulnarableBanners());

        if (existingMaliciousPort != null) {
            logger.info("Create MaliciousPort: " + maliciousPort.getVulnarableBanners());
            return Response.ok(existingMaliciousPort).status(Response.Status.CREATED).build();
        }

        try {
            maliciousPort = maliciousPortService.saveMaliciousPort(maliciousPort);
            logger.info("Create MaliciousPort: " + maliciousPort.getVulnarableBanners());
            return Response.ok(maliciousPort).status(Response.Status.CREATED).build();
        } catch (Exception e) {
            logger.error("Error to save maliciousPort: " + maliciousPort.getVulnarableBanners());
            e.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(maliciousPort).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("id") Long id) {
        logger.info("Remove MaliciousPort: " + id);
        maliciousPortService.deleteMaliciousPortById(id);
        return Response.status(Response.Status.ACCEPTED).entity(new MaliciousPort(0L, "MaliciousPort removed")).build();
    }

    @POST
    @Path("/upload")
    @RolesAllowed("admin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadMaliciousPortFile(@MultipartForm MultipartFormDataInput input) {
        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");

            for (InputPart inputPart : inputParts) {
                String content = inputPart.getBody(String.class, null);
                String[] lines = content.split(",");

                for (String vulnarableBanners : lines) {
                    if (!vulnarableBanners.isBlank()) {

                        MaliciousPort maliciousPort = new MaliciousPort();
                        maliciousPort.setVulnarableBanners(vulnarableBanners);

                        MaliciousPort existingMaliciousPort = maliciousPortService.findMaliciousPortByVulnarableBanners(maliciousPort.getVulnarableBanners());

                        if (existingMaliciousPort != null) {
                            logger.info("The maliciousPort: " + maliciousPort.getVulnarableBanners() + " already exists.");
                            return Response.ok("MaliciousProcess entries added from file").build();
                        }

                        maliciousPortService.saveMaliciousPort(maliciousPort);
                        logger.info("Create maliciousPort: " + maliciousPort.getVulnarableBanners());
                    }
                }
            }
            logger.info("MaliciousPorts saved by file");
            return Response.ok("MaliciousPort entries added from file").build();
        } catch (Exception e) {
            logger.error("Error to save maliciousPorts by file.");
            return Response.serverError().entity("Error uploading and processing file").build();
        }
    }
    
}