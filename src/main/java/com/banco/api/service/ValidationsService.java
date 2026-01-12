package com.banco.api.service;

import lombok.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import jakarta.ejb.Stateless;


//@Stateless
public class ValidationsService {

	private static final Pattern EMAIL_PATTERN = 
	        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	    @Data
	    @AllArgsConstructor
	    public static class ResultadoValidacion {
	        private boolean valido;
	        private String mensaje;
	    }

	    public ResultadoValidacion validarTipoIdentificacion(String tipo) {
	        if (tipo == null || tipo.isEmpty()) {
	            return new ResultadoValidacion(false, "Tipo de identificación vacío");
	        }
	        if (!tipo.matches("[CP]")) {
	            return new ResultadoValidacion(false, 
	                "Tipo de identificación inválido. Solo se permiten C o P");
	        }
	        return new ResultadoValidacion(true, "");
	    }

	    public ResultadoValidacion validarNumeroIdentificacion(String numero) {
	        if (numero == null || numero.isEmpty()) {
	            return new ResultadoValidacion(false, "Número de identificación vacío");
	        }
	        if (!numero.matches("^[a-zA-Z0-9]+$")) {
	            return new ResultadoValidacion(false, 
	                "Número de identificación debe ser alfanumérico");
	        }
	        return new ResultadoValidacion(true, "");
	    }

	    public ResultadoValidacion validarFechaIngreso(String fecha) {
	        if (fecha == null || fecha.isEmpty()) {
	            return new ResultadoValidacion(false, "Fecha de ingreso vacía");
	        }
	        try {
	            LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            return new ResultadoValidacion(true, "");
	        } catch (DateTimeParseException e) {
	            return new ResultadoValidacion(false, 
	                "Fecha de ingreso con formato inválido. Use yyyy-MM-dd");
	        }
	    }

	    public ResultadoValidacion validarValorNomina(String valor) {
	        if (valor == null || valor.isEmpty()) {
	            return new ResultadoValidacion(false, "Valor de nómina vacío");
	        }
	        if (!valor.matches("^\\d+(\\.\\d{1,2})?$")) {
	            return new ResultadoValidacion(false, 
	                "Valor de nómina debe contener solo números");
	        }
	        return new ResultadoValidacion(true, "");
	    }

	    public ResultadoValidacion validarCorreo(String correo) {
	        if (correo == null || correo.isEmpty()) {
	            return new ResultadoValidacion(false, "Correo vacío");
	        }
	        if (!EMAIL_PATTERN.matcher(correo).matches()) {
	            return new ResultadoValidacion(false, 
	                "Formato de correo electrónico inválido");
	        }
	        return new ResultadoValidacion(true, "");
	    }

	    public ResultadoValidacion validarCelular(String celular) {
	        if (celular == null || celular.isEmpty()) {
	            return new ResultadoValidacion(false, "Celular vacío");
	        }
	        if (!celular.matches("^\\d{10}$")) {
	            return new ResultadoValidacion(false, 
	                "Celular debe contener exactamente 10 dígitos numéricos");
	        }
	        return new ResultadoValidacion(true, "");
	    }	
}
