package com.banco.api.service;

import com.banco.api.dto.ClientsDTO;
import com.banco.api.dto.LoadResultsDTO;
import com.banco.api.dto.DatabookDTO;
import com.banco.api.entity.ClientsEntity;
import com.banco.api.enums.EstadoCliente;
import com.banco.api.enums.TipoIdentificacion;
import com.banco.api.entity.AccountsEntity;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import java.io.InputStream;

//import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.banco.api.util.Log;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;

//@Slf4j

@Stateless
public class LoadFilesService {

	@Inject
	private ClientsService clientsService;

	@Inject
	private AccountsService accountsService;

	//	private final ClientsService clientsService = new ClientsService();
	//    private final AccountsService accountsService = new AccountsService();
	private final DatabookService databookService = new DatabookService();
	private final ValidationsService validationsService = new ValidationsService();

	public LoadResultsDTO procesarArchivo(MultipartFormDataInput archivoMP) {
		//    	InputStream archivo = saveTMPFile(archivoMP);
		Log.info(LoadFilesService.class,"Iniciando procesamiento de archivo");

		//        if(archivo == null) {
		//        	return null;
		//        }

		LoadResultsDTO resultado = LoadResultsDTO.builder()
				.clientesCargados(new ArrayList<>())
				.errores(new ArrayList<>())
				.clientesNoEncontrados(new ArrayList<>())
				.build();

		List<Map<String, String>> registros = new ArrayList<>();

		try {
			Map<String, List<InputPart>> formParts = archivoMP.getFormDataMap();

			InputPart filePart = formParts.get("file").get(0);
			InputStream inputStream = filePart.getBody(InputStream.class, null);

			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

				String line = "";

//				while ((line = reader.readLine()) != null) {
					//                    if (line.isBlank()) {
					//                        continue;
					//                    }
					//
					//                    String[] values = line.split(",");
					//
					//                    Map<String, String> registro = new HashMap<>();
					//                    registro.put("tipo", values[0].trim());
					//                    registro.put("numero", values[1].trim());
					//                    registro.put("fecha", values[2].trim());
					//                    registro.put("monto", values[3].trim());
					//                    registro.put("email", values[4].trim());
					//                    registro.put("telefono", values[5].trim());
					//
					//                    registros.add(registro);

					String linea = "";
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
				}

			} catch (Exception e) {
				throw new RuntimeException("Error al procesar archivo TXT", e);
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
			if (clientsService.existeNumeroIdentificacion(numeroId)) {
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
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
				Date fechaNac = sdf.parse(datosDatabook.getFechaNacimiento());
				ClientsEntity clientsEntity = ClientsEntity.builder()
						.codigoCliente(generarCodigoCliente())
						.tipoIdentificacion(TipoIdentificacion.valueOf(tipoId))
						.numeroIdentificacion(numeroId)
						.primerNombre(datosDatabook.getPrimerNombre())
						.segundoNombre(datosDatabook.getSegundoNombre())
						.primerApellido(datosDatabook.getPrimerApellido())
						.segundoApellido(datosDatabook.getSegundoApellido())
						.fechaNacimiento(fechaNac)
						.fechaIngreso(new Date())
						.correo(registro.get("correo"))
						.celular(registro.get("celular"))
						.estado(EstadoCliente.ACTIVO)
						.build();

				clientsEntity = clientsService.crearCliente(clientsEntity);

				// Crear cuenta
            AccountsEntity accountsEntity = AccountsEntity.builder()
                .numeroCuenta(accountsService.generarNumeroCuenta())
                .cliente(clientsEntity)
                .fechaCreacion(new Date())
                .valorNomina(new BigDecimal(registro.get("valorNomina")))
                .build();

            accountsService.crearCuenta(accountsEntity);

				// Agregar a resultados exitosos
				//            ClientsDTO clientsDTO = ClientsDTO.builder()
				//                .codigoCliente(clientsEntity.getCodigoCliente())
				//                .numeroIdentificacion(numeroId)
				//                .numeroCuenta(accountsEntity.getNumeroCuenta())
				//                .build();

				//            resultado.getClientesCargados().add(clientsDTO);

				Log.info(LoadFilesService.class,"Cliente procesado exitosamente: {}", numeroId);

			} catch (Exception e) {
				resultado.getErrores().add("Línea " + numLinea + ": Error al crear cliente: " + 
						e.getMessage());            
				Log.info(LoadFilesService.class,"Error procesando registro en línea" + numLinea, e);
			}
		}

		private String generarCodigoCliente() {
			return "CLI-" + System.currentTimeMillis();
		}

		public InputStream extractFileAsStream(
				MultipartFormDataInput input,
				String fieldName
				) throws Exception {

			Map<String, List<InputPart>> formDataMap = input.getFormDataMap();

			if (!formDataMap.containsKey(fieldName)) {
				throw new IllegalArgumentException("No se encontró el campo: " + fieldName);
			}

			InputPart inputPart = formDataMap.get(fieldName).get(0);

			return inputPart.getBody(InputStream.class, null);
		}

		private InputStream saveTMPFile(MultipartFormDataInput input) {
			try {
				String osName = System.getProperty("os.name").toLowerCase();

				java.nio.file.Path basePath;

				if (osName.contains("win")) {
					basePath = java.nio.file.Paths.get("C:", "files");
				} else {
					basePath = java.nio.file.Paths.get("/files");
				}

				Map<String, java.util.List<org.jboss.resteasy.plugins.providers.multipart.InputPart>> parts =
						input.getFormDataMap();

				InputStream fileStream = parts.get("file").get(0).getBody(InputStream.class, null);
				String fileName = parts.get("file").get(0)
						.getHeaders()
						.getFirst("Content-Disposition")
						.replaceAll(".*filename=\"([^\"]+)\".*", "$1");

				java.nio.file.Path target = java.nio.file.Path.of(basePath.toString(), fileName);
				Files.copy(fileStream, target);

				return fileStream;

			} catch (Exception e) {
				System.out.println(e.getMessage());
				return null;
			}
		}

	}
