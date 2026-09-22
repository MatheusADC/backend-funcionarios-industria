package com.amaral.industria.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.amaral.industria.util.FormatUtil;

public class FuncionarioValoresBrutosDTO {
    private String nome;
    private LocalDate dataNascimento;
    private String dataNascimentoFormatada;
    private BigDecimal salario;
    private String funcao;

    public FuncionarioValoresBrutosDTO(Funcionario funcionario) {
        this.nome = funcionario.getNome();
        this.dataNascimentoFormatada = FormatUtil.formatarData(funcionario.getDataNascimento());
        this.salario = funcionario.getSalario();
        this.funcao = funcionario.getFuncao();
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getDataNascimentoFormatada() {
        return dataNascimentoFormatada;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public String getFuncao() {
        return funcao;
    }
}
