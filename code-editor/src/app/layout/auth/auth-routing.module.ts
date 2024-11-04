import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthLayoutComponent } from './auth-layout/auth-layout.component';
import { SignInComponent } from './components/sign-in/sign-in.component';
import { AuthCallbackComponent } from './components/auth-callback/auth-callback.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';

const routes: Routes = [ {
  path: '',
  component: AuthLayoutComponent,
  children: [
    { path: 'sign-in', component: SignInComponent },
    { path: 'sign-up', component: SignUpComponent },
    { path: 'github-callback', component: AuthCallbackComponent }
  ],
},];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
