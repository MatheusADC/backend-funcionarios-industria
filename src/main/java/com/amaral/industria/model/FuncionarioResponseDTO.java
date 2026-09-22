package com.amaral.industria.model;

import com.amaral.industria.util.FormatUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FuncionarioResponseDTO {

    private String nome;
    private LocalDate dataNascimento;
    private String dataNascimentoFormatada;
    private BigDecimal salario;
    private String salarioFormatado;
    private String funcao;

    public FuncionarioResponseDTO(Funcionario f) {
        this.nome = f.getNome();
        this.dataNascimento = f.getDataNascimento();
        this.dataNascimentoFormatada = FormatUtil.formatarData(f.getDataNascimento());
        this.salario = f.getSalario();
        this.salarioFormatado = FormatUtil.formatarValor(f.getSalario());
        this.funcao = f.getFuncao();
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

    public String getSalarioFormatado() {
        return salarioFormatado;
    }

    public String getFuncao() {
        return funcao;
    }
}