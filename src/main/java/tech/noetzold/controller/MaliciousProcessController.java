package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import org.jboss.logging.Logger;
import tech.noetzold.model.MaliciousProcess;
import tech.noetzold.service.MaliciousProcessService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collection;

@Path("/process")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaliciousProcessController {
    @Inject
    MaliciousProcessService maliciousProcessService;

    private static final Logger logger = Logger.getLogger(MaliciousProcessController.class);

    @GET
    @RolesAllowed("admin")
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        Collection<MaliciousProcess> maliciousProcesses = maliciousProcessService.findAllMaliciousProcess(page, size, sortBy);
        if (maliciousProcesses.isEmpty()) {
            logger.error("There is no maliciousProcess.");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        logger.info("MaliciousProcess returned quantity: " + maliciousProcesses.size());
        return Response.ok(maliciousProcesses).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getMaliciousProcessById(@PathParam("id") long id) {
        if (id <= 0) {
            logger.error("Invalid id: " + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        MaliciousProcess maliciousProcess = maliciousProcessService.findMaliciousProcessById(id);
        if (maliciousProcess == null) {
            logger.error("There is no maliciousProcess with id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        logger.info("MaliciousProcess returned: " + maliciousProcess.getNameExe());
        return Response.ok(maliciousProcess).build();
    }

    @POST
    @RolesAllowed("admin")
    public Response save(MaliciousProcess maliciousProcess) {
        if (maliciousProcess == null) {
            logger.error("Invalid MaliciousProcess: " + maliciousProcess);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        MaliciousProcess existingMaliciousProcess = maliciousProcessService.findMaliciousProcessByNameExe(maliciousProcess.getNameExe());

        if (existingMaliciousProcess != null) {
            logger.info("Create MaliciousProcess: " + maliciousProcess.getNameExe());
            return Response.ok(existingMaliciousProcess).status(Response.Status.CREATED).build();
        }

        maliciousProcess = maliciousProcessService.saveMaliciousProcess(maliciousProcess);
        logger.info("Create MaliciousProcess: " + maliciousProcess.getNameExe());
        return Response.ok(maliciousProcess).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("id") Long id) {
        maliciousProcessService.deleteProcessById(id);
        logger.info("Remove MaliciousProcess: " + id);
        return Response.status(Response.Status.ACCEPTED).build();
    }
}
