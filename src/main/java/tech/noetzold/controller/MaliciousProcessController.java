package tech.noetzold.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(MaliciousProcessController.class);

    @GET
    @Path("/getAll")
    @Transactional
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        Collection<MaliciousProcess> maliciousProcesses = maliciousProcessService.findAllMaliciousProcess(page, size, sortBy);
        if (maliciousProcesses.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(maliciousProcesses).build();
    }

    @GET
    @Path("/get/{id}")
    @Transactional
    public Response getMaliciousProcessById(@PathParam("id") long id) {
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        MaliciousProcess maliciousProcess = maliciousProcessService.findMaliciousProcessById(id);
        if (maliciousProcess == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(maliciousProcess).build();
    }

    @POST
    @Path("/save")
    public Response save(MaliciousProcess maliciousProcess) {
        if (maliciousProcess == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        MaliciousProcess existingMaliciousProcess = maliciousProcessService.findMaliciousProcessByNameExe(maliciousProcess.getNameExe());

        if (existingMaliciousProcess != null) {
            return Response.ok(existingMaliciousProcess).status(Response.Status.CREATED).build();
        }

        maliciousProcess = maliciousProcessService.saveMaliciousProcess(maliciousProcess);
        logger.info("Create MaliciousProcess: " + maliciousProcess.getNameExe());
        return Response.ok(maliciousProcess).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/remove/{id}")
    public Response remover(@PathParam("id") Long id) {
        maliciousProcessService.deleteProcessById(id);
        logger.info("Remove MaliciousProcess: " + id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
