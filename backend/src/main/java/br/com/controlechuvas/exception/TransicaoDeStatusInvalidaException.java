package br.com.controlechuvas.exception;

import br.com.controlechuvas.model.StatusSolicitacao;
import java.io.Serial;

public class TransicaoDeStatusInvalidaException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TransicaoDeStatusInvalidaException(StatusSolicitacao statusAtual, StatusSolicitacao novoStatus) {
        super("Transição de status inválida: " + statusAtual + " não pode ser alterado para " + novoStatus + ".");
    }
}
