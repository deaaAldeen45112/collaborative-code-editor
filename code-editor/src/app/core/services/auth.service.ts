import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { GenericApiHandlerService } from './api.service';


import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';

import { catchError } from 'rxjs/operators';
import { ApiResponse } from '../utils/api-response';
import { LoginDTO } from '../dto/login.dto';
import { UserDTO } from '../dto/user.dto';
import { LocalStorageService } from './local-storage.service';
import { Router } from '@angular/router';
import { API_ENDPOINTS } from '../constants/api.constants';
import { ResLoginDTO } from '../dto/res-login.dto';
import { RegisterDTO } from '../dto/register.dto';
import { ADMIN_ROLE, CODER_ROLE } from '../constants/app.constants';

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  constructor(private router: Router,private apiHandler: GenericApiHandlerService,
     private  cache : LocalStorageService,private http: HttpClient) { }

  login(credentials: LoginDTO): Observable<ApiResponse<ResLoginDTO>> {
    return this.apiHandler.post<ApiResponse<ResLoginDTO>>(API_ENDPOINTS.AUTH.LOGIN, credentials)
      .pipe(
        tap(response => {
          this.setToken(response.data!.token);
          this.setCurrentUser(response.data!.user);
        })
      );
  }
  register(registerDto:RegisterDTO): Observable<ApiResponse> {
    return this.apiHandler.post<ApiResponse>(API_ENDPOINTS.AUTH.REGISTER, registerDto)
  }
  loginByGmail(token: string): Observable<ApiResponse<ResLoginDTO>> {
    return this.apiHandler.post<ApiResponse<ResLoginDTO>>(API_ENDPOINTS.AUTH.LOGIN_BY_GMAIL, { idToken: token })
      .pipe(
        tap(response => {
          if (response.data) {
            this.setToken(response.data.token);
            this.setCurrentUser(response.data.user);
          }
        })
      );
  }

  redirectToGithub() {
    let clientId = 'Ov23lia679JkoqrUGfFw';
    let redirectUri = window.location.origin + '/auth/github-callback';
    let githubAuthUrl = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&scope=user:email`;
    window.location.href = githubAuthUrl;
  }
   handleAuthCallback(code: string): Observable<ApiResponse<ResLoginDTO>> {
    console.log(code);
    return this.apiHandler.post<ApiResponse<ResLoginDTO>>(API_ENDPOINTS.AUTH.LOGIN_BY_GITHUB, { code })
      .pipe(
        tap(response => {
          if (response.data) {
            this.setToken(response.data.token);
            this.setCurrentUser(response.data.user);
          }
        })
      );
  }
  private setToken(token: string): void {
    this.cache.setItem(this.cache.AUTH_TOKEN, token);
  }
  private setCurrentUser(user: UserDTO): void {
    this.cache.setItem(this.cache.USER_SESSION_KEY,user);
  }
  getCurrentUser(): UserDTO | null {
    return this.cache.getItem(this.cache.USER_SESSION_KEY);
  }
  navigateToDash(roleId:number){

    switch (roleId) {
      case ADMIN_ROLE:
        this.router.navigate(['/admin/dashboard']);
        break;
      case CODER_ROLE:
        this.router.navigate(['/coder/project']);
        break;
      default:
        console.warn('Unknown role:', roleId);
        break;
    }
  }
  logout() {
    this.cache.removeItem(this.cache.AUTH_TOKEN);
    this.cache.removeItem(this.cache.USER_SESSION_KEY);
    localStorage.clear();
    this.cache.clear();
    this.router.navigate(['/auth/sign-in']);
  }

}
