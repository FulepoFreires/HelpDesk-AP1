package com.turmab.helpdesk.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro de autorização JWT.
 * Intercepta todas as requisições (exceto /login) para validar o token JWT.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método principal do filtro, executado para cada requisição.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        System.out.println("\n>>> [JWTAuthorizationFilter] Iniciando filtro de autorização...");

        String header = request.getHeader("Authorization");
        
        if (header != null && header.startsWith("Bearer ")) {
            System.out.println(">>> [JWTAuthorizationFilter] Header 'Authorization' encontrado.");
            
            // Pega o token a partir do 7º caractere (depois de "Bearer ")
            String token = header.substring(7);
            
            // Valida o token usando o JWTUtil
            if (jwtUtil.tokenValido(token)) {
                System.out.println(">>> [JWTAuthorizationFilter] Token é válido.");
                
                // Obtém o objeto de autenticação a partir do token
                UsernamePasswordAuthenticationToken auth = getAuthentication(token);
                
                if (auth != null) {
                    System.out.println(">>> [JWTAuthorizationFilter] Autenticação criada com sucesso. Setando no contexto de segurança.");
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    System.out.println(">>> [JWTAuthorizationFilter] FALHA: Não foi possível criar a autenticação.");
                }
            } else {
                 System.out.println(">>> [JWTAuthorizationFilter] ATENÇÃO: Token inválido recebido.");
            }
        } else {
            System.out.println(">>> [JWTAuthorizationFilter] Header 'Authorization' não encontrado ou mal formatado.");
        }

        // Continua a execução da cadeia de filtros
        chain.doFilter(request, response);
        System.out.println(">>> [JWTAuthorizationFilter] Finalizando filtro.\n");
    }

    /**
     * Gera o objeto de autenticação do Spring Security a partir do token.
     * @param token O token JWT válido.
     * @return Um objeto UsernamePasswordAuthenticationToken com os dados e permissões do usuário.
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = jwtUtil.getUsername(token);
        System.out.println(">>> [JWTAuthorizationFilter] Username extraído do token: " + username);
        
        if (username == null) {
            System.out.println(">>> [JWTAuthorizationFilter] FALHA: Username não encontrado no token.");
            return null;
        }

        // Busca o usuário completo no banco de dados para pegar as permissões atualizadas
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        System.out.println(">>> [JWTAuthorizationFilter] Detalhes do usuário carregados do banco. Verificando permissões (authorities):");
        if (userDetails.getAuthorities() == null || userDetails.getAuthorities().isEmpty()) {
            System.out.println(">>> [JWTAuthorizationFilter] ATENÇÃO: NENHUMA PERMISSÃO (AUTHORITY) FOI ENCONTRADA PARA ESTE USUÁRIO!");
        } else {
            // ESTE É O LOG MAIS IMPORTANTE DE TODOS
            userDetails.getAuthorities().forEach(authority -> {
                System.out.println("    - Authority encontrada: " + authority.getAuthority());
            });
        }
        
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}