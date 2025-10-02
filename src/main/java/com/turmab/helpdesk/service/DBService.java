package com.turmab.helpdesk.service;

import com.turmab.helpdesk.domain.Chamado;
import com.turmab.helpdesk.domain.Cliente;
import com.turmab.helpdesk.domain.Tecnico;
import com.turmab.helpdesk.domain.enums.Perfil;
import com.turmab.helpdesk.domain.enums.Prioridade;
import com.turmab.helpdesk.domain.enums.Status;
import com.turmab.helpdesk.repositories.ChamadoRepository;
import com.turmab.helpdesk.repositories.ClienteRepository;
import com.turmab.helpdesk.repositories.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Serviço responsável por popular o banco de dados com dados de teste.
 * Esta classe é útil para ambientes de desenvolvimento e teste, garantindo que a aplicação
 * inicie com um conjunto de dados predefinido.
 * A ativação deste serviço é geralmente controlada por perfis (profiles) do Spring.
 *
 * @author Seu Nome
 * @version 1.1
 * @since 2025-10-02
 */
@Service
public class DBService {

    @Autowired
    private TecnicoRepository tecnicoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ChamadoRepository chamadoRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Injetado para criptografar senhas

    /**
     * Instancia e salva um conjunto inicial de dados no banco de dados.
     * Cria técnicos, clientes e chamados com perfis e senhas criptografadas.
     * Este método deve ser chamado na inicialização da aplicação em perfis de desenvolvimento.
     */
    public void instanciaDB() {
        // Criação de Técnicos
        Tecnico tec1 = new Tecnico(null, "Bill Gates", "76045777093", "bill@mail.com", passwordEncoder.encode("123"));
        tec1.addPerfil(Perfil.ADMIN); // Bill Gates é um técnico e também admin

        Tecnico tec2 = new Tecnico(null, "Steve Jobs", "98765432100", "steve@mail.com", passwordEncoder.encode("123"));
        tec2.addPerfil(Perfil.TECNICO); // Steve Jobs é apenas técnico

        // Criação de Clientes
        Cliente cli1 = new Cliente(null, "Linus Torvalds", "70511744013", "linus@mail.com", passwordEncoder.encode("123"));
        cli1.addPerfil(Perfil.CLIENTE); // Perfil padrão de cliente

        Cliente cli2 = new Cliente(null, "Ada Lovelace", "12345678901", "ada@mail.com", passwordEncoder.encode("123"));
        cli2.addPerfil(Perfil.CLIENTE);

        // Criação de Chamados
        Chamado cha1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01", "Primeiro chamado de teste", tec1, cli1);
        Chamado cha2 = new Chamado(null, Prioridade.ALTA, Status.ABERTO, "Chamado 02", "Impressora não funciona", tec2, cli2);

        // Salvando os dados no banco
        tecnicoRepository.saveAll(Arrays.asList(tec1, tec2));
        clienteRepository.saveAll(Arrays.asList(cli1, cli2));
        chamadoRepository.saveAll(Arrays.asList(cha1, cha2));
    }
}