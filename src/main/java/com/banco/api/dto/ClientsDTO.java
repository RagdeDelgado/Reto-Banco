package com.banco.api.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ClientsDTO {
	private String codigoCliente;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private LocalDate fechaNacimiento;
    private LocalDate fechaIngreso;
    private String correo;
    private String celular;
    private String numeroCuenta;
    private String valorNomina;
    private String estado;                           
}
