package br.com.controlechuvas.service;

import br.com.controlechuvas.dto.SolicitacaoRequestDTO;
import br.com.controlechuvas.dto.SolicitacaoResponseDTO;
import br.com.controlechuvas.exception.RecursoNaoEncontradoException;
import br.com.controlechuvas.exception.TransicaoDeStatusInvalidaException;
import br.com.controlechuvas.mapper.SolicitacaoMapper;
import br.com.controlechuvas.model.CategoriaIncidente;
import br.com.controlechuvas.model.Solicitacao;
import br.com.controlechuvas.model.StatusSolicitacao;
import br.com.controlechuvas.repository.SolicitacaoRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository repositorio;
    private final ValidadorSolicitacao validador;
    private final SolicitacaoMapper mapeador;

    public SolicitacaoResponseDTO criar(SolicitacaoRequestDTO dto) {
        validador.validar(dto);
        Solicitacao nova = mapeador.paraEntidade(dto);
        nova.setStatus(StatusSolicitacao.PENDENTE);
        nova.setDataCriacao(LocalDateTime.now());
        return mapeador.paraResposta(repositorio.salvar(nova));
    }

    public List<SolicitacaoResponseDTO> listar(StatusSolicitacao status, CategoriaIncidente categoria) {
        return buscarComFiltros(status, categoria).stream()
                .map(mapeador::paraResposta)
                .toList();
    }

    public SolicitacaoResponseDTO buscarPorId(Long id) {
        return mapeador.paraResposta(obterOuFalhar(id));
    }

    public SolicitacaoResponseDTO atualizarStatus(Long id, StatusSolicitacao novoStatus) {
        validador.validarNovoStatus(novoStatus);
        Solicitacao solicitacao = obterOuFalhar(id);
        if (!solicitacao.getStatus().podeTransitarPara(novoStatus)) {
            throw new TransicaoDeStatusInvalidaException(solicitacao.getStatus(), novoStatus);
        }
        solicitacao.setStatus(novoStatus);
        return mapeador.paraResposta(repositorio.salvar(solicitacao));
    }

    private List<Solicitacao> buscarComFiltros(StatusSolicitacao status, CategoriaIncidente categoria) {
        if (status != null && categoria != null) {
            return repositorio.buscarPorStatusECategoria(status, categoria);
        }
        if (status != null) {
            return repositorio.buscarPorStatus(status);
        }
        if (categoria != null) {
            return repositorio.buscarPorCategoria(categoria);
        }
        return repositorio.buscarTodas();
    }

    private Solicitacao obterOuFalhar(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Solicitação não encontrada para o id " + id + "."));
    }
}
