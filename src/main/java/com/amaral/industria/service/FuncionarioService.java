package com.amaral.industria.service;

import com.amaral.industria.model.Funcionario;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    private final List<Funcionario> snapshotOriginal = new ArrayList<>();

    private final List<Funcionario> funcionarios = new ArrayList<>();

    private boolean aumentoAplicado = false;

    @PostConstruct
    public void init() {
        montarSnapshotOriginal();
        resetar();
    }

    private void montarSnapshotOriginal() {
        snapshotOriginal
                .add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        snapshotOriginal.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        snapshotOriginal
                .add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        snapshotOriginal
                .add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        snapshotOriginal
                .add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        snapshotOriginal
                .add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        snapshotOriginal
                .add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        snapshotOriginal.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        snapshotOriginal
                .add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        snapshotOriginal.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));
    }

    public List<Funcionario> resetar() {
        funcionarios.clear();
        for (Funcionario f : snapshotOriginal) {
            funcionarios.add(new Funcionario(f.getNome(), f.getDataNascimento(), f.getSalario(), f.getFuncao()));
        }
        aumentoAplicado = false;
        return listarTodos();
    }

    public List<Funcionario> listarTodos() {
        return new ArrayList<>(funcionarios);
    }

    public List<Funcionario> removerJoao() {
        funcionarios.removeIf(f -> f.getNome().equalsIgnoreCase("João"));
        return listarTodos();
    }

    public List<Funcionario> aplicarAumento() {
        if (!aumentoAplicado) {
            BigDecimal percentual = new BigDecimal("0.10");
            for (Funcionario f : funcionarios) {
                BigDecimal novoSalario = f.getSalario()
                        .add(f.getSalario().multiply(percentual))
                        .setScale(2, RoundingMode.HALF_UP);
                f.setSalario(novoSalario);
            }
            aumentoAplicado = true;
        }
        return listarTodos();
    }

    public boolean isAumentoAplicado() {
        return aumentoAplicado;
    }

    public Map<String, Long> agruparResumo() {
        return funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao, LinkedHashMap::new, Collectors.counting()));
    }

    public Map<String, List<Funcionario>> agruparPorFuncao() {
        return funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao, LinkedHashMap::new, Collectors.toList()));
    }

    public List<Funcionario> aniversariantesPorMeses(List<Integer> meses) {
        return funcionarios.stream()
                .filter(f -> meses.contains(f.getDataNascimento().getMonthValue()))
                .collect(Collectors.toList());
    }

    public Funcionario funcionarioMaisVelho() {
        return funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .orElse(null);
    }

    public int calcularIdade(Funcionario f) {
        return Period.between(f.getDataNascimento(), LocalDate.now()).getYears();
    }

    public List<Funcionario> ordenarPorNome() {
        return funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public BigDecimal totalSalarios() {
        return funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Map<String, BigDecimal> salariosMinimosPorFuncionario() {
        Map<String, BigDecimal> resultado = new LinkedHashMap<>();
        for (Funcionario f : funcionarios) {
            BigDecimal qtd = f.getSalario().divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
            resultado.put(f.getNome(), qtd);
        }
        return resultado;
    }

    public BigDecimal getSalarioMinimo() {
        return SALARIO_MINIMO;
    }
}