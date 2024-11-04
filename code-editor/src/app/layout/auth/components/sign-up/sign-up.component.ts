import { Component } from '@angular/core';
import { FormControllerService } from '../../../../core/services/form-controller.service';
import { AuthService } from '../../../../core/services/auth.service'; //
import { Router } from '@angular/router';
import { LocalStorageService } from '../../../../core/services/local-storage.service';
import { UserService } from '../../../../core/services/user.service';
import { SocialAuthService } from '@abacritt/angularx-social-login';
import { HttpClient } from '@angular/common/http';
import { FormGroup, Validators } from '@angular/forms';
import { RegisterDTO } from '../../../../core/dto/register.dto';
import { APP_MESSAGES } from '../../../../core/constants/error-messages.constants';
@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent {
  registerForm: FormGroup;
  AppMessages = APP_MESSAGES;
    constructor(private formController: FormControllerService,
    private authService: AuthService,
    private router: Router,
  ) 
  {
    this.registerForm = this.formController.createFormGroup({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      //confirmPassword: ['', [Validators.required, Validators.minLength(6)]],
    });

    
  }

  register() {
    if (this.registerForm.valid) {
      const formData = this.registerForm.value;
      console.log(formData);
    }
    const registerData: RegisterDTO = <RegisterDTO>this.registerForm.value;
    this.authService.register(registerData).subscribe((res) => {
      console.log(res);
      this.router.navigate(['/auth/sign-in']);
    },
    (error) => {
      console.log(error);
    });


  }


  private passwordMatchValidator(formGroup: FormGroup): { [key: string]: boolean } | null {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { 'mismatch': true };
  }
  

}
