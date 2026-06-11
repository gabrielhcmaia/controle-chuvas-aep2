import { provideHttpClient } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { rotas } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(rotas), provideHttpClient()]
};
