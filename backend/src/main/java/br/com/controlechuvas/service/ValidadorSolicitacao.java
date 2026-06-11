package br.com.controlechuvas.service;

import br.com.controlechuvas.dto.SolicitacaoRequestDTO;
import br.com.controlechuvas.exception.DadosInvalidosException;
import br.com.controlechuvas.model.CategoriaIncidente;
import br.com.controlechuvas.model.StatusSolicitacao;
import br.com.controlechuvas.util.ValidadorCpf;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ValidadorSolicitacao {

    private static final Pattern NAO_DIGITO = Pattern.compile("\\D");
    private static final int TAMANHO_MINIMO_RG = 7;
    private static final int TAMANHO_MAXIMO_RG = 9;

    public void validar(SolicitacaoRequestDTO dto) {
        validarObrigatorio(dto.getNomeSolicitante(), "nome do solicitante");
        validarObrigatorio(dto.getEndereco(), "endereço");
        validarObrigatorio(dto.getDescricao(), "descrição");
        validarCategoria(dto.getCategoria());
        validarRg(dto.getRg());
        validarCpf(dto.getCpf());
    }

    public void validarNovoStatus(StatusSolicitacao novoStatus) {
        if (novoStatus == null) {
            throw new DadosInvalidosException("O novo status é obrigatório.");
        }
    }

    private void validarObrigatorio(String valor, String nomeDoCampo) {
        if (valor == null || valor.isBlank()) {
            throw new DadosInvalidosException("O campo " + nomeDoCampo + " é obrigatório.");
        }
    }

    private void validarCategoria(CategoriaIncidente categoria) {
        if (categoria == null) {
            throw new DadosInvalidosException("A categoria do incidente é obrigatória.");
        }
    }

    private void validarRg(String rg) {
        validarObrigatorio(rg, "RG");
        int quantidadeDeDigitos = NAO_DIGITO.matcher(rg).replaceAll("").length();
        if (quantidadeDeDigitos < TAMANHO_MINIMO_RG || quantidadeDeDigitos > TAMANHO_MAXIMO_RG) {
            throw new DadosInvalidosException("O RG informado é inválido.");
        }
    }

    private void validarCpf(String cpf) {
        validarObrigatorio(cpf, "CPF");
        if (!ValidadorCpf.ehValido(cpf)) {
            throw new DadosInvalidosException("O CPF informado é inválido.");
        }
    }
}
