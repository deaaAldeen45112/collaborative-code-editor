import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CoderLayoutComponent } from './coder-layout/coder-layout.component';
import { HomeComponent } from './components/home/home.component';
import { ProjectComponent } from './components/project/project.component';
import { InvitationComponent } from './components/invitation/invitation.component';

const routes: Routes = [
  {
    path: '',
    component: CoderLayoutComponent,
    children: [
      { path: 'home', component: HomeComponent },
      { path: 'project', component: ProjectComponent },
      { path: 'invitation', component: InvitationComponent },
      
    ],
  },

  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CoderRoutingModule { }
