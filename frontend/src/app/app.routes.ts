import { Routes } from '@angular/router';

export const rotas: Routes = [
  {
    path: '',
    title: 'Controle de Chuvas — Dashboard',
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent)
  },
  {
    path: 'mapa',
    title: 'Controle de Chuvas — Mapa',
    loadComponent: () =>
      import('./features/mapa/mapa.component').then((m) => m.MapaComponent)
  },
  {
    path: 'nova-solicitacao',
    title: 'Controle de Chuvas — Nova Solicitação',
    loadComponent: () =>
      import('./features/nova-solicitacao/nova-solicitacao.component')
        .then((m) => m.NovaSolicitacaoComponent)
  },
  {
    path: 'perfil',
    title: 'Controle de Chuvas — Perfil do Usuário',
    loadComponent: () =>
      import('./features/perfil/perfil.component').then((m) => m.PerfilComponent)
  },
  {
    path: 'admin',
    title: 'Controle de Chuvas — Painel Administrativo',
    loadComponent: () =>
      import('./features/painel-admin/painel-admin.component').then((m) => m.PainelAdminComponent)
  },
  {
    path: 'admin/:id',
    title: 'Controle de Chuvas — Painel Administrativo',
    loadComponent: () =>
      import('./features/painel-admin/painel-admin.component').then((m) => m.PainelAdminComponent)
  },
  { path: '**', redirectTo: '' }
];
