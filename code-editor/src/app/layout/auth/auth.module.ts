import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import { SignInComponent } from './components/sign-in/sign-in.component';
import { SharedModule } from '../../shared/shared.module';
import { AuthLayoutComponent } from './auth-layout/auth-layout.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { GoogleSigninButtonModule } from '@abacritt/angularx-social-login';


@NgModule({
  declarations: [
    SignInComponent,
    AuthLayoutComponent,
    SignUpComponent
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    SharedModule,
    GoogleSigninButtonModule,
  ]
})
export class AuthModule { }
