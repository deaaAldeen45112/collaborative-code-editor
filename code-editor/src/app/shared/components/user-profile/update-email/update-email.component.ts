import { Component, OnInit } from '@angular/core';
// import { EMAIL_CONTROL, PASSWORD_CONTROL } from '../../../core/constants/form-control.constant';
// import { AuthService } from '../../../core/services/auth.service';
// import { ToastMsgService } from '../../../core/services/toast.service';
// import { FormControllerService } from '../../../core/services/form-controller.service';
// import { APP_MESSAGES } from '../../../core/constants/error-messages.constants';
// import { FormGroup } from '@angular/forms';
// import { ApiResponse } from '../../../core/utils/ApiResponse';
// import { LocalStorageService } from '../../../core/services/local-storage.service';
// import { CurrentUserData } from '../../../core/models/current-user-data';
// import { UpdateEmailViewModel } from '../../../core/DTO/update-email-view-model';

@Component({
  selector: 'app-update-email',
  templateUrl: './update-email.component.html',
  styleUrl: './update-email.component.css'
})
export class UpdateEmailComponent {
  // updateEmailForm: FormGroup;
  // AppMessages = APP_MESSAGES;
  // userData: CurrentUserData|null=null;

  // constructor(
  //   private formController: FormControllerService,
  //   private authService: AuthService,
  //   private toast: ToastMsgService,
  //   private cache: LocalStorageService,

  // ) {
  //   this.updateEmailForm = this.formController.createFormGroup({
  //     email: EMAIL_CONTROL,
  //     password: PASSWORD_CONTROL,
  //   });
  // }
  // ngOnInit(): void {
  //   this.userData = this.cache.getItem(this.cache.USER_SESSION_KEY);
  //   console.log('User Data in ngOnInit:', this.userData);
  // }
  // resetForm():void{
  //   this.updateEmailForm.reset();
  // }

  // UpdateEmail(): void {
  //   if (this.updateEmailForm.invalid) {
  //     return;
  //   }
  //   let user: UpdateEmailViewModel = <UpdateEmailViewModel>this.updateEmailForm.value;
  //   user.userId = this.userData?.userId;
  //   console.log(user);
  //   this.authService.updateEmail(user).subscribe(
  //     (response: ApiResponse<any>) => {
  //       if (response.status === 200) {
  //         this.userData!.email = user.email??"";

  //         this.cache.setItem(this.cache.USER_SESSION_KEY,this.userData);
  //         console.log('UpdateEmail successful:', response);
  //         this.resetForm();
  //       }
  //     },
  //     error => {
  //       console.error('UpdateEmail error:', error);

  //     }
  //   );
  // }
}
