package com.turmab.helpdesk.config;

import com.turmab.helpdesk.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuração específica para o perfil de desenvolvimento.
 * Responsável por instanciar o banco de dados com dados de teste.
 */
@Configuration
@Profile("dev") // <-- Esta configuração só será ativada quando o perfil 'dev' estiver ativo
public class DevConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public void instanciaDB() {
        this.dbService.instanciaDB();
    }
}