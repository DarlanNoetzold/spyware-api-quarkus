package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import org.jboss.logging.Logger;
import tech.noetzold.model.MaliciousPort;
import tech.noetzold.service.MaliciousPortService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collection;

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
        return Response.status(Response.Status.ACCEPTED).build();
    }
}