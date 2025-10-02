package com.turmab.helpdesk.resources;

import com.turmab.helpdesk.domain.Tecnico;
import com.turmab.helpdesk.domain.dtos.TecnicoDTO;
import com.turmab.helpdesk.service.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciar as requisições relacionadas a Técnicos.
 * Expõe os endpoints para criar, ler, atualizar e deletar técnicos.
 * O caminho base para todos os endpoints nesta classe é /tecnicos.
 *
 * @author Seu Nome
 * @version 1.0
 * @since 2025-10-02
 */
@RestController
@RequestMapping(value = "/tecnicos")
public class TecnicoResource {

    @Autowired
    private TecnicoService service;

    /**
     * Endpoint para buscar um técnico específico pelo seu ID.
     *
     * @param id O ID do técnico a ser buscado (vem da URL).
     * @return Um ResponseEntity contendo o DTO do técnico e o status HTTP 200 (OK).
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<TecnicoDTO> findById(@PathVariable Integer id) {
        Tecnico obj = service.findById(id);
        return ResponseEntity.ok().body(new TecnicoDTO(obj));
    }

    /**
     * Endpoint para listar todos os técnicos cadastrados.
     *
     * @return Um ResponseEntity contendo uma lista de DTOs de técnicos e o status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<TecnicoDTO>> findAll() {
        List<Tecnico> list = service.findAll();
        // Converte a lista de entidades Tecnico para uma lista de TecnicoDTO
        List<TecnicoDTO> listDTO = list.stream().map(obj -> new TecnicoDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    /**
     * Endpoint para criar um novo técnico.
     * Apenas usuários com perfil de ADMIN podem acessar este endpoint.
     *
     * @param objDTO O DTO do técnico, vindo no corpo da requisição.
     * @return Um ResponseEntity com a URL do novo recurso no cabeçalho e o status HTTP 201 (Created).
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TecnicoDTO> create(@Valid @RequestBody TecnicoDTO objDTO) {
        Tecnico newObj = service.create(objDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /**
     * Endpoint para atualizar um técnico existente.
     * Apenas usuários com perfil de ADMIN podem acessar este endpoint.
     *
     * @param id O ID do técnico a ser atualizado (vem da URL).
     * @param objDTO O DTO com os novos dados do técnico, vindo no corpo da requisição.
     * @return Um ResponseEntity contendo o DTO do técnico atualizado e o status HTTP 200 (OK).
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<TecnicoDTO> update(@PathVariable Integer id, @Valid @RequestBody TecnicoDTO objDTO) {
        Tecnico obj = service.update(id, objDTO);
        return ResponseEntity.ok().body(new TecnicoDTO(obj));
    }

    /**
     * Endpoint para deletar um técnico.
     * Apenas usuários com perfil de ADMIN могут acessar este endpoint.
     *
     * @param id O ID do técnico a ser deletado (vem da URL).
     * @return Um ResponseEntity sem conteúdo e com status HTTP 204 (No Content).
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<TecnicoDTO> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}