package com.turmab.helpdesk.resources;

import com.turmab.helpdesk.domain.Chamado;
import com.turmab.helpdesk.domain.dtos.ChamadoDTO;
import com.turmab.helpdesk.service.ChamadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciar as requisições relacionadas a Chamados.
 * Expõe os endpoints para criar, ler e atualizar chamados.
 * O caminho base para todos os endpoints nesta classe é /chamados.
 *
 * @author Seu Nome
 * @version 1.0
 * @since 2025-10-02
 */
@RestController
@RequestMapping(value = "/chamados")
public class ChamadoResource {

    @Autowired
    private ChamadoService service;

    /**
     * Endpoint para buscar um chamado específico pelo seu ID.
     *
     * @param id O ID do chamado a ser buscado (vem da URL).
     * @return Um ResponseEntity contendo o DTO do chamado e o status HTTP 200 (OK).
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> findById(@PathVariable Integer id) {
        Chamado obj = service.findById(id);
        return ResponseEntity.ok().body(new ChamadoDTO(obj));
    }

    /**
     * Endpoint para listar todos os chamados cadastrados.
     *
     * @return Um ResponseEntity contendo uma lista de DTOs de chamados e o status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ChamadoDTO>> findAll() {
        List<Chamado> list = service.findAll();
        List<ChamadoDTO> listDTO = list.stream().map(obj -> new ChamadoDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    /**
     * Endpoint para criar um novo chamado.
     *
     * @param objDTO O DTO do chamado, vindo no corpo da requisição.
     * @return Um ResponseEntity com a URL do novo recurso no cabeçalho e o status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ChamadoDTO> create(@Valid @RequestBody ChamadoDTO objDTO) {
        Chamado obj = service.create(objDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /**
     * Endpoint para atualizar um chamado existente.
     *
     * @param id O ID do chamado a ser atualizado (vem da URL).
     * @param objDTO O DTO com os novos dados do chamado, vindo no corpo da requisição.
     * @return Um ResponseEntity contendo o DTO do chamado atualizado e o status HTTP 200 (OK).
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ChamadoDTO> update(@PathVariable Integer id, @Valid @RequestBody ChamadoDTO objDTO) {
        Chamado newObj = service.update(id, objDTO);
        return ResponseEntity.ok().body(new ChamadoDTO(newObj));
    }
}