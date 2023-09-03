package tech.noetzold.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.model.Image;
import tech.noetzold.service.ImageService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Base64;
import java.util.Collection;

@Path("/image")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImageController {
    @Inject
    ImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @GET
    @Path("/getAll")
    public Response getAll(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("sortBy") String sortBy) {
        Collection<Image> images = imageService.findAllImages(page, size, sortBy);
        if (images.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(images).build();
    }

    @GET
    @Path("/get/{id}")
    public Response getImagemById(@PathParam("id") long id) {
        try {
            if (id <= 0)
                return Response.status(Response.Status.BAD_REQUEST).build();
            Image image = imageService.findImageById(id);
            if (image == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(image).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/save")
    public Response save(Image image) {
        try {
            image = imageService.saveImage(image);
            if (image.getBase64Img() == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            logger.info("Create " + image.getId());
            image.setBase64Img(Base64.getDecoder().decode(""));
            return Response.status(Response.Status.CREATED).entity(image).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(image).build();
    }
}
