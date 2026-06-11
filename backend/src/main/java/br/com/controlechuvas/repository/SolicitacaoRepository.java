package br.com.controlechuvas.repository;

import br.com.controlechuvas.model.CategoriaIncidente;
import br.com.controlechuvas.model.Solicitacao;
import br.com.controlechuvas.model.StatusSolicitacao;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import org.springframework.stereotype.Repository;

@Repository
public class SolicitacaoRepository {

    private final Map<Long, Solicitacao> banco = new ConcurrentHashMap<>();
    private final AtomicLong geradorDeId = new AtomicLong(0);

    public Solicitacao salvar(Solicitacao solicitacao) {
        if (solicitacao.getId() == null) {
            solicitacao.setId(geradorDeId.incrementAndGet());
        }
        banco.put(solicitacao.getId(), solicitacao);
        return solicitacao;
    }

    public Optional<Solicitacao> buscarPorId(Long id) {
        return Optional.ofNullable(banco.get(id));
    }

    public List<Solicitacao> buscarTodas() {
        return filtrar(solicitacao -> true);
    }

    public List<Solicitacao> buscarPorStatus(StatusSolicitacao status) {
        return filtrar(solicitacao -> solicitacao.getStatus() == status);
    }

    public List<Solicitacao> buscarPorCategoria(CategoriaIncidente categoria) {
        return filtrar(solicitacao -> solicitacao.getCategoria() == categoria);
    }

    public List<Solicitacao> buscarPorStatusECategoria(StatusSolicitacao status, CategoriaIncidente categoria) {
        return filtrar(solicitacao -> solicitacao.getStatus() == status
                && solicitacao.getCategoria() == categoria);
    }

    private List<Solicitacao> filtrar(Predicate<Solicitacao> criterio) {
        return banco.values().stream()
                .filter(criterio)
                .sorted(Comparator.comparing(Solicitacao::getDataCriacao)
                        .thenComparing(Solicitacao::getId)
                        .reversed())
                .toList();
    }
}
