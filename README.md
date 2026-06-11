# 🌧️ Controle de Chuvas — Maringá-PR (Versão Beta)

Sistema de gestão de incidentes climáticos em tempo real para a cidade de Maringá-PR,
desenvolvido como entrega acadêmica da disciplina de Engenharia de Software.

## Perfis de usuário

| Perfil                                      | Telas principais                                                             |
| ------------------------------------------- | ---------------------------------------------------------------------------- |
| **Cidadão**                                 | Dashboard, Mapa, Nova Solicitação, Perfil                                    |
| **Servidor Público / Gestor**               | Painel Administrativo (triagem e encaminhamento)                             |
| **Profissional de Ajuda / Equipe de Campo** | Recebe as solicitações encaminhadas (fluxo concluído via Painel ADM na Beta) |

## Arquitetura

```
controle-chuvas/
├── backend/    → API REST em Java 17 + Spring Boot 3.5 (arquitetura em camadas)
│   └── src/main/java/br/com/controlechuvas/
│       ├── controller/   → SolicitacaoController (apenas roteamento HTTP)
│       ├── service/      → SolicitacaoService + ValidadorSolicitacao (regras de negócio)
│       ├── repository/   → SolicitacaoRepository (persistência simulada em memória)
│       ├── mapper/       → SolicitacaoMapper (conversão entidade ↔ DTO)
│       ├── dto/          → contratos de entrada/saída da API
│       ├── model/        → Solicitacao, StatusSolicitacao, CategoriaIncidente
│       ├── exception/    → exceções customizadas + @RestControllerAdvice
│       ├── config/       → CORS e carga de dados de demonstração
│       └── util/         → ValidadorCpf (algoritmo de dígitos verificadores)
│
└── frontend/   → SPA em Angular 17 (componentes standalone + Reactive Forms)
    └── src/app/
        ├── core/
        │   ├── models/    → interfaces e enums espelhando os modelos Java
        │   └── services/  → SolicitacaoService (HttpClient) e UsuarioService (perfil mockado)
        └── features/
            ├── dashboard/         → cards recentes + painel de informações úteis
            ├── mapa/              → mapa simulado com marcadores + alertas por bairro
            ├── nova-solicitacao/  → formulário reativo validado
            ├── perfil/            → dados formais, contador e edição do perfil
            └── painel-admin/      → triagem com CONCLUIR / ENCAMINHAR / CANCELAR
```

## Como executar

### Backend (porta 8080) — requer JDK 17+ e Maven

```bash
cd backend
mvn spring-boot:run
```

A API sobe com 5 solicitações de exemplo pré-carregadas em memória.

### Frontend (porta 4200) — requer Node 18+

```bash
cd frontend
npm install
npm start
```

Acesse **http://localhost:4200**.

## Endpoints da API

| Método  | Rota                            | Descrição                                                  |
| ------- | ------------------------------- | ---------------------------------------------------------- |
| `POST`  | `/api/solicitacoes`             | Cria uma solicitação (status inicial `PENDENTE`)           |
| `GET`   | `/api/solicitacoes`             | Lista todas (filtros opcionais `?status=` e `?categoria=`) |
| `GET`   | `/api/solicitacoes/{id}`        | Detalha uma solicitação                                    |
| `PATCH` | `/api/solicitacoes/{id}/status` | Atualiza o status (`{"status": "CONCLUIDO"}`)              |

**Ciclo de vida:** `PENDENTE → ENCAMINHADO → CONCLUIDO`, com `CANCELADO` possível a partir
de `PENDENTE` ou `ENCAMINHADO`. Estados `CONCLUIDO` e `CANCELADO` são terminais — transições
inválidas retornam **HTTP 409** com mensagem explicativa.

## Limitações conhecidas da Beta

- Persistência simulada em memória (os dados são reiniciados a cada execução).
- Sem autenticação: o perfil do cidadão é mockado e o Painel ADM é de acesso livre.
- Previsão do tempo, região mais afetada e posições no mapa são simuladas no cliente.

## Qualidade de código

As decisões de Clean Code adotadas para zerar apontamentos de SonarQube, PMD,
Checkstyle e SpotBugs estão documentadas em [`docs/RELATORIO-CLEAN-CODE.md`](docs/RELATORIO-CLEAN-CODE.md).
