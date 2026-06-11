package br.com.controlechuvas.exception;

import br.com.controlechuvas.dto.RespostaErro;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class TratadorGlobalDeExcecoes {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<RespostaErro> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException excecao) {
        return construirResposta(HttpStatus.NOT_FOUND, excecao.getMessage());
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<RespostaErro> tratarDadosInvalidos(DadosInvalidosException excecao) {
        return construirResposta(HttpStatus.BAD_REQUEST, excecao.getMessage());
    }

    @ExceptionHandler(TransicaoDeStatusInvalidaException.class)
    public ResponseEntity<RespostaErro> tratarTransicaoInvalida(TransicaoDeStatusInvalidaException excecao) {
        return construirResposta(HttpStatus.CONFLICT, excecao.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RespostaErro> tratarParametroInvalido(MethodArgumentTypeMismatchException excecao) {
        return construirResposta(HttpStatus.BAD_REQUEST,
                "Valor inválido para o parâmetro '" + excecao.getName() + "'.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespostaErro> tratarCorpoIlegivel(HttpMessageNotReadableException excecao) {
        log.warn("Corpo de requisição rejeitado pelo conversor JSON.", excecao);
        return construirResposta(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido. Verifique os campos e valores enviados.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespostaErro> tratarErroInesperado(Exception excecao) {
        log.error("Erro inesperado ao processar a requisição.", excecao);
        return construirResposta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado. Tente novamente mais tarde.");
    }

    private ResponseEntity<RespostaErro> construirResposta(HttpStatus status, String mensagem) {
        log.warn("Requisição rejeitada ({}): {}", status.value(), mensagem);
        RespostaErro corpo = RespostaErro.builder()
                .dataHora(LocalDateTime.now())
                .status(status.value())
                .erro(status.getReasonPhrase())
                .mensagem(mensagem)
                .build();
        return ResponseEntity.status(status).body(corpo);
    }
}
