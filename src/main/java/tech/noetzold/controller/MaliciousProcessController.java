package tech.noetzold.controller;

import jakarta.annotation.security.RolesAllowed;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import tech.noetzold.model.MaliciousProcess;
import tech.noetzold.model.MaliciousProcess;
import tech.noetzold.service.MaliciousProcessService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response update(@PathParam("id") long id, MaliciousProcess updatedMaliciousProcess) {
        if (id <= 0 || updatedMaliciousProcess == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        MaliciousProcess existingMaliciousProcess = maliciousProcessService.findMaliciousProcessById(id);
        if (existingMaliciousProcess == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existingMaliciousProcess.setNameExe(updatedMaliciousProcess.getNameExe());
        MaliciousProcess updated = maliciousProcessService.updateMaliciousProcess(existingMaliciousProcess);

        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response remove(@PathParam("id") Long id) {
        maliciousProcessService.deleteProcessById(id);
        logger.info("Remove MaliciousProcess: " + id);
        return Response.status(Response.Status.ACCEPTED).entity(new MaliciousProcess(0L, "MaliciousProcess removed")).build();
    }

    @POST
    @Path("/upload")
    @RolesAllowed("admin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadMaliciousProcessFile(@MultipartForm MultipartFormDataInput input) {
        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");

            for (InputPart inputPart : inputParts) {
                String content = inputPart.getBody(String.class, null);
                String[] lines = content.split(",");

                for (String nameExe : lines) {
                    if (!nameExe.isBlank()) {

                        MaliciousProcess maliciousProcess = new MaliciousProcess();
                        maliciousProcess.setNameExe(nameExe);

                        MaliciousProcess existingMaliciousProcess = maliciousProcessService.findMaliciousProcessByNameExe(maliciousProcess.getNameExe());

                        if (existingMaliciousProcess != null) {
                            logger.info("The maliciousProcess: " + maliciousProcess.getNameExe() + " already exists.");
                            return Response.ok("MaliciousProcess entries added from file").build();
                        }

                        maliciousProcessService.saveMaliciousProcess(maliciousProcess);
                        logger.info("Create maliciousProcess: " + maliciousProcess.getNameExe());
                    }
                }
            }
            logger.info("MaliciousProcesss saved by file");
            return Response.ok("MaliciousProcess entries added from file").build();
        } catch (Exception e) {
            logger.error("Error to save maliciousProcesss by file.");
            return Response.serverError().entity("Error uploading and processing file").build();
        }
    }
}
