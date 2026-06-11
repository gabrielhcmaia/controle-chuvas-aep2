import { Component, OnInit, inject } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { SolicitacaoService } from '../../core/services/solicitacao.service';
import { PerfilUsuario, UsuarioService } from '../../core/services/usuario.service';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit {
  private readonly usuarioService = inject(UsuarioService);
  private readonly solicitacaoService = inject(SolicitacaoService);
  private readonly construtorDeFormulario = inject(NonNullableFormBuilder);

  protected perfil: PerfilUsuario = this.usuarioService.obterPerfil();
  protected totalSolicitacoes = 0;
  protected editando = false;

  protected readonly formularioEdicao = this.construtorDeFormulario.group({
    nome: ['', [Validators.required, Validators.minLength(3)]],
    rg: ['', Validators.required],
    cpf: ['', Validators.required],
    endereco: ['', Validators.required],
    profissao: ['', Validators.required]
  });

  ngOnInit(): void {
    this.contarSolicitacoesDoUsuario();
  }

  protected get iniciais(): string {
    return this.perfil.nome
      .split(' ')
      .filter((parte) => parte.length > 0)
      .slice(0, 2)
      .map((parte) => parte.charAt(0))
      .join('')
      .toUpperCase();
  }

  protected iniciarEdicao(): void {
    this.formularioEdicao.setValue({ ...this.perfil });
    this.editando = true;
  }

  protected cancelarEdicao(): void {
    this.editando = false;
  }

  protected salvarEdicao(): void {
    if (this.formularioEdicao.invalid) {
      this.formularioEdicao.markAllAsTouched();
      return;
    }
    this.usuarioService.atualizarPerfil(this.formularioEdicao.getRawValue());
    this.perfil = this.usuarioService.obterPerfil();
    this.editando = false;
  }

  private contarSolicitacoesDoUsuario(): void {
    this.solicitacaoService.listar().subscribe({
      next: (lista) => {
        this.totalSolicitacoes = lista.filter(
          (solicitacao) => solicitacao.nomeSolicitante === this.perfil.nome
        ).length;
      },
      error: () => {
        this.totalSolicitacoes = 0;
      }
    });
  }
}
