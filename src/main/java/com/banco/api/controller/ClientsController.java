package com.banco.api.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import com.banco.api.entity.ClientsEntity;
import com.banco.api.service.ClientsService;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientsController {
	
	@Inject
    private ClientsService service;

    @GET
    public List<ClientsEntity> getAll() {
        return service.findAll();
    }

    @POST
    public ClientsEntity create(ClientsEntity cliente) {
        return service.create(cliente);
    }

    @GET
    @Path("/{id}")
    public ClientsEntity get(@PathParam("id") Long id) {
        return service.find(id);
    }

    @PUT
    @Path("/{id}")
    public ClientsEntity update(@PathParam("id") Long id, ClientsEntity cliente) {
        cliente.setIdCliente(id);
        return service.update(cliente);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }

}
