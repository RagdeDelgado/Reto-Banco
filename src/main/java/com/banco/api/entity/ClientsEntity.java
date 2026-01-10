package com.banco.api.entity;

import java.time.LocalDate;

import com.banco.api.enums.EstadoCliente;
import com.banco.api.enums.TipoIdentificacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "clients")
public class ClientsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCliente;

	@Column(unique = true, nullable = false)
	private String codigoCliente;

	@Enumerated(EnumType.STRING)
	private TipoIdentificacion tipoIdentificacion;

	@Column(unique = true, nullable = false)
	private String numeroIdentificacion;

	@Column
	private String primerNombre;

	@Column(length = 50)
	private String segundoNombre;

	@Column
	private String primerApellido;

	@Column
	private String segundoApellido;

	@Column(nullable = false)
	private LocalDate fechaNacimiento;

	@Column(nullable = false)
	private LocalDate fechaIngreso;

	@Column(unique = true, nullable = false)
	private String correo;

	@Column(nullable = false)
	private String celular;

	@Enumerated(EnumType.STRING)
	private EstadoCliente estado;

}
