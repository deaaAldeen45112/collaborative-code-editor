import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoderLayoutComponent } from './layout/coder/coder-layout/coder-layout.component';
import { GoogleLoginProvider, GoogleSigninButtonModule, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import { CommonModule } from '@angular/common';
import { SharedModule } from './shared/shared.module';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

export function getAuthServiceConfigs() {
  const config: SocialAuthServiceConfig = {
    autoLogin: false,
    lang: 'en',
    providers: [
      {
        id: GoogleLoginProvider.PROVIDER_ID,
        provider: new GoogleLoginProvider('1085825486944-0mee6tmfenrshpss5adhdbchh8pd26r9.apps.googleusercontent.com') // Replace with your Google Client ID
      }
    ]
  };
  return config;
}

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    GoogleSigninButtonModule,
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    SharedModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-bottom-center',
      preventDuplicates: true,
      progressBar: true,
      progressAnimation: 'increasing',
      closeButton: true,
      tapToDismiss: true
    }),
  ],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useFactory: getAuthServiceConfigs,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
