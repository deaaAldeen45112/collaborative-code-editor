import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./layout/auth/auth.module').then(m => m.AuthModule) ,
   // canActivate: [AuthGuard], data: { roles: [ADMIN_ROLE] }
  },
  {
    path: 'admin',
    loadChildren: () => import('./layout/admin/admin.module').then(m => m.AdminModule) ,
   // canActivate: [AuthGuard], data: { roles: [ADMIN_ROLE] }
  },
  {
    path: 'coder',
    loadChildren: () => import('./layout/coder/coder.module').then(m => m.CoderModule) ,
   // canActivate: [AuthGuard], data: { roles: [ADMIN_ROLE] }
  },
  {
    path: 'editor',
    loadChildren: () => import('./layout/editor/editor.module').then(m => m.EditorModule) ,
   // canActivate: [AuthGuard], data: { roles: [ADMIN_ROLE] }
  },
  {
    path: '',
    redirectTo: '/auth/sign-in',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/auth/sign-in'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
