package com.turmab.helpdesk.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.turmab.helpdesk.domain.enums.Perfil;

/**
 * Classe abstrata que representa uma Pessoa no sistema.
 * Serve como base para as entidades Tecnico e Cliente, compartilhando atributos comuns
 * como id, nome, cpf, email e senha.
 *
 * @author Seu Nome
 * @version 1.0
 * @since 2025-10-02
 */
@Entity
public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    protected String nome;

    @Column(unique = true)
    protected String cpf;

    @Column(unique = true)
    protected String email;

    protected String senha;

    /**
     * Conjunto de perfis (roles) associados à pessoa.
     * Armazenado como String no banco de dados (ex: "ADMIN", "CLIENTE").
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PERFIS")
    protected Set<Perfil> perfis = new HashSet<>();

    @JsonFormat(pattern = "dd/MM/yyyy")
    protected LocalDate dataCriacao = LocalDate.now();

    /**
     * Construtor padrão. Adiciona o perfil de CLIENTE como padrão.
     */
    public Pessoa() {
        super();
        addPerfil(Perfil.CLIENTE);
    }

    /**
     * Construtor com parâmetros.
     * @param id O ID da pessoa.
     * @param nome O nome da pessoa.
     * @param cpf O CPF da pessoa.
     * @param email O e-mail da pessoa.
     * @param senha A senha (já criptografada) da pessoa.
     */
    public Pessoa(Integer id, String nome, String cpf, String email, String senha) {
        super();
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        addPerfil(Perfil.CLIENTE);
    }

    // --- GETTERS E SETTERS ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Retorna o conjunto de perfis (Enums) da pessoa.
     * @return Um Set de Perfil.
     */
    public Set<Perfil> getPerfis() {
        return perfis;
    }

    /**
     * Adiciona um novo perfil (Enum) ao conjunto de perfis da pessoa.
     * @param perfil O Perfil a ser adicionado.
     */
    public void addPerfil(Perfil perfil) {
        this.perfis.add(perfil);
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    // --- EQUALS E HASHCODE (baseado em ID e CPF) ---

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pessoa other = (Pessoa) obj;
        if (cpf == null) {
            if (other.cpf != null)
                return false;
        } else if (!cpf.equals(other.cpf))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}