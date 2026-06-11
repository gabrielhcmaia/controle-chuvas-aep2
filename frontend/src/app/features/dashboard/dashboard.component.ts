import { DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import {
  ICONES_CATEGORIA,
  ROTULOS_CATEGORIA,
  ROTULOS_STATUS,
  Solicitacao,
  StatusSolicitacao,
  classeDoBadge
} from '../../core/models/solicitacao.model';
import { SolicitacaoService } from '../../core/services/solicitacao.service';

const LIMITE_RECENTES = 6;

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DatePipe, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  private readonly solicitacaoService = inject(SolicitacaoService);

  protected readonly iconesCategoria = ICONES_CATEGORIA;
  protected readonly rotulosCategoria = ROTULOS_CATEGORIA;
  protected readonly rotulosStatus = ROTULOS_STATUS;
  protected readonly classeDoBadge = classeDoBadge;

  protected readonly previsaoTempo = 'Maringá-PR — Chuvoso, 22 °C';
  protected readonly regiaoMaisAfetada = 'Zona 7';
  protected readonly ruasAfetadas = ['Av. Brasil', 'Rua Joubert de Carvalho', 'Av. Mandacaru'];

  protected solicitacoesRecentes: Solicitacao[] = [];
  protected totalPendentes = 0;
  protected totalEncaminhadas = 0;
  protected totalConcluidas = 0;
  protected carregando = true;
  protected erroCarregamento = false;

  ngOnInit(): void {
    this.carregarSolicitacoes();
  }

  private carregarSolicitacoes(): void {
    this.solicitacaoService.listar().subscribe({
      next: (lista) => this.processarLista(lista),
      error: () => {
        this.carregando = false;
        this.erroCarregamento = true;
      }
    });
  }

  private processarLista(lista: Solicitacao[]): void {
    this.solicitacoesRecentes = lista.slice(0, LIMITE_RECENTES);
    this.totalPendentes = this.contarPorStatus(lista, StatusSolicitacao.PENDENTE);
    this.totalEncaminhadas = this.contarPorStatus(lista, StatusSolicitacao.ENCAMINHADO);
    this.totalConcluidas = this.contarPorStatus(lista, StatusSolicitacao.CONCLUIDO);
    this.carregando = false;
  }

  private contarPorStatus(lista: Solicitacao[], status: StatusSolicitacao): number {
    return lista.filter((solicitacao) => solicitacao.status === status).length;
  }
}
