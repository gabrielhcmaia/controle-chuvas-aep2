package br.com.controlechuvas.dto;

import br.com.controlechuvas.model.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoStatusDTO {

    private StatusSolicitacao status;
}
