package br.com.controlechuvas.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {

    private Long id;
    private String nomeSolicitante;
    private String rg;
    private String cpf;
    private String endereco;
    private CategoriaIncidente categoria;
    private String descricao;
    private StatusSolicitacao status;
    private LocalDateTime dataCriacao;
}
