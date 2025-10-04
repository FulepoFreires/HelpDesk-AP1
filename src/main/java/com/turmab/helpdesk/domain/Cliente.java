package com.turmab.helpdesk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turmab.helpdesk.domain.dtos.ClienteDTO;
import com.turmab.helpdesk.domain.enums.Perfil;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entidade que representa um Cliente no sistema.
 * Herda os atributos básicos da classe Pessoa.
 *
 * @author Seu Nome
 * @version 1.0
 * @since 2025-10-04
 */
@Entity
public class Cliente extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Lista de chamados associados a este cliente.
     * A anotação @JsonIgnore previne loops infinitos durante a serialização para JSON.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Chamado> chamados = new ArrayList<>();

    /**
     * Construtor padrão.
     * Adiciona o perfil de CLIENTE por padrão.
     */
    public Cliente() {
        super();
        addPerfil(Perfil.CLIENTE);
    }

    /**
     * Construtor com parâmetros.
     * @param id O ID do cliente.
     * @param nome O nome do cliente.
     * @param cpf O CPF do cliente.
     * @param email O e-mail do cliente.
     * @param senha A senha (já criptografada) do cliente.
     */
    public Cliente(Integer id, String nome, String cpf, String email, String senha) {
        super(id, nome, cpf, email, senha);
        addPerfil(Perfil.CLIENTE);
    }

    /**
     * Construtor que cria uma entidade Cliente a partir de um ClienteDTO.
     * Este é o método que estava faltando e causava o erro no ClienteService.
     *
     * @param obj O DTO com os dados do cliente.
     */
    public Cliente(ClienteDTO obj) {
        super();
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.cpf = obj.getCpf();
        this.email = obj.getEmail();
        this.senha = obj.getSenha();
        this.perfis = obj.getPerfis().stream().map(Perfil::toEnum).collect(Collectors.toSet());
        this.dataCriacao = obj.getDataCriacao();
    }


    public List<Chamado> getChamados() {
        return chamados;
    }

    public void setChamados(List<Chamado> chamados) {
        this.chamados = chamados;
    }
}