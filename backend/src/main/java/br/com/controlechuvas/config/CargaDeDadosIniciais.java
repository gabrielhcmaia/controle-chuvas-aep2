package br.com.controlechuvas.config;

import br.com.controlechuvas.model.CategoriaIncidente;
import br.com.controlechuvas.model.Solicitacao;
import br.com.controlechuvas.model.StatusSolicitacao;
import br.com.controlechuvas.repository.SolicitacaoRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CargaDeDadosIniciais implements CommandLineRunner {

    private final SolicitacaoRepository repositorio;

    @Override
    public void run(String... argumentos) {
        if (!repositorio.buscarTodas().isEmpty()) {
            return;
        }
        solicitacoesDeExemplo().forEach(repositorio::salvar);
        log.info("Carga inicial concluída: {} solicitações de exemplo registradas.",
                repositorio.buscarTodas().size());
    }

    private List<Solicitacao> solicitacoesDeExemplo() {
        LocalDateTime agora = LocalDateTime.now();
        return List.of(
                Solicitacao.builder()
                        .nomeSolicitante("Ana Beatriz Souza")
                        .rg("10.234.567-8")
                        .cpf("111.444.777-35")
                        .endereco("Rua Néo Alves Martins, 2787 - Zona 07")
                        .categoria(CategoriaIncidente.ARVORE_CAIDA)
                        .descricao("Árvore caída interditando a rua após o temporal da madrugada.")
                        .status(StatusSolicitacao.PENDENTE)
                        .dataCriacao(agora.minusMinutes(40))
                        .build(),
                Solicitacao.builder()
                        .nomeSolicitante("Carlos Eduardo Lima")
                        .rg("9.876.543-2")
                        .cpf("529.982.247-25")
                        .endereco("Av. Duque de Caxias, 1500 - Centro")
                        .categoria(CategoriaIncidente.ALAGAMENTO)
                        .descricao("Alagamento intenso na avenida, veículos ilhados próximo ao terminal.")
                        .status(StatusSolicitacao.PENDENTE)
                        .dataCriacao(agora.minusHours(2))
                        .build(),
                Solicitacao.builder()
                        .nomeSolicitante("Mariana Castro")
                        .rg("8.123.456-7")
                        .cpf("390.533.447-05")
                        .endereco("Av. Mandacaru, 980 - Zona 05")
                        .categoria(CategoriaIncidente.QUEDA_DE_ENERGIA)
                        .descricao("Bairro sem energia elétrica desde o início da chuva forte.")
                        .status(StatusSolicitacao.ENCAMINHADO)
                        .dataCriacao(agora.minusHours(3))
                        .build(),
                Solicitacao.builder()
                        .nomeSolicitante("João Pereira")
                        .rg("7.654.321-0")
                        .cpf("111.444.777-35")
                        .endereco("Av. Colombo, 3700 - Zona 08")
                        .categoria(CategoriaIncidente.TRANSITO)
                        .descricao("Semáforos apagados causando lentidão e risco de colisão no cruzamento.")
                        .status(StatusSolicitacao.PENDENTE)
                        .dataCriacao(agora.minusMinutes(15))
                        .build(),
                Solicitacao.builder()
                        .nomeSolicitante("Fernanda Oliveira")
                        .rg("6.543.210-9")
                        .cpf("529.982.247-25")
                        .endereco("Av. Tuiuti, 458 - Vila Morangueira")
                        .categoria(CategoriaIncidente.ACIDENTE)
                        .descricao("Colisão entre dois veículos na pista alagada, sem vítimas graves.")
                        .status(StatusSolicitacao.CONCLUIDO)
                        .dataCriacao(agora.minusHours(5))
                        .build()
        );
    }
}
