import { Component, OnInit, inject } from '@angular/core';

import {
  ICONES_CATEGORIA,
  ROTULOS_CATEGORIA,
  Solicitacao,
  StatusSolicitacao
} from '../../core/models/solicitacao.model';
import { SolicitacaoService } from '../../core/services/solicitacao.service';

interface MarcadorMapa {
  solicitacao: Solicitacao;
  icone: string;
  esquerda: string;
  topo: string;
}

interface AlertaBairro {
  bairro: string;
  icone: string;
  mensagem: string;
}

const BAIRRO_PADRAO = 'Região Central';

@Component({
  selector: 'app-mapa',
  standalone: true,
  imports: [],
  templateUrl: './mapa.component.html',
  styleUrl: './mapa.component.css'
})
export class MapaComponent implements OnInit {
  private readonly solicitacaoService = inject(SolicitacaoService);

  protected readonly rotulosCategoria = ROTULOS_CATEGORIA;

  protected marcadores: MarcadorMapa[] = [];
  protected alertas: AlertaBairro[] = [];
  protected carregando = true;
  protected erroCarregamento = false;

  ngOnInit(): void {
    this.carregarOcorrencias();
  }

  private carregarOcorrencias(): void {
    this.solicitacaoService.listar().subscribe({
      next: (lista) => this.montarMapa(lista),
      error: () => {
        this.carregando = false;
        this.erroCarregamento = true;
      }
    });
  }

  private montarMapa(lista: Solicitacao[]): void {
    const ativas = lista.filter((solicitacao) => this.estaAtiva(solicitacao));
    this.marcadores = ativas.map((solicitacao) => this.criarMarcador(solicitacao));
    this.alertas = ativas.map((solicitacao) => this.criarAlerta(solicitacao));
    this.carregando = false;
  }

  private estaAtiva(solicitacao: Solicitacao): boolean {
    return (
      solicitacao.status === StatusSolicitacao.PENDENTE ||
      solicitacao.status === StatusSolicitacao.ENCAMINHADO
    );
  }

  private criarMarcador(solicitacao: Solicitacao): MarcadorMapa {
    const esquerda = 8 + ((solicitacao.id * 37) % 80);
    const topo = 12 + ((solicitacao.id * 53) % 68);
    return {
      solicitacao,
      icone: ICONES_CATEGORIA[solicitacao.categoria],
      esquerda: `${esquerda}%`,
      topo: `${topo}%`
    };
  }

  private criarAlerta(solicitacao: Solicitacao): AlertaBairro {
    return {
      bairro: this.extrairBairro(solicitacao.endereco),
      icone: ICONES_CATEGORIA[solicitacao.categoria],
      mensagem: solicitacao.descricao
    };
  }

  private extrairBairro(endereco: string): string {
    const partes = endereco.split(' - ');
    return partes.length > 1 ? partes[partes.length - 1].trim() : BAIRRO_PADRAO;
  }
}
