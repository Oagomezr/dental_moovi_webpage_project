import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { AuthenticateService } from 'src/app/services/authenticate/authenticate.service';

export const tokenGuard: CanActivateFn = (route, state) => {

  const authSer = inject(AuthenticateService);
  const router = inject(Router);

  const requiredRole = route.data?.['role'];

  return authSer.permission().pipe(
    map((isAdmin: boolean) => {
      if (isAdmin) return true;
      if(requiredRole === 'user') return true;
      router.navigate(['/']);
      return false;
    }),
    catchError(() => {
      //no autenticado o problema en la solicitud
      router.navigate(['/login']);
      return of(false); // Bloquear la navegación
    })
  );
};
