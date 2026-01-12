package com.banco.api.service;

import com.banco.api.dto.ClientsDTO;
import com.banco.api.dto.LoadResultsDTO;
import com.banco.api.dto.DatabookDTO;
import com.banco.api.entity.ClientsEntity;
import com.banco.api.entity.AccountsEntity;

//import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.banco.api.util.Log;

//@Slf4j

public class LoadFilesService {
	private final ClientsService clientsService = new ClientsService();
    private final AccountsService accountsService = new AccountsService();
    private final DatabookService databookService = new DatabookService();
    private final ValidationsService validationsService = new ValidationsService();

    public LoadResultsDTO procesarArchivo(InputStream archivo) {        
        Log.info(LoadFilesService.class,"Iniciando procesamiento de archivo");
                
        LoadResultsDTO resultado = LoadResultsDTO.builder()
            .clientesCargados(new ArrayList<>())
            .errores(new ArrayList<>())
            .clientesNoEncontrados(new ArrayList<>())
            .build();
        
        List<Map<String, String>> registros = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(archivo))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] campos = linea.split(",");
                
                if (campos.length != 6) {
                    resultado.getErrores().add(
                        "Línea " + numeroLinea + ": Número de campos incorrecto. " +
                        "Se esperaban 6 campos, se encontraron " + campos.length);
                    continue;
                }

                Map<String, String> registro = new HashMap<>();
                registro.put("tipoId", campos[0].trim());
                registro.put("numeroId", campos[1].trim());
                registro.put("fechaIngreso", campos[2].trim());
                registro.put("valorNomina", campos[3].trim());
                registro.put("correo", campos[4].trim());
                registro.put("celular", campos[5].trim());
                registro.put("numeroLinea", String.valueOf(numeroLinea));

                registros.add(registro);
            }
            
            Log.info(LoadFilesService.class,"Total de registros a procesar: {}", registros.size());
            
            // Procesar registros validados
            for (Map<String, String> registro : registros) {
                procesarRegistro(registro, resultado);
            }

        } catch (IOException e) {
            resultado.getErrores().add("Error al leer el archivo: " + e.getMessage());            
            Log.info(LoadFilesService.class,"Error al procesar archivo", e);
        }

        resultado.setTotalProcesados(registros.size());
        resultado.setTotalExitosos(resultado.getClientesCargados().size());
        resultado.setTotalErrores(resultado.getErrores().size() + 
                                 resultado.getClientesNoEncontrados().size());

        	Log.info(LoadFilesService.class,"Procesamiento completado: {} procesados, {} exitosos, {} errores", 		
            resultado.getTotalProcesados(),
            resultado.getTotalExitosos(),
            resultado.getTotalErrores());

        return resultado;
    }

    private void procesarRegistro(Map<String, String> registro, LoadResultsDTO resultado) {
        String tipoId = registro.get("tipoId");
        String numeroId = registro.get("numeroId");
        String numLinea = registro.get("numeroLinea");

        // Validaciones
        ValidationsService.ResultadoValidacion validacion;

        validacion = validationsService.validarTipoIdentificacion(tipoId);
        if (!validacion.isValido()) {
            resultado.getErrores().add("Línea " + numLinea + ": " + validacion.getMensaje());
            return;
        }

        validacion = validationsService.validarNumeroIdentificacion(numeroId);
        if (!validacion.isValido()) {
            resultado.getErrores().add("Línea " + numLinea + ": " + validacion.getMensaje());
            return;
        }

        validacion = validationsService.validarFechaIngreso(registro.get("fechaIngreso"));
        if (!validacion.isValido()) {
            resultado.getErrores().add("Línea " + numLinea + ": " + validacion.getMensaje());
            return;
        }

        validacion = validationsService.validarValorNomina(registro.get("valorNomina"));
        if (!validacion.isValido()) {
            resultado.getErrores().add("Línea " + numLinea + ": " + validacion.getMensaje());
            return;
        }

        validacion = validationsService.validarCorreo(registro.get("correo"));
        if (!validacion.isValido()) {
            resultado.getErrores().add("Línea " + numLinea + ": " + validacion.getMensaje());
            return;
        }

        validacion = validationsService.validarCelular(registro.get("celular"));
        if (!validacion.isValido()) {
            resultado.getErrores().add("Línea " + numLinea + ": " + validacion.getMensaje());
            return;
        }

        // Verificar duplicados
        if (clientsService.existeNumerIdentificacion(numeroId)) {
            resultado.getErrores().add("Línea " + numLinea + ": Número de identificación " +
                    numeroId + " ya existe en la base de datos");
            return;
        }

        if (clientsService.existeCorreo(registro.get("correo"))) {
            resultado.getErrores().add("Línea " + numLinea + ": Correo " + 
                    registro.get("correo") + " ya existe en la base de datos");
            return;
        }

        // Consultar Databook
        DatabookDTO datosDatabook = databookService.obtenerDatos(tipoId, numeroId);
        if (datosDatabook == null) {
            resultado.getClientesNoEncontrados().add(
                "Tipo: " + tipoId + ", Número: " + numeroId + " (Línea " + numLinea + ")");
            return;
        }

        // Crear cliente
//        try {
//        	ClientsEntity clientsEntity = ClientsEntity.builder()
//                .codigoCliente(generarCodigoCliente())
//                .tipoIdentificacion(Cliente.TipoIdentificacion.valueOf(tipoId))
//                .numeroIdentificacion(numeroId)
//                .primerNombre(datosDatabook.getPrimerNombre())
//                .segundoNombre(datosDatabook.getSegundoNombre())
//                .primerApellido(datosDatabook.getPrimerApellido())
//                .segundoApellido(datosDatabook.getSegundoApellido())
//                .fechaNacimiento(LocalDate.parse(datosDatabook.getFechaNacimiento(),
//                        DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//                .fechaIngreso(LocalDate.parse(registro.get("fechaIngreso"),
//                        DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//                .correo(registro.get("correo"))
//                .celular(registro.get("celular"))
//                .build();
//
//            cliente = clienteService.crearCliente(cliente);
//
//            // Crear cuenta
//            AccountsEntity accountsEntity = AccountsEntity.builder()
//                .numeroCuenta(AccountsService.generarNumeroCuenta())
//                .clientsEntity(clientsEntity)
//                .valorNomina(new BigDecimal(registro.get("valorNomina")))
//                .build();
//
//            AccountsService.crearCuenta(cuenta);
//
//            // Agregar a resultados exitosos
//            ClientsDTO clientsDTO = ClientsDTO.builder()
//                .codigoCliente(clientsEntity.getCodigoCliente())
//                .numeroIdentificacion(numeroId)
//                .numeroCuenta(accountsEntity.getNumeroCuenta())
//                .build();
//            
//            resultado.getClientesCargados().add(clientsDTO);
//            
//            Log.info(LoadFilesService.class,"Cliente procesado exitosamente: {}", numeroId);
//
//        } catch (Exception e) {
//            resultado.getErrores().add("Línea " + numLinea + ": Error al crear cliente: " + 
//                    e.getMessage());            
//            Log.info(LoadFilesService.class,"Error procesando registro en línea" + numLinea, e);
//        }
    }

    private String generarCodigoCliente() {
        return "CLI-" + System.currentTimeMillis();
    }

}
