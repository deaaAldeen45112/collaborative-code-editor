import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms'; // Add FormControl import
import { FormControllerService } from '../../../../core/services/form-controller.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Router } from '@angular/router';
import {
  EMAIL_CONTROL,
  PASSWORD_CONTROL,
} from '../../../../core/constants/form-control.constant';
import { LocalStorageService } from '../../../../core/services/local-storage.service';
import { ToastMsgService } from '../../../../core/services/toast.service';
import { APP_MESSAGES } from '../../../../core/constants/error-messages.constants';
import { jwtDecode } from 'jwt-decode';
import {

  ADMIN_ROLE,

} from '../../../../core/constants/app.constants';
import { LoginDTO } from '../../../../core/dto/login.dto';
import { ApiResponse } from '../../../../core/utils/api-response';
import { ResLoginDTO } from '../../../../core/dto/res-login.dto';
import { UserService } from '../../../../core/services/user.service';
import {
  GoogleLoginProvider,
  SocialAuthService,
  SocialUser,
} from '@abacritt/angularx-social-login';
import { HttpClient } from '@angular/common/http';
import { take } from 'rxjs/operators';
@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css'],
})
export class SignInComponent {
  loginForm: FormGroup;
  AppMessages = APP_MESSAGES;
  socialUser!: SocialUser;
  loggedIn!: boolean;

  constructor(
    private formController: FormControllerService,
    private authService: AuthService,
    private router: Router,
    private cache: LocalStorageService,
    private toast: ToastMsgService,
    private userServcie: UserService,
    private socialAuthService: SocialAuthService,
    private http: HttpClient
  ) {
    this.loginForm = new FormGroup({
      email: EMAIL_CONTROL,
      password: PASSWORD_CONTROL,
      rememberMe: new FormControl(false),
    });
  }
  
  ngOnInit(): void {
    
  }
  login(): void {
    if (this.loginForm.invalid) {
      return;
    }
    const credentials: LoginDTO = <LoginDTO>this.loginForm.value;
    this.authService.login(credentials).subscribe((response) => {
          const roleId = response.data?.user.roleId;
          if(!roleId){return;}
          this.authService.navigateToDash(roleId)
          if (this.loginForm.get('rememberMe')?.value) {}
          this.loginForm.reset();
      },
      (error) => {
        console.error('Login error:', error);
      }
    );
  }
  loginByGmail() {
    this.socialAuthService.authState.pipe(take(1)).subscribe((user) => {
      this.socialUser = user;
      console.log(user);
      console.log(user.idToken);
       if (user != null) {
         this.authService.loginByGmail(user.idToken).subscribe(
           (response) => {
             const roleId = response.data?.user.roleId;
             if(!roleId){return;}
             
             this.authService.navigateToDash(roleId)
             console.log('Login successful!', response);
           },
           (error) => {
             console.error('Login failed', error);
           }
         );
       }
    });
  }
  redirectToGithub() {
   this.authService.redirectToGithub();
  }
 
}
