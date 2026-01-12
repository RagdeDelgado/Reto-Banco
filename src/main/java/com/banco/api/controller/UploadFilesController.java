package com.banco.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.banco.api.dto.FileUploadRequest;
import com.banco.api.dto.LoadResultsDTO;
import com.banco.api.service.LoadFilesService;

import java.io.InputStream;
import java.nio.file.Files;
//import java.nio.file.Path;
import java.util.Map;

@Path("/files")
@Produces(MediaType.APPLICATION_JSON)
public class UploadFilesController {
	
	@Inject
    private LoadFilesService service;
	
	private static final String UPLOAD_DIR = "/tmp/uploads";

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(
        summary = "Subir archivo",
        description = "Permite subir un archivo usando multipart/form-data",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                mediaType = MediaType.MULTIPART_FORM_DATA,
                schema = @Schema(implementation = FileUploadRequest.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Archivo subido correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno")
        }
    )
    public Response uploadFile(@MultipartForm MultipartFormDataInput input) {
    	
    	LoadResultsDTO dto = service.procesarArchivo(input);
    	
    	return Response.ok(Map.of(
                "fileName", "",
                "path", "",
                "status", "OK"
        )).build();
        
    }

}
