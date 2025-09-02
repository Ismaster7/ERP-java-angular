import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { NotificationService } from '../services/notification.service';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationService = inject(NotificationService);

  return next(req).pipe(
    catchError((error) => {
      handleError(error, notificationService);
      return throwError(() => error);
    })
  );
};

function handleError(error: any, notificationService: NotificationService) {
  if (error.error instanceof ErrorEvent) {
    // Erro do cliente
    notificationService.error(
      'Erro de Cliente',
      'Ocorreu um erro no navegador. Verifique sua conexão.'
    );
  } else {
    // Erro do servidor
    const serverError = error.error;

    if (serverError && typeof serverError === 'object') {
      handleServerError(serverError, error.status, notificationService);
    } else {
      notificationService.error(
        `Erro  ${error.status}`,
        error.message || 'Ocorreu um erro inesperado'
      );
    }
  }
}

function handleServerError(error: any, status: number, notificationService: NotificationService) {
  const title = error.exeption || `Erro ao conectar com o servidor`;
  const message = error.details || error.message || 'Tente novamente mais tarde';

  //notificationService.error(title, message);
}
