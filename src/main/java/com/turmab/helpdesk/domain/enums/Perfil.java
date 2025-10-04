package com.turmab.helpdesk.domain.enums;

/**
 * Enum que representa os perfis (roles) de usuário no sistema.
 * Cada perfil tem um código numérico e uma descrição textual.
 */
public enum Perfil {

    // CORREÇÃO: Descrição sem o prefixo "ROLE_"
    ADMIN(0, "ADMIN"), 
    CLIENTE(1, "CLIENTE"), 
    TECNICO(2, "TECNICO");

    private Integer codigo;
    private String descricao;

    Perfil(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Converte um código numérico para o Enum Perfil correspondente.
     * @param cod O código do perfil (0, 1 ou 2).
     * @return O Enum Perfil correspondente.
     * @throws IllegalArgumentException Se o código fornecido não for válido.
     */
    public static Perfil toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (Perfil x : Perfil.values()) {
            if (cod.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Perfil inválido: " + cod);
    }
}