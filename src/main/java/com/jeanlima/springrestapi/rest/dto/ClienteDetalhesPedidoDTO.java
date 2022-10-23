package com.jeanlima.springrestapi.rest.dto;

import java.math.BigDecimal;
import java.util.Set;

import com.jeanlima.springrestapi.model.ItemPedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDetalhesPedidoDTO {
    private Integer id;
    private Set<ItemPedido> itens;
    private BigDecimal total;
}
