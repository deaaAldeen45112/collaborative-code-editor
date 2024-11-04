import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

import { jwtDecode } from 'jwt-decode';
import { LocalStorageService } from '../services/local-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private cache: LocalStorageService) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    let userToken:any = this.cache.getItem(this.cache.AUTH_TOKEN);

    if (!userToken) {
      this.router.navigate(['/auth/sign-in']);
      return false;
    }

    try {
      const decodedToken: any = jwtDecode(userToken);

      const userRoleId = Number(decodedToken.RoleId);

      const requiredRoles = next.data['roles'] as Array<number>;

      if (requiredRoles && !requiredRoles.includes(userRoleId)) {
        this.router.navigate(['**']);
        return false;
      }

      return true;
    } catch (error) {
      console.error('Token decoding error:', error);
      this.router.navigate(['/auth/sign-in']);
      return false;
    }
  }
}
// todo: example how to use
