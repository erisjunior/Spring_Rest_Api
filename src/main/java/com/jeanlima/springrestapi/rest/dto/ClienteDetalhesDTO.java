package com.jeanlima.springrestapi.rest.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDetalhesDTO {
    private Integer id;
    private Set<ClienteDetalhesPedidoDTO> pedidos;
    private String nome;
    private String cpf;
}
