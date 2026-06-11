package br.com.controlechuvas.exception;

import java.io.Serial;

public class DadosInvalidosException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }
}
