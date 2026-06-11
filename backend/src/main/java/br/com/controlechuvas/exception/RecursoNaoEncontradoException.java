package br.com.controlechuvas.exception;

import java.io.Serial;

public class RecursoNaoEncontradoException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
