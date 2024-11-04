
import { Component } from '@angular/core';
import { LocalStorageService } from '../../../../core/services/local-storage.service';
import { ADMIN_ROLE } from '../../../../core/constants/app.constants';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent  {
  examProviderRole:number = ADMIN_ROLE;

  userRole: number | null = null;

  constructor(public cache: LocalStorageService) {

}


  getUserDashboard(): string {
    if (this.userRole === null) {
      return "Loading...";
    }

    switch (this.userRole) {
      case ADMIN_ROLE:
        return "Admin Setting";

      default:
        console.error('Invalid user role:', this.userRole);
        return "Unknown Role";
    }
  }
}
