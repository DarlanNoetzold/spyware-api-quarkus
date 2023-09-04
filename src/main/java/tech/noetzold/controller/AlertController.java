package tech.noetzold.controller;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import tech.noetzold.model.Alert;
import tech.noetzold.model.Image;
import tech.noetzold.service.AlertService;

import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tech.noetzold.service.ImageService;

import java.util.Base64;
import java.util.List;

@Path("/alert")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertController {

    @Inject
    AlertService alertService;

    @Inject
    ImageService imageService;


    @Channel("alerts")
    Emitter<Alert> quoteRequestEmitter;

    private static final Logger logger = Logger.getLogger(AlertController.class);

    @GET
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        if (page <= 0 || size <= 0) {
            logger.error("Invalid page: " + page +" or size: "+ size);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Alert> alerts = alertService.findAll(page, size,sortBy);
        logger.info("Returned alerts quantity: " + alerts.size());
        return Response.ok(alerts).build();
    }

    @GET
    @Path("/{id}")
    public Response getAlertId(@PathParam("id") Long id) {
        if (id <= 0) {
            logger.error("Invalid id" + id);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Alert alert = alertService.findAlertaById(id);
        if (alert == null) {
            logger.error("There is no alert with id: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        logger.info("Returned alert: " + alert);
        return Response.ok(alert).build();
    }

    @GET
    @Path("/pcId/{pcId}")
    public Response getAlertPcId(@PathParam("pcId") String pcId) {
        if (pcId == null || pcId.isEmpty()) {
            logger.error("Error to get alert by pcId.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Alert> alerts = alertService.findAlertaByPcId(pcId);
        if (alerts == null || alerts.isEmpty()) {
            logger.error("There is no alert with pcId: " + pcId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        logger.info("Returned alerts quantity: " + alerts.size());
        return Response.ok(alerts).build();
    }

    @POST
    public Response save(Alert alert) {
        if (alert == null || alert.getImage() == null || alert.getImage().getId() == null) {
            logger.error("Error to save alert.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Image optionalImage = imageService.findImageById(alert.getImage().getId());
        if (optionalImage == null) {
            logger.error("Invalid image.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        alert.setImage(optionalImage);

        quoteRequestEmitter.send(alert);
        logger.info("Save Alert message sended.");
        alert.getImage().setBase64Img(Base64.getDecoder().decode(""));

        return Response.status(Response.Status.CREATED).entity(alert).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") Long id) {
        alertService.deleteAlertaById(id);
        logger.info("Remove alert: " + id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
