package com.turmab.helpdesk.StartupMessage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupMessage implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n*********************************************");
        System.out.println("* APLICAÇÃO HELPDESK INICIADA COM SUCESSO!  *");
        System.out.println("*********************************************\n");
    }
}