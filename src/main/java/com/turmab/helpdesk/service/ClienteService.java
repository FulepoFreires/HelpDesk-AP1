package com.turmab.helpdesk.service;

import com.turmab.helpdesk.domain.Cliente;
import com.turmab.helpdesk.domain.Pessoa;
import com.turmab.helpdesk.domain.dtos.ClienteDTO;
import com.turmab.helpdesk.repositories.ClienteRepository;
import com.turmab.helpdesk.repositories.PessoaRepository;
import com.turmab.helpdesk.service.exceptions.DataIntegrityViolationException;
import com.turmab.helpdesk.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Classe de serviço para gerenciar as operações de negócio relacionadas a Clientes.
 * Contém a lógica para criar, ler, atualizar, deletar e validar clientes.
 *
 * @author Seu Nome
 * @version 1.0
 * @since 2025-10-04
 */
@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Busca um cliente pelo seu ID.
     *
     * @param id O ID do cliente a ser buscado.
     * @return A entidade Cliente correspondente ao ID.
     * @throws ObjectNotFoundException Se nenhum cliente for encontrado com o ID fornecido.
     */
    public Cliente findById(Integer id) {
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! ID: " + id));
    }

    /**
     * Retorna uma lista com todas as entidades de Clientes cadastradas.
     * A conversão para DTO é feita na camada de Resource (Controller).
     *
     * @return Uma lista de entidades Cliente.
     */
    public List<Cliente> findAll() {
        return repository.findAll();
    }

    /**
     * Cria um novo cliente no sistema a partir de um DTO.
     * A senha é criptografada antes de ser salva.
     *
     * @param objDTO O DTO contendo os dados do novo cliente.
     * @return A entidade Cliente que foi persistida no banco de dados.
     */
    public Cliente create(ClienteDTO objDTO) {
        objDTO.setId(null); // Garante que estamos criando uma nova instância
        objDTO.setSenha(passwordEncoder.encode(objDTO.getSenha())); // Criptografa a senha
        validaPorCpfEEmail(objDTO);
        Cliente newObj = new Cliente(objDTO);
        return repository.save(newObj);
    }

    /**
     * Atualiza as informações de um cliente existente.
     *
     * @param id O ID do cliente a ser atualizado.
     * @param objDTO O DTO com os novos dados para o cliente.
     * @return A entidade Cliente atualizada.
     */
    public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
        objDTO.setId(id);
        Cliente oldObj = findById(id);
        validaPorCpfEEmail(objDTO);
        oldObj = new Cliente(objDTO);
        return repository.save(oldObj);
    }

    /**
     * Deleta um cliente do banco de dados pelo seu ID.
     * Valida se o cliente possui chamados associados antes de deletar.
     *
     * @param id O ID do cliente a ser deletado.
     * @throws DataIntegrityViolationException Se o cliente possuir chamados abertos.
     */
    public void delete(Integer id) {
        Cliente obj = findById(id);
        if (obj.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
        }
        repository.deleteById(id);
    }

    /**
     * Valida se o CPF ou E-mail fornecido já existem na base de dados,
     * ignorando o próprio ID do usuário em caso de atualização.
     *
     * @param objDTO O DTO do cliente contendo CPF e E-mail.
     * @throws DataIntegrityViolationException Se o CPF ou E-mail já estiverem em uso por outro usuário.
     */
    private void validaPorCpfEEmail(ClienteDTO objDTO) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
    }
}