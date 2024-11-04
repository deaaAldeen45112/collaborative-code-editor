import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
@Component({
  selector: 'app-auth-callback',
  templateUrl: './auth-callback.component.html',
  styleUrl: './auth-callback.component.css'
})
export class AuthCallbackComponent {
  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const code = params['code'];
      if (code) {
        console.log(code);
        this.authService.handleAuthCallback(code).subscribe(
          (response) => {
            const roleId = response.data?.user.roleId;
            if(!roleId){return;}
            this.authService.navigateToDash(roleId);
  
          },
          (error) => {
            this.router.navigate(['/auth/sign-in']);
            console.error('Error during authentication:', error);
          }
        );
      }
    });
  }
}
