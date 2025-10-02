package com.turmab.helpdesk.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.turmab.helpdesk.domain.Pessoa;
import com.turmab.helpdesk.repositories.PessoaRepository;

/**
 * Implementação de {@link UserDetailsService} para autenticação de usuários.
 *
 * <p>
 * O Spring Security utiliza esta classe para buscar um usuário no banco de dados
 * a partir do e-mail informado no login. Ela retorna um objeto {@link UserDetails}
 * que o Spring Security usa para verificar senha e permissões.
 * </p>
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PessoaRepository pessoaRepository;

    /**
     * Carrega o usuário a partir do e-mail informado.
     *
     * @param email e-mail do usuário informado no login
     * @return instância de {@link UserSS} contendo ID, e-mail, senha e perfis/roles
     * @throws UsernameNotFoundException se não houver usuário com o e-mail informado
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // =================================================================================
        // LINHA DE DEBUG ADICIONADA PARA VERIFICAR O E-MAIL RECEBIDO
        System.out.println(">>> [UserDetailsService] Buscando usuário pelo email: " + email);
        // =================================================================================

        Optional<Pessoa> pessoa = pessoaRepository.findByEmail(email);

        if (pessoa.isPresent()) {
            return new UserSS(
                    pessoa.get().getId(),
                    pessoa.get().getEmail(),
                    pessoa.get().getSenha(),
                    pessoa.get().getPerfis()
            );
        }

        throw new UsernameNotFoundException("Usuário não encontrado: " + email);
    }
}