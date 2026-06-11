package br.com.controlechuvas.mapper;

import br.com.controlechuvas.dto.SolicitacaoRequestDTO;
import br.com.controlechuvas.dto.SolicitacaoResponseDTO;
import br.com.controlechuvas.model.Solicitacao;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoMapper {

    public Solicitacao paraEntidade(SolicitacaoRequestDTO dto) {
        return Solicitacao.builder()
                .nomeSolicitante(dto.getNomeSolicitante())
                .rg(dto.getRg())
                .cpf(dto.getCpf())
                .endereco(dto.getEndereco())
                .categoria(dto.getCategoria())
                .descricao(dto.getDescricao())
                .build();
    }

    public SolicitacaoResponseDTO paraResposta(Solicitacao solicitacao) {
        return SolicitacaoResponseDTO.builder()
                .id(solicitacao.getId())
                .nomeSolicitante(solicitacao.getNomeSolicitante())
                .rg(solicitacao.getRg())
                .cpf(solicitacao.getCpf())
                .endereco(solicitacao.getEndereco())
                .categoria(solicitacao.getCategoria())
                .descricao(solicitacao.getDescricao())
                .status(solicitacao.getStatus())
                .dataCriacao(solicitacao.getDataCriacao())
                .build();
    }
}
