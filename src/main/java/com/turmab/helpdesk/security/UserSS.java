package com.turmab.helpdesk.security;

import com.turmab.helpdesk.domain.enums.Perfil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementação de UserDetails do Spring Security.
 * Esta classe encapsula as informações do usuário (Pessoa) no formato que o
 * Spring Security entende, incluindo as permissões (authorities) com o prefixo ROLE_.
 */
public class UserSS implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String email;
    private String senha;
    private Collection<? extends GrantedAuthority> authorities;

    public UserSS() {
    }

    /**
     * Construtor que recebe os dados do usuário e converte os perfis para o formato
     * que o Spring Security espera (GrantedAuthority com prefixo ROLE_).
     *
     * @param id O ID do usuário.
     * @param email O email (username) do usuário.
     * @param senha A senha (já codificada) do usuário.
     * @param perfis O conjunto de perfis (Enums) do usuário.
     */
    public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        // ALTERAÇÃO CRÍTICA AQUI: Adiciona o prefixo "ROLE_" a cada perfil
        this.authorities = perfis.stream()
                .map(x -> new SimpleGrantedAuthority("ROLE_" + x.getDescricao()))
                .collect(Collectors.toSet());
    }

    public Integer getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}