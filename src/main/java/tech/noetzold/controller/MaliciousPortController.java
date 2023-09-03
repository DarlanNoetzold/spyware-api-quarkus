package tech.noetzold.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.MaliciousPort;
import tech.noetzold.service.MaliciousPortService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

    private static final Logger logger = LoggerFactory.getLogger(MaliciousPortController.class);

    @GET
    @Path("/getAll")
    @Transactional
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        Collection<MaliciousPort> maliciousPorts = maliciousPortService.findAllMaliciousPort(page, size, sortBy);
        if (maliciousPorts.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(maliciousPorts).build();
    }

    @GET
    @Path("/get/{id}")
    @Transactional
    public Response getMaliciousPortById(@PathParam("id") long id) {
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        MaliciousPort maliciousPort = maliciousPortService.findMaliciousPortById(id);
        if (maliciousPort == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(maliciousPort).build();
    }

    @POST
    @Path("/save")
    public Response save(MaliciousPort maliciousPort) {
        if (maliciousPort == null || maliciousPort.getVulnarableBanners() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        MaliciousPort existingMaliciousPort = maliciousPortService.findMaliciousPortByVulnarableBanners(maliciousPort.getVulnarableBanners());

        if (existingMaliciousPort != null) {
            return Response.ok(existingMaliciousPort).status(Response.Status.CREATED).build();
        }

        try {
            maliciousPort = maliciousPortService.saveMaliciousPort(maliciousPort);
            logger.info("Create MaliciousPort: " + maliciousPort.getVulnarableBanners());
            return Response.ok(maliciousPort).status(Response.Status.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(maliciousPort).build();
    }

    @DELETE
    @Path("/remove/{id}")
    public Response remover(@PathParam("id") Long id) {
        logger.info("Remove MaliciousPort: " + id);
        maliciousPortService.deleteMaliciousPortById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}