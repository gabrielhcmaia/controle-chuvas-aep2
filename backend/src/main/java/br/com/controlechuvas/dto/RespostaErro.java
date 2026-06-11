package br.com.controlechuvas.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespostaErro {

    private LocalDateTime dataHora;
    private int status;
    private String erro;
    private String mensagem;
}
