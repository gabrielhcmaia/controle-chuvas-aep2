import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';

import {
  CategoriaIncidente,
  NovaSolicitacao,
  ROTULOS_CATEGORIA,
  Solicitacao
} from '../../core/models/solicitacao.model';
import { SolicitacaoService } from '../../core/services/solicitacao.service';
import { UsuarioService } from '../../core/services/usuario.service';

type CampoDoFormulario = 'nomeSolicitante' | 'endereco' | 'categoria' | 'descricao';

@Component({
  selector: 'app-nova-solicitacao',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './nova-solicitacao.component.html',
  styleUrl: './nova-solicitacao.component.css'
})
export class NovaSolicitacaoComponent {
  private readonly construtorDeFormulario = inject(NonNullableFormBuilder);
  private readonly solicitacaoService = inject(SolicitacaoService);
  private readonly usuarioService = inject(UsuarioService);

  protected readonly categorias = Object.values(CategoriaIncidente);
  protected readonly rotulosCategoria = ROTULOS_CATEGORIA;
  protected readonly perfil = this.usuarioService.obterPerfil();

  protected enviando = false;
  protected mensagemSucesso = '';
  protected mensagemErro = '';

  protected readonly formulario = this.construtorDeFormulario.group({
    nomeSolicitante: [this.perfil.nome, [Validators.required, Validators.minLength(3)]],
    endereco: ['', [Validators.required, Validators.minLength(5)]],
    categoria: ['' as CategoriaIncidente | '', Validators.required],
    descricao: ['', [Validators.required, Validators.minLength(10)]]
  });

  protected usarNomeDoPerfil(): void {
    this.formulario.patchValue({ nomeSolicitante: this.perfil.nome });
  }

  protected campoInvalido(campo: CampoDoFormulario): boolean {
    const controle = this.formulario.controls[campo];
    return controle.invalid && (controle.touched || controle.dirty);
  }

  protected enviar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }
    const valores = this.formulario.getRawValue();
    if (valores.categoria === '') {
      return;
    }
    this.enviando = true;
    this.mensagemErro = '';
    const novaSolicitacao: NovaSolicitacao = {
      nomeSolicitante: valores.nomeSolicitante,
      endereco: valores.endereco,
      categoria: valores.categoria,
      descricao: valores.descricao,
      rg: this.perfil.rg,
      cpf: this.perfil.cpf
    };
    this.solicitacaoService.criar(novaSolicitacao).subscribe({
      next: (criada) => this.tratarSucesso(criada),
      error: (erro: HttpErrorResponse) => this.tratarErro(erro)
    });
  }

  private tratarSucesso(criada: Solicitacao): void {
    this.enviando = false;
    this.mensagemSucesso = `Solicitação registrada com sucesso! Protocolo nº ${criada.id}.`;
    this.formulario.reset();
  }

  private tratarErro(erro: HttpErrorResponse): void {
    this.enviando = false;
    this.mensagemErro = erro.error?.mensagem ?? 'Não foi possível enviar a solicitação. Tente novamente.';
  }
}
