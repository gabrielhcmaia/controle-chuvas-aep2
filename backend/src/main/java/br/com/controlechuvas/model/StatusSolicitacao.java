package br.com.controlechuvas.model;

import java.util.Map;
import java.util.Set;

public enum StatusSolicitacao {

    PENDENTE,
    ENCAMINHADO,
    CONCLUIDO,
    CANCELADO;

    private static final Map<StatusSolicitacao, Set<StatusSolicitacao>> TRANSICOES_VALIDAS = Map.of(
            PENDENTE, Set.of(ENCAMINHADO, CONCLUIDO, CANCELADO),
            ENCAMINHADO, Set.of(CONCLUIDO, CANCELADO),
            CONCLUIDO, Set.of(),
            CANCELADO, Set.of()
    );

    public boolean podeTransitarPara(StatusSolicitacao novoStatus) {
        return TRANSICOES_VALIDAS.get(this).contains(novoStatus);
    }
}
