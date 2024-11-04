import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoderRoutingModule } from './coder-routing.module';
import { CoderLayoutComponent } from './coder-layout/coder-layout.component';
import { ProjectComponent } from './components/project/project.component';
import { HomeComponent } from './components/home/home.component';
import { SharedModule } from '../../shared/shared.module';
import { NgxMonacoTreeComponent } from 'ngx-monaco-tree';
import { InvitationComponent } from './components/invitation/invitation.component';
import { NgSelectModule } from '@ng-select/ng-select';
@NgModule({
  declarations: [
    CoderLayoutComponent,
    HomeComponent,
    ProjectComponent,
    InvitationComponent  ],
  imports: [
    CommonModule,
    CoderRoutingModule,
    SharedModule ,
    NgSelectModule
 
  ]
})
export class CoderModule { }
