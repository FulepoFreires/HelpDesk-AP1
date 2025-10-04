package com.turmab.helpdesk.resources;

import com.turmab.helpdesk.domain.Cliente;
import com.turmab.helpdesk.domain.dtos.ClienteDTO;
import com.turmab.helpdesk.service.ClienteService;
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
 * Controller REST para gerenciar as requisições relacionadas a Clientes.
 * Expõe os endpoints para criar, ler, atualizar e deletar clientes.
 * O caminho base para todos os endpoints nesta classe é /clientes.
 *
 * @author Seu Nome
 * @version 1.0
 * @since 2025-10-04
 */
@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    /**
     * Endpoint para buscar um cliente específico pelo seu ID.
     *
     * @param id O ID do cliente a ser buscado (vem da URL).
     * @return Um ResponseEntity contendo o DTO do cliente e o status HTTP 200 (OK).
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable Integer id) {
        Cliente obj = service.findById(id);
        return ResponseEntity.ok().body(new ClienteDTO(obj));
    }

    /**
     * Endpoint para listar todos os clientes cadastrados.
     *
     * @return Um ResponseEntity contendo uma lista de DTOs de clientes e o status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<Cliente> list = service.findAll();
        // Converte a lista da entidade Cliente para uma lista de ClienteDTO
        List<ClienteDTO> listDTO = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    /**
     * Endpoint para criar um novo cliente.
     * Apenas usuários com perfil de ADMIN podem acessar este endpoint.
     *
     * @param objDTO O DTO do cliente, vindo no corpo da requisição.
     * @return Um ResponseEntity com a URL do novo recurso no cabeçalho e o status HTTP 201 (Created).
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteDTO objDTO) {
        Cliente newObj = service.create(objDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /**
     * Endpoint para atualizar um cliente existente.
     * Apenas usuários com perfil de ADMIN podem acessar este endpoint.
     *
     * @param id O ID do cliente a ser atualizado (vem da URL).
     * @param objDTO O DTO com os novos dados do cliente, vindo no corpo da requisição.
     * @return Um ResponseEntity contendo o DTO do cliente atualizado e o status HTTP 200 (OK).
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Integer id, @Valid @RequestBody ClienteDTO objDTO) {
        Cliente obj = service.update(id, objDTO);
        return ResponseEntity.ok().body(new ClienteDTO(obj));
    }

    /**
     * Endpoint para deletar um cliente.
     * Apenas usuários com perfil de ADMIN podem acessar este endpoint.
     *
     * @param id O ID do cliente a ser deletado (vem da URL).
     * @return Um ResponseEntity sem conteúdo e com status HTTP 204 (No Content).
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}