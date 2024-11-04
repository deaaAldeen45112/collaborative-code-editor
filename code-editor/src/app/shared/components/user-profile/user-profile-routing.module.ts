import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { UpdateEmailComponent } from './update-email/update-email.component';
import { UpdatePhoneComponent } from './update-phone/update-phone.component';
import { UpdateNameComponent } from './update-name/update-name.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';
import { MyAccountComponent } from './my-account/my-account.component';
import { ChoosePlanComponent } from './choose-plan/choose-plan.component';


const routes: Routes = [

  { path: '', component: UserProfileComponent, children: [
    { path: 'my-account', component: MyAccountComponent },
    { path: 'update-email', component: UpdateEmailComponent },
    { path: 'update-phone', component: UpdatePhoneComponent },
    { path: 'update-name', component: UpdateNameComponent },
    { path: 'update-password', component: UpdatePasswordComponent },
    { path: 'choose-plan', component: ChoosePlanComponent },
  ] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserProfileRoutingModule { }
