
export enum StatusSolicitacao {
  PENDENTE = 'PENDENTE',
  ENCAMINHADO = 'ENCAMINHADO',
  CONCLUIDO = 'CONCLUIDO',
  CANCELADO = 'CANCELADO'
}

export enum CategoriaIncidente {
  ALAGAMENTO = 'ALAGAMENTO',
  TRANSITO = 'TRANSITO',
  ARVORE_CAIDA = 'ARVORE_CAIDA',
  ACIDENTE = 'ACIDENTE',
  QUEDA_DE_ENERGIA = 'QUEDA_DE_ENERGIA'
}

export interface Solicitacao {
  id: number;
  nomeSolicitante: string;
  rg: string;
  cpf: string;
  endereco: string;
  categoria: CategoriaIncidente;
  descricao: string;
  status: StatusSolicitacao;
  dataCriacao: string;
}

export interface NovaSolicitacao {
  nomeSolicitante: string;
  rg: string;
  cpf: string;
  endereco: string;
  categoria: CategoriaIncidente;
  descricao: string;
}

export interface FiltroSolicitacoes {
  status?: StatusSolicitacao;
  categoria?: CategoriaIncidente;
}

export const ROTULOS_STATUS: Record<StatusSolicitacao, string> = {
  [StatusSolicitacao.PENDENTE]: 'Pendente',
  [StatusSolicitacao.ENCAMINHADO]: 'Encaminhado',
  [StatusSolicitacao.CONCLUIDO]: 'Concluído',
  [StatusSolicitacao.CANCELADO]: 'Cancelado'
};

export const ROTULOS_CATEGORIA: Record<CategoriaIncidente, string> = {
  [CategoriaIncidente.ALAGAMENTO]: 'Alagamento',
  [CategoriaIncidente.TRANSITO]: 'Trânsito',
  [CategoriaIncidente.ARVORE_CAIDA]: 'Árvore caída',
  [CategoriaIncidente.ACIDENTE]: 'Acidente',
  [CategoriaIncidente.QUEDA_DE_ENERGIA]: 'Queda de energia'
};

export const ICONES_CATEGORIA: Record<CategoriaIncidente, string> = {
  [CategoriaIncidente.ALAGAMENTO]: '🌊',
  [CategoriaIncidente.TRANSITO]: '🚧',
  [CategoriaIncidente.ARVORE_CAIDA]: '🌳',
  [CategoriaIncidente.ACIDENTE]: '🚨',
  [CategoriaIncidente.QUEDA_DE_ENERGIA]: '⚡'
};

export function classeDoBadge(status: StatusSolicitacao): string {
  return `badge badge-${status.toLowerCase()}`;
}
