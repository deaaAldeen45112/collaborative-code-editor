import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
export const PHONE_NUMBER_REGEX = /^[0-9]{10}$/;


export function  endTimeAfterStartTimeValidator(control: AbstractControl): { [key: string]: boolean } | null {
  const startTime = control.get('workFrom')?.value;
  const endTime = control.get('workTo')?.value;

  if (!startTime || !endTime) {
    return null;
  }

  return startTime < endTime ? null : { endTimeBeforeStartTime: true };
}

export function matchPasswordValidator(): ValidatorFn {
  return (formGroup: AbstractControl): ValidationErrors | null => {
    const newPassword = formGroup.get('newPassword');
    const confirmPassword = formGroup.get('confirmPassword');
    if (newPassword && confirmPassword && newPassword.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    return null;
  };
}
