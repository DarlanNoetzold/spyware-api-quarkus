package tech.noetzold.controller;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.Alert;
import tech.noetzold.model.Image;
import tech.noetzold.repository.ImageRepository;
import tech.noetzold.service.AlertService;

import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Base64;
import java.util.List;

@Path("/alert")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertController {

    @Inject
    AlertService alertService;

    @Channel("alerts")
    Emitter<Alert> quoteRequestEmitter;

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    @GET
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        if (page <= 0 || size <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Alert> alerts = alertService.findAll(page, size,sortBy);
        return Response.ok(alerts).build();
    }

    @GET
    @Path("/{id}")
    public Response getAlertId(@PathParam("id") Long id) {
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Alert alert = alertService.findAlertaById(id);
        if (alert == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(alert).build();
    }

    @GET
    @Path("/pcId/{pcId}")
    public Response getAlertPcId(@PathParam("pcId") String pcId) {
        if (pcId == null || pcId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Alert> alerts = alertService.findAlertaByPcId(pcId);
        if (alerts == null || alerts.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(alerts).build();
    }

    @POST
    @Path("/save")
    public Response save(Alert alert) {
        if (alert == null || alert.getImage() == null || alert.getImage().getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        quoteRequestEmitter.send(alert);

        alert.getImage().setBase64Img(Base64.getDecoder().decode(""));

        return Response.status(Response.Status.CREATED).entity(alert).build();
    }

    @DELETE
    @Path("/remove/{id}")
    public Response remover(@PathParam("id") Long id) {
        alertService.deleteAlertaById(id);
        logger.info("Remove alert: " + id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
