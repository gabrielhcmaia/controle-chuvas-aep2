import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import {
  ICONES_CATEGORIA,
  ROTULOS_CATEGORIA,
  ROTULOS_STATUS,
  Solicitacao,
  StatusSolicitacao,
  classeDoBadge
} from '../../core/models/solicitacao.model';
import { SolicitacaoService } from '../../core/services/solicitacao.service';

@Component({
  selector: 'app-painel-admin',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './painel-admin.component.html',
  styleUrl: './painel-admin.component.css'
})
export class PainelAdminComponent implements OnInit {
  private readonly solicitacaoService = inject(SolicitacaoService);
  private readonly rota = inject(ActivatedRoute);

  protected readonly iconesCategoria = ICONES_CATEGORIA;
  protected readonly rotulosCategoria = ROTULOS_CATEGORIA;
  protected readonly rotulosStatus = ROTULOS_STATUS;
  protected readonly classeDoBadge = classeDoBadge;

  protected solicitacoes: Solicitacao[] = [];
  protected selecionada: Solicitacao | null = null;
  protected carregando = true;
  protected atualizando = false;
  protected mensagemFeedback = '';
  protected feedbackErro = false;

  ngOnInit(): void {
    this.carregarSolicitacoes();
  }

  protected get acoesDisponiveis(): boolean {
    return (
      this.selecionada !== null &&
      this.selecionada.status !== StatusSolicitacao.CONCLUIDO &&
      this.selecionada.status !== StatusSolicitacao.CANCELADO
    );
  }

  protected get podeEncaminhar(): boolean {
    return this.acoesDisponiveis && this.selecionada?.status !== StatusSolicitacao.ENCAMINHADO;
  }

  protected selecionar(solicitacao: Solicitacao): void {
    this.selecionada = solicitacao;
    this.mensagemFeedback = '';
  }

  protected concluir(): void {
    this.mudarStatus(StatusSolicitacao.CONCLUIDO);
  }

  protected encaminhar(): void {
    this.mudarStatus(StatusSolicitacao.ENCAMINHADO);
  }

  protected cancelar(): void {
    this.mudarStatus(StatusSolicitacao.CANCELADO);
  }

  private carregarSolicitacoes(): void {
    this.solicitacaoService.listar().subscribe({
      next: (lista) => {
        this.solicitacoes = lista;
        this.carregando = false;
        this.selecionarPelaRota();
      },
      error: () => {
        this.carregando = false;
        this.feedbackErro = true;
        this.mensagemFeedback = 'Não foi possível carregar as solicitações.';
      }
    });
  }

  private selecionarPelaRota(): void {
    const parametroId = this.rota.snapshot.paramMap.get('id');
    if (!parametroId) {
      return;
    }
    const id = Number(parametroId);
    this.selecionada = this.solicitacoes.find((solicitacao) => solicitacao.id === id) ?? null;
  }

  private mudarStatus(novoStatus: StatusSolicitacao): void {
    if (!this.selecionada || this.atualizando) {
      return;
    }
    this.atualizando = true;
    this.solicitacaoService.atualizarStatus(this.selecionada.id, novoStatus).subscribe({
      next: (atualizada) => this.aplicarAtualizacao(atualizada),
      error: (erro: HttpErrorResponse) => this.exibirErro(erro)
    });
  }

  private aplicarAtualizacao(atualizada: Solicitacao): void {
    this.solicitacoes = this.solicitacoes.map((solicitacao) =>
      solicitacao.id === atualizada.id ? atualizada : solicitacao
    );
    this.selecionada = atualizada;
    this.atualizando = false;
    this.feedbackErro = false;
    this.mensagemFeedback = `Status atualizado para ${ROTULOS_STATUS[atualizada.status]}.`;
  }

  private exibirErro(erro: HttpErrorResponse): void {
    this.atualizando = false;
    this.feedbackErro = true;
    this.mensagemFeedback = erro.error?.mensagem ?? 'Não foi possível atualizar o status.';
  }
}
