package com.banco.api.rest;

//import com.banco.api.dto.LoadResultsDTO;
//import com.banco.api.service.LoadFilesService;
//import com.banco.api.util.Log;
//
//import lombok.extern.slf4j.Slf4j;
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//import org.glassfish.jersey.media.multipart.FormDataParam;
//
//import java.io.InputStream;
//
////@Slf4j
//@Path("/cargas")
//@Consumes(MediaType.MULTIPART_FORM_DATA)
//@Produces(MediaType.APPLICATION_JSON)

public class LoadClientsRest {

//	private final LoadFilesService cargaArchivoService = new LoadFilesService();
//
//    @POST
//    @Path("/procesar")
//    public Response procesarCarga(
//            @FormDataParam("archivo") InputStream archivo,
//            @FormDataParam("archivo") FormDataContentDisposition fileDetail) {
//                       
//        Log.info(LoadFilesService.class,"Recibida solicitud de carga de archivo: {}",
//        	fileDetail != null ? fileDetail.getFileName() : "sin nombre");
//        
//        
//        if (archivo == null) {            
//            Log.warn(LoadFilesService.class,"No se envió archivo en la solicitud");
//            return Response.status(Response.Status.BAD_REQUEST)
//                .entity("{\"error\": \"No se envió archivo\"}")
//                .build();
//        }
//
//        try {
//        	LoadResultsDTO resultado = cargaArchivoService.procesarArchivo(archivo);
//                        
//            Log.info(LoadFilesService.class,"Procesamiento exitoso: {} exitosos, {} errores",              		
//                resultado.getTotalExitosos(),
//                resultado.getTotalErrores());
//            
//            return Response.ok(resultado).build();
//            
//        } catch (Exception e) {            
//            Log.error(LoadFilesService.class,"Error durante el procesamiento del archivo", e);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                .entity("{\"error\": \"" + e.getMessage() + "\"}")
//                .build();
//        }
//    }
//
//    @GET
//    @Path("/health")
//    public Response health() {
//        //log.debug("Health check solicitado");
//    	Log.info(LoadFilesService.class,"Health check solicitado");
//        return Response.ok("{\"status\": \"OK\"}").build();
//    }
}
