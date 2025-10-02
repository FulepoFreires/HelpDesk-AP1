package com.turmab.helpdesk.service;

import com.turmab.helpdesk.domain.Chamado;
import com.turmab.helpdesk.domain.Cliente;
import com.turmab.helpdesk.domain.Tecnico;
import com.turmab.helpdesk.domain.dtos.ChamadoDTO;
import com.turmab.helpdesk.domain.enums.Prioridade;
import com.turmab.helpdesk.domain.enums.Status;
import com.turmab.helpdesk.repositories.ChamadoRepository;
import com.turmab.helpdesk.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Classe de serviço para gerenciar as operações de negócio relacionadas a Chamados.
 * Contém a lógica para criar, ler, atualizar e validar chamados.
 *
 * @author Seu Nome
 * @version 1.0
 * @since 2025-10-02
 */
@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository repository;
    @Autowired
    private TecnicoService tecnicoService;
    @Autowired
    private ClienteService clienteService;

    /**
     * Busca um chamado pelo seu ID.
     *
     * @param id O ID do chamado a ser buscado.
     * @return O objeto Chamado correspondente ao ID.
     * @throws ObjectNotFoundException Lançada se nenhum chamado for encontrado com o ID fornecido.
     */
    public Chamado findById(Integer id) {
        Optional<Chamado> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
    }

    /**
     * Retorna uma lista com todos os chamados cadastrados no sistema.
     *
     * @return Uma lista de objetos Chamado.
     */
    public List<Chamado> findAll() {
        return repository.findAll();
    }

    /**
     * Cria um novo chamado no sistema a partir de um DTO.
     *
     * @param objDTO O DTO contendo os dados do novo chamado.
     * @return O objeto Chamado que foi persistido no banco de dados.
     */
    public Chamado create(@Valid ChamadoDTO objDTO) {
        return repository.save(newChamado(objDTO));
    }

    /**
     * Atualiza as informações de um chamado existente.
     *
     * @param id O ID do chamado a ser atualizado.
     * @param objDTO O DTO com os novos dados para o chamado.
     * @return O objeto Chamado atualizado.
     */
    public Chamado update(Integer id, @Valid ChamadoDTO objDTO) {
        objDTO.setId(id);
        Chamado oldObj = findById(id);
        oldObj = newChamado(objDTO);
        return repository.save(oldObj);
    }

    /**
     * Converte um ChamadoDTO em uma entidade Chamado e preenche os dados.
     * Este método auxiliar é usado tanto para criação quanto para atualização.
     *
     * @param objDTO O DTO de origem.
     * @return Uma instância de Chamado preenchida.
     */
    private Chamado newChamado(ChamadoDTO objDTO) {
        Tecnico tecnico = tecnicoService.findById(objDTO.getTecnico());
        Cliente cliente = clienteService.findById(objDTO.getCliente());

        Chamado chamado = new Chamado();
        if (objDTO.getId() != null) {
            chamado.setId(objDTO.getId());
        }

        if(objDTO.getStatus().equals(2)) { // Se o status for "ENCERRADO"
            chamado.setDataFechamento(LocalDate.now());
        }

        chamado.setTecnico(tecnico);
        chamado.setCliente(cliente);
        chamado.setPrioridade(Prioridade.toEnum(objDTO.getPrioridade()));
        chamado.setStatus(Status.toEnum(objDTO.getStatus()));
        chamado.setTitulo(objDTO.getTitulo());
        chamado.setObservacoes(objDTO.getObservacoes());
        return chamado;
    }
}