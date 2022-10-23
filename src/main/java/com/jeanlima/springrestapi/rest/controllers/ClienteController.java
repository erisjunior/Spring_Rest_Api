package com.jeanlima.springrestapi.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jeanlima.springrestapi.model.Cliente;
import com.jeanlima.springrestapi.repository.ClienteRepository;
import com.jeanlima.springrestapi.rest.dto.ClienteDetalhesDTO;
import com.jeanlima.springrestapi.rest.dto.ClienteDetalhesPedidoDTO;

@RequestMapping("/api/clientes")
@RestController
public class ClienteController {

    @Autowired
    private ClienteRepository clientes;

    @GetMapping("{id}")
    public Cliente getClienteById(@PathVariable Integer id) {
        return clientes
                .findById(id)
                .orElseThrow(() -> // se nao achar lança o erro!
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @GetMapping("{id}/detalhes")
    public ClienteDetalhesDTO getClienteDetalhesById(@PathVariable Integer id) {
        return clientes
                .findById(id)
                .map(cliente -> {
                    ClienteDetalhesDTO clienteDetalhes = new ClienteDetalhesDTO();
                    clienteDetalhes.setId(cliente.getId());
                    clienteDetalhes.setNome(cliente.getNome());
                    clienteDetalhes.setCpf(cliente.getCpf());
                    clienteDetalhes.setPedidos(cliente.getPedidos()
                            .stream()
                            .map(pedido -> {
                                ClienteDetalhesPedidoDTO pedidoDTO = new ClienteDetalhesPedidoDTO();
                                pedidoDTO.setId(pedido.getId());
                                pedidoDTO.setTotal(pedido.getTotal());
                                return pedidoDTO;
                            })
                            .collect(Collectors.toSet()));
                    return clienteDetalhes;
                })
                .orElseThrow(() -> // se nao achar lança o erro!
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente save(@RequestBody Cliente cliente) {
        return clientes.save(cliente);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        clientes.findById(id)
                .map(cliente -> {
                    clientes.delete(cliente);
                    return cliente;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));

    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id,
            @RequestBody Cliente cliente) {
        clientes
                .findById(id)
                .map(clienteExistente -> {
                    cliente.setId(clienteExistente.getId());
                    clientes.save(cliente);
                    return clienteExistente;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void partialUpdate(@PathVariable Integer id,
            @RequestBody Cliente cliente) {
        clientes
                .findById(id)
                .map(clienteExistente -> {
                    cliente.setId(clienteExistente.getId());

                    if (cliente.getNome() == null) {
                        cliente.setNome(clienteExistente.getNome());
                    }
                    if (cliente.getCpf() == null) {
                        cliente.setCpf(clienteExistente.getCpf());
                    }

                    clientes.save(cliente);
                    return clienteExistente;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @GetMapping
    public List<Cliente> find(Cliente filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example<Cliente> example = Example.of(filtro, matcher);
        return clientes.findAll(example);
    }

}
