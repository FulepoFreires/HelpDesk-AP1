package com.turmab.helpdesk.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity; // <-- IMPORT ADICIONADO
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.turmab.helpdesk.security.JWTAuthenticationFilter;
import com.turmab.helpdesk.security.JWTAuthorizationFilter; // <-- IMPORT ADICIONADO
import com.turmab.helpdesk.security.JWTUtil;

/**
 * Classe de configuração de segurança da aplicação.
 * <p>
 * Esta classe estende {@link WebSecurityConfigurerAdapter} para configurar
 * autenticação, autorização, CORS e filtros JWT.
 * A anotação @EnableGlobalMethodSecurity habilita a segurança baseada em anotações nos métodos.
 * </p>
 *
 * @author Seu Nome (Wagner, atualizado por Gemini)
 * @version 1.1
 */
@Configuration // Adicionando a anotação @Configuration que pode estar faltando
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // <-- LINHA ADICIONADA
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * URLs públicas que não exigem autenticação.
     * Exemplo: console do H2 para uso em ambiente de desenvolvimento/testes.
     */
    private static final String[] PUBLIC_MATCHES = { "/h2-console/**" };

    /**
     * Ambiente atual da aplicação (profiles ativos, etc).
     * Injetado pelo Spring para permitir condicionais (ex.: liberar frame para H2 em profile "test").
     */
    @Autowired
    private Environment env;

    /**
     * Utilitário para operações com JWT (gerar/validar tokens).
     * Injetado pelo Spring.
     */
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Serviço que fornece os dados do usuário (username, senha, roles).
     * Usado para autenticação.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configurações de segurança HTTP.
     *
     * @param http objeto para configurar as regras HTTP do Spring Security
     * @throws Exception se ocorrer erro na configuração
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }

        http.cors().and().csrf().disable();

        // Filtro de AUTENTICAÇÃO: Intercepta o /login, autentica e gera o token.
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
        
        // Filtro de AUTORIZAÇÃO: Intercepta as outras requisições, valida o token e libera o acesso.
        http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService)); // <-- LINHA ADICIONADA

        http.authorizeRequests()
                .antMatchers(PUBLIC_MATCHES).permitAll()
                .anyRequest().authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * Configura o provedor de autenticação do Spring Security.
     *
     * @param auth builder para configurar autenticação
     * @throws Exception se ocorrer erro na configuração de autenticação
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * Define a configuração de CORS para a aplicação.
     *
     * @return fonte de configuração CORS
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Bean que provê o {@link BCryptPasswordEncoder} usado para criptografar senhas.
     *
     * @return um {@link BCryptPasswordEncoder} pronto para uso
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}