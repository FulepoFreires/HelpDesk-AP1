package com.turmab.helpdesk.domain.dtos;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import com.turmab.helpdesk.domain.Cliente;
import com.turmab.helpdesk.domain.enums.Perfil;

public class ClienteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String cpf;
    private String email;
    private Set<Integer> perfis;  // Guardando os códigos dos perfis
    private String dataCriacao;   // Se houver esse campo em Pessoa

    // Construtor padrão
    public ClienteDTO() {
        super();
    }

    // Construtor recebendo a entidade Cliente
    public ClienteDTO(Cliente obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.cpf = obj.getCpf();
        this.email = obj.getEmail();
        this.perfis = obj.getPerfis().stream()
                         .map(Perfil::getCodigo)
                         .collect(Collectors.toSet());
        this.dataCriacao = (obj.getDataCriacao() != null) ? obj.getDataCriacao().toString() : null;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Integer> getPerfis() { return perfis; }
    public void setPerfis(Set<Integer> perfis) { this.perfis = perfis; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }
}
