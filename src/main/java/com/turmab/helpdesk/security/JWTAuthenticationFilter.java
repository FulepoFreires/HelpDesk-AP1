package com.turmab.helpdesk.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turmab.helpdesk.domain.dtos.CredenciaisDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Filtro de autenticação JWT.
 * Responsável por interceptar a requisição de /login e tentar autenticar o usuário.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        super();
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Tenta realizar a autenticação do usuário.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            CredenciaisDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executado quando a autenticação é bem-sucedida.
     * Gera o token JWT e o adiciona no header da resposta.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // =========================================================================================
        // MÉTODO CORRIGIDO E LIMPO, COM AS LINHAS DE DEBUG
        // =========================================================================================

        // 1. Pega o username do usuário que foi autenticado com sucesso.
        String username = ((UserSS) authResult.getPrincipal()).getUsername();

        // 2. Imprime no console para termos certeza de quem foi autenticado (nosso "dedo-duro").
        System.out.println(">>> [JWTAuthenticationFilter] Usuário autenticado com sucesso: " + username);

        // 3. Gera o token JWT para este usuário.
        String token = jwtUtil.generateToken(username);

        // 4. Adiciona o token no cabeçalho da resposta.
        response.setHeader("Authorization", "Bearer " + token);
        response.setHeader("access-control-expose-headers", "Authorization");
        
        // 5. Imprime no console para confirmar que o token foi gerado para o usuário correto.
        System.out.println(">>> [JWTAuthenticationFilter] Token gerado para o usuário: " + username);
    }

    /**
     * Executado quando a autenticação falha.
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().append(jsonError());
    }

    private CharSequence jsonError() {
        long date = new Date().getTime();
        return "{"
                + "\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", "
                + "\"path\": \"/login\""
                + "}";
    }
}