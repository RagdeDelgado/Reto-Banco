package com.banco.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.ws.rs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import jakarta.ws.rs.core.MediaType;
import java.io.InputStream;

@Schema(name = "FileUploadRequest")
public class FileUploadRequest {

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema(type = "string", format = "binary", description = "Archivo a subir")
    public InputStream file;

}
