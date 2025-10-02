package com.turmab.helpdesk.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.turmab.helpdesk.domain.Chamado;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Objeto de Transferência de Dados (DTO) para a entidade Chamado.
 * Esta classe é usada para transferir dados de chamados entre o front-end e o back-end,
 * evitando a exposição direta das entidades do domínio e validando os dados de entrada.
 *
 * @author Seu Nome
 * @version 1.1
 * @since 2025-10-02
 */
public class ChamadoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID único do chamado.
     */
    private Integer id;

    /**
     * Data em que o chamado foi aberto. Preenchido automaticamente.
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataAbertura = LocalDate.now();

    /**
     * Data em que o chamado foi fechado.
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFechamento;

    /**
     * Código da prioridade do chamado (0-BAIXA, 1-MEDIA, 2-ALTA).
     */
    @NotNull(message = "O campo PRIORIDADE é requerido")
    private Integer prioridade;

    /**
     * Código do status do chamado (0-ABERTO, 1-ANDAMENTO, 2-ENCERRADO).
     */
    @NotNull(message = "O campo STATUS é requerido")
    private Integer status;

    /**
     * Título descritivo do chamado.
     */
    @NotNull(message = "O campo TÍTULO é requerido")
    private String titulo;

    /**
     * Observações e detalhes sobre o chamado.
     */
    @NotNull(message = "O campo OBSERVAÇÕES é requerido")
    private String observacoes;

    /**
     * ID do técnico associado ao chamado.
     */
    @NotNull(message = "O campo TÉCNICO é requerido")
    private Integer tecnico;

    /**
     * ID do cliente que abriu o chamado.
     */
    @NotNull(message = "O campo CLIENTE é requerido")
    private Integer cliente;

    /**
     * Nome do técnico associado (preenchido na resposta).
     */
    private String nomeTecnico;

    /**
     * Nome do cliente que abriu o chamado (preenchido na resposta).
     */
    private String nomeCliente;

    /**
     * Construtor padrão.
     */
    public ChamadoDTO() {
        super();
    }

    /**
     * Construtor que converte uma entidade Chamado em um ChamadoDTO.
     * Usado para preparar os dados a serem enviados como resposta para o cliente.
     *
     * @param obj A entidade Chamado a ser convertida.
     */
    public ChamadoDTO(Chamado obj) {
        this.id = obj.getId();
        this.dataAbertura = obj.getDataAbertura();
        this.dataFechamento = obj.getDataFechamento();
        this.prioridade = obj.getPrioridade().getCodigo();
        this.status = obj.getStatus().getCodigo();
        this.titulo = obj.getTitulo();
        this.observacoes = obj.getObservacoes();
        this.tecnico = obj.getTecnico().getId();
        this.cliente = obj.getCliente().getId();
        this.nomeTecnico = obj.getTecnico().getNome();
        this.nomeCliente = obj.getCliente().getNome();
    }

    // --- GETTERS E SETTERS ---

    /**
     * Obtém o ID do chamado.
     * @return O ID do chamado.
     */
    public Integer getId() { return id; }

    /**
     * Define o ID do chamado.
     * @param id O novo ID do chamado.
     */
    public void setId(Integer id) { this.id = id; }

    /**
     * Obtém a data de abertura do chamado.
     * @return A data de abertura.
     */
    public LocalDate getDataAbertura() { return dataAbertura; }

    /**
     * Define a data de abertura do chamado.
     * @param dataAbertura A nova data de abertura.
     */
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }

    /**
     * Obtém a data de fechamento do chamado.
     * @return A data de fechamento.
     */
    public LocalDate getDataFechamento() { return dataFechamento; }

    /**
     * Define a data de fechamento do chamado.
     * @param dataFechamento A nova data de fechamento.
     */
    public void setDataFechamento(LocalDate dataFechamento) { this.dataFechamento = dataFechamento; }

    /**
     * Obtém o código da prioridade do chamado.
     * @return O código da prioridade.
     */
    public Integer getPrioridade() { return prioridade; }

    /**
     * Define o código da prioridade do chamado.
     * @param prioridade O novo código da prioridade.
     */
    public void setPrioridade(Integer prioridade) { this.prioridade = prioridade; }

    /**
     * Obtém o código do status do chamado.
     * @return O código do status.
     */
    public Integer getStatus() { return status; }

    /**
     * Define o código do status do chamado.
     * @param status O novo código do status.
     */
    public void setStatus(Integer status) { this.status = status; }

    /**
     * Obtém o título do chamado.
     * @return O título do chamado.
     */
    public String getTitulo() { return titulo; }

    /**
     * Define o título do chamado.
     * @param titulo O novo título do chamado.
     */
    public void setTitulo(String titulo) { this.titulo = titulo; }

    /**
     * Obtém as observações do chamado.
     * @return As observações do chamado.
     */
    public String getObservacoes() { return observacoes; }

    /**
     * Define as observações do chamado.
     * @param observacoes As novas observações do chamado.
     */
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    /**
     * Obtém o ID do técnico.
     * @return O ID do técnico.
     */
    public Integer getTecnico() { return tecnico; }

    /**
     * Define o ID do técnico.
     * @param tecnico O novo ID do técnico.
     */
    public void setTecnico(Integer tecnico) { this.tecnico = tecnico; }

    /**
     * Obtém o ID do cliente.
     * @return O ID do cliente.
     */
    public Integer getCliente() { return cliente; }

    /**
     * Define o ID do cliente.
     * @param cliente O novo ID do cliente.
     */
    public void setCliente(Integer cliente) { this.cliente = cliente; }

    /**
     * Obtém o nome do técnico.
     * @return O nome do técnico.
     */
    public String getNomeTecnico() { return nomeTecnico; }

    /**
     * Define o nome do técnico.
     * @param nomeTecnico O novo nome do técnico.
     */
    public void setNomeTecnico(String nomeTecnico) { this.nomeTecnico = nomeTecnico; }

    /**
     * Obtém o nome do cliente.
     * @return O nome do cliente.
     */
    public String getNomeCliente() { return nomeCliente; }

    /**
     * Define o nome do cliente.
     * @param nomeCliente O novo nome do cliente.
     */
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
}