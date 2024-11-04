import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ToastMsgService {

  constructor(private toastr: ToastrService) { }

  showError(message: string): void {
    this.toastr.error(message, 'Error');
  }

  showSuccess(message: string): void {
    this.toastr.success(message, 'Success');
  }

  showInfo(message: string): void {
    this.toastr.info(message, 'Info');
  }

  showWarning(message: string): void {
    this.toastr.warning(message, 'Warning');
  }
}
