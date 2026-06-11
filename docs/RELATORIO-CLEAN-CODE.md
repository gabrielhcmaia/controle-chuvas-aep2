# Relatório de Clean Code e Métricas — Controle de Chuvas (Beta)

Este documento mapeia cada diretriz de qualidade adotada no projeto para os pontos
exatos do código onde ela foi aplicada, servindo de base para o relatório de métricas
(SonarQube, PMD, Checkstyle e SpotBugs).

## 1. Responsabilidade Única (SRP)

| Classe | Responsabilidade única |
|---|---|
| `SolicitacaoController` | Apenas roteia HTTP e delega ao service. Nenhum `if` de negócio: os 4 métodos têm complexidade ciclomática 1. |
| `SolicitacaoService` | Orquestra as regras de negócio (status inicial, data de criação, ciclo de vida). |
| `ValidadorSolicitacao` | Concentra exclusivamente as validações de entrada (campos obrigatórios, RG, CPF, categoria). |
| `SolicitacaoMapper` | Só converte entidade ↔ DTO; o service não conhece detalhes de mapeamento. |
| `SolicitacaoRepository` | Só armazena e consulta dados (simulação thread-safe de persistência). |
| `TratadorGlobalDeExcecoes` | Único ponto de tradução de exceções para respostas HTTP. |
| `ValidadorCpf` | Classe utilitária `final` com construtor privado, dedicada ao módulo 11 do CPF. |

No frontend, a mesma separação: componentes cuidam apenas de apresentação,
`SolicitacaoService` centraliza todo o HTTP e `UsuarioService` é a única fonte do perfil.

## 2. Baixa complexidade ciclomática (guard clauses e early returns)

- `SolicitacaoService.buscarComFiltros(...)`: a combinação de filtros usa **retornos
  antecipados** em vez de `if/else` aninhados — quatro caminhos lineares e independentes.
- `ValidadorSolicitacao`: cada regra é um método-guarda pequeno (`validarObrigatorio`,
  `validarRg`, `validarCpf`) que lança exceção na primeira violação. Nenhum aninhamento.
- `StatusSolicitacao.podeTransitarPara(...)`: as transições válidas são **dados**
  (mapa imutável `Map.of` + `Set.of`), não lógica condicional — complexidade 1 e regra
  de negócio legível em uma única estrutura declarativa.
- `ValidadorCpf.ehValido(...)`: early returns para nulo, tamanho incorreto e dígitos
  repetidos antes do cálculo dos verificadores; um único laço, sem aninhamento.
- Frontend: `NovaSolicitacaoComponent.enviar()` usa guard clauses (formulário inválido →
  `markAllAsTouched` + `return`), e `MapaComponent` decompõe a montagem do mapa em
  métodos privados de uma responsabilidade cada (`criarMarcador`, `criarAlerta`,
  `extrairBairro`).

## 3. Tratamento de exceções

- **Nenhum** `catch (Exception e)` em código de negócio, nenhum `System.out.println`
  e nenhum `printStackTrace()` no projeto.
- Exceções customizadas com semântica HTTP clara:
  - `RecursoNaoEncontradoException` → 404
  - `DadosInvalidosException` → 400
  - `TransicaoDeStatusInvalidaException` → 409 (conflito de estado)
- `TratadorGlobalDeExcecoes` (`@RestControllerAdvice`) converte cada exceção em um corpo
  padronizado (`RespostaErro` com data/hora, status, erro e mensagem) e registra tudo via
  **SLF4J** (`@Slf4j`) com logging parametrizado (`log.warn("... {}", valor)`), incluindo
  o stack trace completo no handler de erro inesperado.
- Todas as exceções declaram `serialVersionUID` (`@Serial`), evitando o apontamento
  SE_NO_SERIALVERSIONID do SpotBugs.

## 4. Encapsulamento estrito

- Todos os atributos de entidades e DTOs são `private`; getters/setters, `equals`,
  `hashCode` e `toString` são gerados por Lombok (`@Data`, `@NoArgsConstructor`,
  `@AllArgsConstructor`, `@Builder`), eliminando boilerplate sem expor estado.
- `UsuarioService` (frontend) retorna **cópias** do perfil (`{ ...this.perfilAtual }`),
  impedindo mutação externa do estado interno.
- Campos de injeção (`repositorio`, `validador`, `mapeador`) são `private final` com
  injeção por construtor (`@RequiredArgsConstructor`) — sem `@Autowired` em campo.

## 5. Convenções de nomenclatura

- Classes em `PascalCase` (`SolicitacaoService`, `TratadorGlobalDeExcecoes`);
  métodos e variáveis em `camelCase` (`buscarComFiltros`, `novaSolicitacao`);
  constantes em `UPPER_SNAKE_CASE` (`TRANSICOES_VALIDAS`, `TAMANHO_CPF`, `CAMINHO_BASE`).
- Identificadores de domínio em português, espelhados 1:1 entre Java e TypeScript
  (ex.: enum `StatusSolicitacao` existe nas duas pontas com os mesmos literais).
- Sem números mágicos: limites de RG/CPF e o módulo 11 são constantes nomeadas;
  o caminho base da API é a constante `CAMINHO_BASE` reutilizada no header `Location`.

## 6. Concorrência e robustez do repositório simulado

- `ConcurrentHashMap` para armazenamento e `AtomicLong.incrementAndGet()` para geração
  de id — thread-safe sem blocos `synchronized` manuais.
- Um único ponto de iteração (`filtrar(Predicate)`) elimina duplicação entre as quatro
  consultas públicas (DRY) e garante ordenação consistente (mais recentes primeiro).
- `buscarPorId` retorna `Optional`, eliminando retorno de `null` (apontamento clássico
  de SonarQube/SpotBugs).

## 7. Boas práticas específicas do frontend

- **Reactive Forms** com validações declarativas (`Validators.required`, `minLength`)
  e feedback visual por campo; o estado de envio desabilita o botão (evita duplo POST).
- Tipagem estrita: `strict: true` + `strictTemplates` no `tsconfig`; interfaces
  espelham os modelos Java e as chamadas HTTP são todas genéricas
  (`http.get<Solicitacao[]>`), sem `any`.
- Componentes standalone com **lazy loading** (`loadComponent`) — módulos desacoplados
  e bundle inicial menor.
- Lógica fora dos templates: classes de badge calculadas pela função pura
  `classeDoBadge(status)`; rótulos e ícones em `Record`s constantes (sem `switch` em HTML).
- Estados terminais desabilitam os botões do Painel ADM no cliente, e o backend ainda
  valida a transição (defesa em profundidade — a regra de negócio nunca depende só da UI).

## 8. Decisões que evitam apontamentos das ferramentas

| Ferramenta / regra típica | Como foi evitada |
|---|---|
| Sonar S106 (uso de `System.out`) | Todo log via SLF4J (`@Slf4j`). |
| Sonar S1166 (exceção engolida) | Handlers logam a mensagem/stack trace antes de responder. |
| Sonar S3776 (complexidade cognitiva) | Early returns, métodos curtos, transições como dados. |
| Sonar S1118 / PMD UseUtilityClass | `ValidadorCpf` é `final` com construtor privado. |
| PMD AvoidDuplicateLiterals | Literais repetidos extraídos para constantes. |
| PMD/Sonar regex recompilada em loop | `Pattern` pré-compilado em constante (`NAO_DIGITO`). |
| Checkstyle MagicNumber | Números do algoritmo de CPF e limites de RG em constantes nomeadas. |
| SpotBugs SE_NO_SERIALVERSIONID | `serialVersionUID` declarado nas exceções. |
| SpotBugs NP (null dereference) | `Optional` no repositório; validações de nulo nos guards. |

## 9. Verificação

- `mvn -DskipTests package` → compila e empacota sem erros nem warnings de compilação.
- `ng build` (produção, `strictTemplates`) → compila sem erros de template/tipagem.
