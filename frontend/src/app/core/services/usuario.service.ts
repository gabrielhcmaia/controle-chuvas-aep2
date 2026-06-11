import { Injectable } from '@angular/core';

export interface PerfilUsuario {
  nome: string;
  rg: string;
  cpf: string;
  endereco: string;
  profissao: string;
}

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private perfilAtual: PerfilUsuario = {
    nome: 'Gabriel Maia',
    rg: '12.345.678-9',
    cpf: '390.533.447-05',
    endereco: 'Av. Colombo, 5790 - Zona 07, Maringá-PR',
    profissao: 'Estudante de Engenharia de Software'
  };

  obterPerfil(): PerfilUsuario {
    return { ...this.perfilAtual };
  }

  atualizarPerfil(novosDados: PerfilUsuario): void {
    this.perfilAtual = { ...novosDados };
  }
}
