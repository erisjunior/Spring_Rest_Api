package com.jeanlima.springrestapi.rest.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;

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

import com.jeanlima.springrestapi.model.Estoque;
import com.jeanlima.springrestapi.repository.EstoqueRepository;

@RestController
@RequestMapping("/api/estoques")
public class EstoqueController {

    @Autowired
    private EstoqueRepository repository;

    @PostMapping
    @ResponseStatus(CREATED)
    public Estoque save(@RequestBody Estoque estoque) {
        return repository.save(estoque);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody Estoque estoque) {
        repository
                .findById(id)
                .map(e -> {
                    estoque.setId(e.getId());
                    repository.save(estoque);
                    return estoque;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estoque não encontrado."));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void partialUpdate(@PathVariable Integer id,
            @RequestBody Estoque estoque) {
        repository
                .findById(id)
                .map(estoqueExistente -> {
                    estoque.setId(estoqueExistente.getId());

                    if (estoque.getQuantidade() == null) {
                        estoque.setQuantidade(estoqueExistente.getQuantidade());
                    }

                    repository.save(estoque);
                    return estoqueExistente;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estoque não encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository
                .findById(id)
                .map(p -> {
                    repository.delete(p);
                    return Void.TYPE;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estoque não encontrado."));
    }

    @GetMapping("{id}")
    public Estoque getById(@PathVariable Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estoque não encontrado."));
    }

    @GetMapping
    public List<Estoque> find(Estoque filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example<Estoque> example = Example.of(filtro, matcher);
        return repository.findAll(example);
    }
}
