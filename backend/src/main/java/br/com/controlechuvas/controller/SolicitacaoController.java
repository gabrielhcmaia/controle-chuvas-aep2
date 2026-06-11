package br.com.controlechuvas.controller;

import br.com.controlechuvas.dto.AtualizacaoStatusDTO;
import br.com.controlechuvas.dto.SolicitacaoRequestDTO;
import br.com.controlechuvas.dto.SolicitacaoResponseDTO;
import br.com.controlechuvas.model.CategoriaIncidente;
import br.com.controlechuvas.model.StatusSolicitacao;
import br.com.controlechuvas.service.SolicitacaoService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SolicitacaoController.CAMINHO_BASE)
@RequiredArgsConstructor
public class SolicitacaoController {

    static final String CAMINHO_BASE = "/api/solicitacoes";

    private final SolicitacaoService servico;

    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criar(@RequestBody SolicitacaoRequestDTO dto) {
        SolicitacaoResponseDTO criada = servico.criar(dto);
        URI localizacao = URI.create(CAMINHO_BASE + "/" + criada.getId());
        return ResponseEntity.created(localizacao).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoResponseDTO>> listar(
            @RequestParam(required = false) StatusSolicitacao status,
            @RequestParam(required = false) CategoriaIncidente categoria) {
        return ResponseEntity.ok(servico.listar(status, categoria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servico.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SolicitacaoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizacaoStatusDTO dto) {
        return ResponseEntity.ok(servico.atualizarStatus(id, dto.getStatus()));
    }
}
