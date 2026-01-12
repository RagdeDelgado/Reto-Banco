package com.banco.api.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import com.banco.api.entity.AccountsEntity;
import com.banco.api.service.AccountsService;

@Path("/cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class AccountsController {
	@Inject
    private AccountsService service;

    @GET
    public List<AccountsEntity> getAll() {
        return service.findAll();
    }

    @POST
    public AccountsEntity create(AccountsEntity cuenta) {
        return service.create(cuenta);
    }

    @GET
    @Path("/{id}")
    public AccountsEntity get(@PathParam("id") Long id) {
        return service.find(id);
    }

    @PUT
    @Path("/{id}")
    public AccountsEntity update(@PathParam("id") Long id, AccountsEntity cuenta) {
        cuenta.setIdCuenta(id);
        return service.update(cuenta);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }

}



