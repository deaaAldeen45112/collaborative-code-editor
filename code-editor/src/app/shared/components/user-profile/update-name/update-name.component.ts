import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
// import { APP_MESSAGES } from '../../../core/constants/error-messages.constants';
// import { CurrentUserData } from '../../../core/models/current-user-data';
// import { FormControllerService } from '../../../core/services/form-controller.service';
// import { AuthService } from '../../../core/services/auth.service';
// import { LocalStorageService } from '../../../core/services/local-storage.service';
// import { ToastMsgService } from '../../../core/services/toast.service';
// import { FIRST_NAME_CONTROL, LAST_NAME_CONTROL, PASSWORD_CONTROL, PHONE_CONTROL } from '../../../core/constants/form-control.constant';
// import { UpdateNameViewModel } from '../../../core/DTO/update-name-view-model';
// import { ApiResponse } from '../../../core/utils/ApiResponse';

@Component({
  selector: 'app-update-name',
  templateUrl: './update-name.component.html',
  styleUrl: './update-name.component.css'
})
export class UpdateNameComponent {
  // updateNameForm: FormGroup;
  // AppMessages = APP_MESSAGES;
  // userData: CurrentUserData|null=null;

  // constructor(
  //   private formController: FormControllerService,
  //   private authService: AuthService,
  //   private toast: ToastMsgService,
  //   private cache: LocalStorageService,

  // ) {
  //   this.updateNameForm = this.formController.createFormGroup({
  //     firstName: FIRST_NAME_CONTROL,
  //     lastName: LAST_NAME_CONTROL,
  //     password: PASSWORD_CONTROL,

  //   });
  // }
  // ngOnInit(): void {
  //   this.userData = this.cache.getItem(this.cache.USER_SESSION_KEY);
  //   console.log('User Data in ngOnInit:', this.userData);
  // }
  // resetForm():void{
  //   this.updateNameForm.reset();
  // }

  // UpdateName(): void {
  //   if (this.updateNameForm.invalid) {
  //     return;
  //   }
  //   let user: UpdateNameViewModel = <UpdateNameViewModel>this.updateNameForm.value;
  //   user.userId = this.userData?.userId;
  //   console.log(user);
  //   this.authService.updateUserName(user).subscribe(
  //     (response: ApiResponse<any>) => {
  //       if (response.status === 200) {
  //         this.userData!.firstName = user.firstName??"";
  //         this.userData!.lastName = user.lastName??"";

  //         this.cache.setItem(this.cache.USER_SESSION_KEY,this.userData);
  //         console.log('UpdateName successful:', response);
  //         this.resetForm();

  //       }
  //     },
  //     error => {
  //       console.error('UpdateName error:', error);

  //     }
  //   );
  // }
}
