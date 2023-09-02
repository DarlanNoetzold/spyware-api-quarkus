package tech.noetzold.controller;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import tech.noetzold.model.User;
import tech.noetzold.service.UserService;

import java.util.List;

@Path("/user")
public class UserController {

    @Inject
    private UserService userService;


    @GET
    @Path("/listAll")
    @RolesAllowed("admin")
    public List<User> listarTodos() {
        return userService.findAllUsuarios();
    }

    @POST
    @Path("/save")
    @RolesAllowed("admin")
    public User salvar(User user) {
        return userService.saveUsuario(user);
    }


}
