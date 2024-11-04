import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { InputComponent } from './components/input/input.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { UserProfileRoutingModule } from './components/user-profile/user-profile-routing.module';


import { TokenInterceptor } from '../core/interceptors/auth-interceptor';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

import { ChatSectionComponent } from './components/input/chat-section/chat-section.component';

import { RestrictedScreenComponent } from './components/restricted-screen/restricted-screen.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { DraggableComponent } from './components/draggable/draggable.component';
import { NgxSpinnerModule } from 'ngx-spinner';
import { SpaceBeforeCapitalPipe } from '../core/pipes/space-before-capital.pipe';
import { MyAccountComponent } from './components/user-profile/my-account/my-account.component';
import { UserProfileComponent } from './components/user-profile/user-profile/user-profile.component';
import { UpdateEmailComponent } from './components/user-profile/update-email/update-email.component';
import { UpdateNameComponent } from './components/user-profile/update-name/update-name.component';
import { UpdatePhoneComponent } from './components/user-profile/update-phone/update-phone.component';
import { UpdatePasswordComponent } from './components/user-profile/update-password/update-password.component';
import { ChoosePlanComponent } from './components/user-profile/choose-plan/choose-plan.component';
import { UnsavedChangesDirective } from '../core/directive/unsaved-changes.directive';
import { DisableRightClickDirective } from '../core/directive/disable-right-click.directive';
import { AuthService } from '../core/services/auth.service';

@NgModule({
  declarations: [

    InputComponent,
    MyAccountComponent,
    UserProfileComponent,
    UpdateEmailComponent,
    UpdatePhoneComponent,
    UpdateNameComponent,
    UpdatePasswordComponent,
    ChoosePlanComponent,
    ChatSectionComponent,
    DisableRightClickDirective,
    RestrictedScreenComponent,
    UnsavedChangesDirective,
    DraggableComponent,
    SpaceBeforeCapitalPipe


  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,
    FormsModule,
    UserProfileRoutingModule,
    NgxDatatableModule,
    FontAwesomeModule,
    NgxSpinnerModule


  ],
  exports: [
    ReactiveFormsModule,
    InputComponent,
    HttpClientModule,
    FormsModule,
    UserProfileComponent,
    UpdateEmailComponent,
    UpdatePhoneComponent,
    UpdateNameComponent,
    UpdatePasswordComponent,
    MyAccountComponent,
    UserProfileRoutingModule,
    NgxDatatableModule,
    CommonModule,
    FontAwesomeModule,
    ChatSectionComponent,
    DisableRightClickDirective,
    UnsavedChangesDirective,
    DraggableComponent,
    NgxSpinnerModule,
    SpaceBeforeCapitalPipe


  ],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
  ],
})
export class SharedModule {}
