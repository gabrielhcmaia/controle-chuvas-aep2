import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import {
  FiltroSolicitacoes,
  NovaSolicitacao,
  Solicitacao,
  StatusSolicitacao
} from '../models/solicitacao.model';

@Injectable({ providedIn: 'root' })
export class SolicitacaoService {
  private readonly http = inject(HttpClient);
  private readonly urlBase = `${environment.urlApi}/solicitacoes`;

  listar(filtros?: FiltroSolicitacoes): Observable<Solicitacao[]> {
    let parametros = new HttpParams();
    if (filtros?.status) {
      parametros = parametros.set('status', filtros.status);
    }
    if (filtros?.categoria) {
      parametros = parametros.set('categoria', filtros.categoria);
    }
    return this.http.get<Solicitacao[]>(this.urlBase, { params: parametros });
  }

  buscarPorId(id: number): Observable<Solicitacao> {
    return this.http.get<Solicitacao>(`${this.urlBase}/${id}`);
  }

  criar(novaSolicitacao: NovaSolicitacao): Observable<Solicitacao> {
    return this.http.post<Solicitacao>(this.urlBase, novaSolicitacao);
  }

  atualizarStatus(id: number, status: StatusSolicitacao): Observable<Solicitacao> {
    return this.http.patch<Solicitacao>(`${this.urlBase}/${id}/status`, { status });
  }
}
