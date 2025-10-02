package com.turmab.helpdesk.service;

import com.turmab.helpdesk.domain.Pessoa;
import com.turmab.helpdesk.domain.Tecnico;
import com.turmab.helpdesk.domain.dtos.TecnicoDTO;
import com.turmab.helpdesk.repositories.PessoaRepository;
import com.turmab.helpdesk.repositories.TecnicoRepository;
import com.turmab.helpdesk.service.exceptions.DataIntegrityViolationException;
import com.turmab.helpdesk.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Classe de serviço para gerenciar as operações de negócio relacionadas a Técnicos.
 * Contém a lógica para criar, ler, atualizar, deletar e validar técnicos.
 *
 * @author Seu Nome
 * @version 1.0
 * @since 2025-10-02
 */
@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository repository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Busca um técnico pelo seu ID.
     *
     * @param id O ID do técnico a ser buscado.
     * @return A entidade Tecnico correspondente ao ID.
     * @throws ObjectNotFoundException Se nenhum técnico for encontrado com o ID fornecido.
     */
    public Tecnico findById(Integer id) {
        Optional<Tecnico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
    }

    /**
     * Retorna uma lista com todas as entidades de Técnicos cadastradas.
     * A conversão para DTO é feita na camada de Resource (Controller).
     *
     * @return Uma lista de entidades Tecnico.
     */
    public List<Tecnico> findAll() {
        return repository.findAll();
    }

    /**
     * Cria um novo técnico no sistema a partir de um DTO.
     * A senha é criptografada antes de ser salva.
     *
     * @param objDTO O DTO contendo os dados do novo técnico.
     * @return A entidade Tecnico que foi persistida no banco de dados.
     */
    public Tecnico create(TecnicoDTO objDTO) {
        objDTO.setId(null); // Garante que estamos criando uma nova instância
        objDTO.setSenha(passwordEncoder.encode(objDTO.getSenha())); // Criptografa a senha
        validaPorCpfEEmail(objDTO);
        Tecnico newObj = new Tecnico(objDTO);
        return repository.save(newObj);
    }

    /**
     * Atualiza as informações de um técnico existente.
     *
     * @param id O ID do técnico a ser atualizado.
     * @param objDTO O DTO com os novos dados para o técnico.
     * @return A entidade Tecnico atualizada.
     */
    public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
        objDTO.setId(id);
        Tecnico oldObj = findById(id);
        validaPorCpfEEmail(objDTO);
        oldObj = new Tecnico(objDTO);
        return repository.save(oldObj);
    }


    /**
     * Deleta um técnico do banco de dados pelo seu ID.
     * Valida se o técnico possui chamados associados antes de deletar.
     *
     * @param id O ID do técnico a ser deletado.
     * @throws DataIntegrityViolationException Se o técnico possuir ordens de serviço.
     */
    public void delete(Integer id) {
        Tecnico obj = findById(id);
        if (obj.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
        }
        repository.deleteById(id);
    }

    /**
     * Valida se o CPF ou E-mail fornecido já existem na base de dados,
     * ignorando o próprio ID do usuário em caso de atualização.
     *
     * @param objDTO O DTO do técnico contendo CPF e E-mail.
     * @throws DataIntegrityViolationException Se o CPF ou E-mail já estiverem em uso por outro usuário.
     */
    private void validaPorCpfEEmail(TecnicoDTO objDTO) {
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