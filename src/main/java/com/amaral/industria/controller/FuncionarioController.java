package com.amaral.industria.controller;

import com.amaral.industria.model.Funcionario;
import com.amaral.industria.model.FuncionarioResponseDTO;
import com.amaral.industria.model.FuncionarioValoresBrutosDTO;
import com.amaral.industria.service.FuncionarioService;
import com.amaral.industria.util.FormatUtil;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @PostMapping("/resetar")
    public List<FuncionarioValoresBrutosDTO> resetar() {
        return toDtoResetList(service.resetar());
    }

    @GetMapping("/3-1-inserir")
    public List<FuncionarioValoresBrutosDTO> item31() {
        return service.listarTodos().stream()
                .map(FuncionarioValoresBrutosDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/3-2-remover-joao")
    public List<FuncionarioResponseDTO> item32() {
        return toDtoList(service.removerJoao());
    }

    @GetMapping("/3-3-todos")
    public List<FuncionarioResponseDTO> item33() {
        return toDtoList(service.listarTodos());
    }

    @PostMapping("/3-4-aplicar-aumento")
    public Map<String, Object> item34() {
        boolean jaEstavaAplicado = service.isAumentoAplicado();
        List<FuncionarioResponseDTO> lista = toDtoList(service.aplicarAumento());

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("funcionarios", lista);
        resposta.put("mensagem", jaEstavaAplicado
                ? "O aumento de 10% já havia sido aplicado anteriormente."
                : "Aumento de 10% aplicado com sucesso.");
        return resposta;
    }

    @GetMapping("/3-5-agrupar-resumo")
    public Map<String, Long> item35() {
        return service.agruparResumo();
    }

    @GetMapping("/3-6-agrupados")
    public Map<String, List<FuncionarioResponseDTO>> item36() {
        Map<String, List<Funcionario>> agrupado = service.agruparPorFuncao();
        Map<String, List<FuncionarioResponseDTO>> resultado = new LinkedHashMap<>();
        agrupado.forEach((funcao, lista) -> resultado.put(funcao, toDtoList(lista)));
        return resultado;
    }

    @GetMapping("/3-8-aniversariantes")
    public List<FuncionarioResponseDTO> item38() {
        return toDtoList(service.aniversariantesPorMeses(Arrays.asList(10, 12)));
    }

    @GetMapping("/3-9-mais-velho")
    public Map<String, Object> item39() {
        Funcionario f = service.funcionarioMaisVelho();
        Map<String, Object> resultado = new LinkedHashMap<>();
        if (f != null) {
            resultado.put("nome", f.getNome());
            resultado.put("idade", service.calcularIdade(f));
        }
        return resultado;
    }

    @GetMapping("/3-10-ordenados")
    public List<FuncionarioResponseDTO> item310() {
        return toDtoList(service.ordenarPorNome());
    }

    @GetMapping("/3-11-total-salarios")
    public Map<String, String> item311() {
        BigDecimal total = service.totalSalarios();
        Map<String, String> resultado = new LinkedHashMap<>();
        resultado.put("totalFormatado", FormatUtil.formatarValor(total));
        return resultado;
    }

    @GetMapping("/3-12-salarios-minimos")
    public Map<String, String> item312() {
        Map<String, BigDecimal> mapa = service.salariosMinimosPorFuncionario();
        Map<String, String> resultado = new LinkedHashMap<>();
        mapa.forEach((nome, qtd) -> resultado.put(nome, FormatUtil.formatarValor(qtd)));
        return resultado;
    }

    private List<FuncionarioResponseDTO> toDtoList(List<Funcionario> lista) {
        return lista.stream().map(FuncionarioResponseDTO::new).collect(Collectors.toList());
    }

    private List<FuncionarioValoresBrutosDTO> toDtoResetList(List<Funcionario> lista) {
        return lista.stream().map(FuncionarioValoresBrutosDTO::new).collect(Collectors.toList());
    }
}
