package br.com.controlechuvas.dto;

import br.com.controlechuvas.model.CategoriaIncidente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoRequestDTO {

    private String nomeSolicitante;
    private String rg;
    private String cpf;
    private String endereco;
    private CategoriaIncidente categoria;
    private String descricao;
}
