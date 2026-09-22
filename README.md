# Funcionários da Indústria - Backend

API REST desenvolvida em **Java Spring Boot** para o exercício de manipulação de funcionários (inserção, remoção, reajuste salarial, agrupamento por função, aniversariantes, ordenação, totais e cálculo de salários mínimos). Os dados são mantidos **em memória**, sem uso de banco de dados — ideal para fins didáticos e para que qualquer pessoa consiga rodar o projeto sem precisar instalar ou configurar um SGBD.

## Descrição

A aplicação modela uma hierarquia simples `Pessoa` → `Funcionario` e expõe um endpoint para cada item do exercício (3.1 a 3.12), além de um endpoint de reset que restaura a base ao estado original.

Os dados são carregados automaticamente na inicialização da aplicação (via `@PostConstruct`) e mantidos em uma lista em memória durante a execução — ou seja, qualquer alteração (remoção do "João", aplicação do aumento de 10%) persiste apenas enquanto a aplicação estiver rodando, e é perdida ao reiniciar (ou pode ser desfeita a qualquer momento via `/resetar`).

A maioria dos endpoints devolve os dados já formatados (data em `dd/MM/aaaa`, valores com separador de milhar em ponto e decimal em vírgula), exceto o item 3.1, que devolve os valores **brutos** — sem formatação — para refletir o estado real dos dados logo após a inserção.

## Stack Tecnológica

| Tecnologia | Finalidade |
|---|---|
| Java 17+ | Linguagem principal |
| Spring Boot | Framework da aplicação |
| Spring Web (MVC) | Camada REST |
| Maven (com Maven Wrapper) | Gerenciador de dependências e build |

Não há dependência de banco de dados, JPA, Flyway ou qualquer SGBD — todo o estado é mantido em uma lista Java (`List<Funcionario>`) dentro do `FuncionarioService`.

## Arquitetura do projeto

```
src/main/java/com/amaral/industria/
├── model/
│   ├── Pessoa.java                     # Classe base (nome, data de nascimento)
│   ├── Funcionario.java                # Estende Pessoa (salário, função)
│   ├── FuncionarioResponseDTO.java     # DTO com dados formatados (data e salário)
│   └── FuncionarioValoresBrutosDTO.java# DTO com valores brutos (usado só no item 3.1)
├── service/
│   └── FuncionarioService.java         # Regras de negócio, estado em memória, seed inicial
├── controller/
│   └── FuncionarioController.java      # Endpoints REST, um por item do exercício
├── util/
│   └── FormatUtil.java                 # Formatação de datas e valores monetários (pt-BR)
├── config/
│   └── CorsConfig.java                 # Liberação de CORS para o frontend (Vue)
└── IndustriaApplication.java           # Classe principal (ponto de entrada)
```

## Pré-requisitos

- JDK 17 ou superior
- Não é necessário instalar o Maven — o projeto já inclui o **Maven Wrapper** (`mvnw` / `mvnw.cmd`)
- Não é necessário instalar nenhum banco de dados

## Como executar

1. Execute a aplicação usando o Maven Wrapper, no Prompt de Comando (cmd):
   ```
   mvnw.cmd spring-boot:run
   ```

   > Se estiver usando o **PowerShell** em vez do cmd, o comando precisa do prefixo `.\`:
   > ```
   > .\mvnw.cmd spring-boot:run
   > ```

2. Ao subir, a aplicação já inicializa automaticamente os 10 funcionários em memória (com "João" presente e sem nenhum aumento aplicado).

3. A API estará disponível em:
   ```
   http://localhost:8080/api/funcionarios
   ```

## Endpoints disponíveis

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/funcionarios/resetar` | Restaura a lista ao estado original (10 funcionários, com João, sem aumento) |
| GET | `/api/funcionarios/3-1-inserir` | Estado atual da lista, com valores **brutos** (sem formatação) |
| POST | `/api/funcionarios/3-2-remover-joao` | Remove o funcionário "João" (idempotente) |
| GET | `/api/funcionarios/3-3-todos` | Lista todos os funcionários, formatados |
| POST | `/api/funcionarios/3-4-aplicar-aumento` | Aplica 10% de aumento salarial (apenas uma vez; cliques seguintes não acumulam) |
| GET | `/api/funcionarios/3-5-agrupar-resumo` | Resumo: função → quantidade de funcionários |
| GET | `/api/funcionarios/3-6-agrupados` | Funcionários agrupados por função, com dados completos e formatados |
| GET | `/api/funcionarios/3-8-aniversariantes` | Aniversariantes dos meses 10 (outubro) e 12 (dezembro) |
| GET | `/api/funcionarios/3-9-mais-velho` | Nome e idade do funcionário mais velho |
| GET | `/api/funcionarios/3-10-ordenados` | Lista em ordem alfabética por nome |
| GET | `/api/funcionarios/3-11-total-salarios` | Soma total dos salários, formatada |
| GET | `/api/funcionarios/3-12-salarios-minimos` | Quantos salários mínimos cada funcionário recebe (salário mínimo de referência: R$ 1.212,00) |

## Formatação de dados

Toda formatação de data e valores monetários é centralizada em `FormatUtil`:

- **Datas**: formato `dd/MM/aaaa`
- **Valores monetários**: separador de milhar como ponto e decimal como vírgula (padrão `pt-BR`), ex: `19.119,88`

Essa formatação é aplicada nos DTOs de resposta (`FuncionarioResponseDTO`), exceto no item 3.1, que utiliza `FuncionarioValoresBrutosDTO` para expor os valores sem formatação de salário (apenas a data vem formatada nesse DTO, como referência visual; o salário permanece como `BigDecimal` puro).

## CORS

O CORS já está configurado em `CorsConfig` para aceitar requisições vindas do frontend Vue, rodando em `http://localhost:5173`. Se o frontend rodar em outra porta, ajuste `allowedOrigins` nesse arquivo.

## Observação sobre persistência

Como os dados vivem apenas em memória, **reiniciar a aplicação** (parar e rodar `mvnw.cmd spring-boot:run` novamente) também restaura tudo ao estado original — o mesmo efeito do endpoint `/resetar`, só que reiniciando o processo inteiro.