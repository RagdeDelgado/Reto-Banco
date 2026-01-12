package com.banco.api.service;

import com.banco.api.dto.DatabookDTO;
//import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import com.banco.api.util.Log;

import jakarta.ejb.Stateless;

//@Slf4j

//@Stateless
public class DatabookService {
	private static final Map<String, DatabookDTO> databookData = new HashMap<>();

    static {
        // Inicializar datos dummy estáticos
        databookData.put("C1725364578", 
            DatabookDTO.builder()
                .primerNombre("Jaime")
                .segundoNombre("Fernando")
                .primerApellido("García")
                .segundoApellido("López")
                .fechaNacimiento("1990-03-15")
                .build());
        
        databookData.put("PA123", 
            DatabookDTO.builder()
                .primerNombre("José")
                .segundoNombre("Luis")
                .primerApellido("Martínez")
                .segundoApellido("González")
                .fechaNacimiento("1985-07-22")
                .build());
        
        databookData.put("C1234567890", 
            DatabookDTO.builder()
                .primerNombre("María")
                .segundoNombre("José")
                .primerApellido("Rodríguez")
                .segundoApellido("Pérez")
                .fechaNacimiento("1992-11-08")
                .build());
        
        databookData.put("P9876543210", 
            DatabookDTO.builder()
                .primerNombre("Carlos")
                .segundoNombre("Alberto")
                .primerApellido("López")
                .segundoApellido("Sánchez")
                .fechaNacimiento("1988-05-14")
                .build());
        
        databookData.put("C1111111111", 
            DatabookDTO.builder()
                .primerNombre("Ana")
                .segundoNombre("María")
                .primerApellido("Flores")
                .segundoApellido("Jiménez")
                .fechaNacimiento("1995-01-30")
                .build());
                
        Log.info(DatabookService.class,"Databook service initialized with {} records", databookData.size());
    }

    public DatabookDTO obtenerDatos(String tipoIdentificacion, String numeroIdentificacion) {
        String clave = tipoIdentificacion + numeroIdentificacion;
        return databookData.getOrDefault(clave, null);
    }
}
