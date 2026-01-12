package com.banco.api.dto;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoadResultsDTO {
	@Builder.Default
    private List<ClientsDTO> clientesCargados = new ArrayList<>();
    
    @Builder.Default
    private List<String> errores = new ArrayList<>();
    
    @Builder.Default
    private List<String> clientesNoEncontrados = new ArrayList<>();
    
    private int totalProcesados;
    private int totalExitosos;
    private int totalErrores;
}
