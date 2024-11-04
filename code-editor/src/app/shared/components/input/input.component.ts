import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-input',
  template: `
    <div [formGroup]="formGroup">
      <label class="form-label" [for]="controlName">{{ label }}</label>
      <input [placeholder]="placeholder"
      class="form-control" [id]="controlName" [formControlName]="controlName" [type]="type"
      [class.is-invalid]="formGroup.get(controlName)?.invalid && (formGroup.get(controlName)?.dirty || formGroup.get(controlName)?.touched)"
      [class.is-valid]="formGroup.get(controlName)?.valid && (formGroup.get(controlName)?.dirty || formGroup.get(controlName)?.touched)">
      <ng-container *ngIf="formGroup.get(controlName)?.invalid && (formGroup.get(controlName)?.dirty || formGroup.get(controlName)?.touched)">
        <span *ngFor="let error of getErrorMessages()" class="invalid-feedback">{{ error }}</span>
      </ng-container>
    </div>
  `
})
export class InputComponent {
  @Input() formGroup!: FormGroup;
  @Input() controlName!: string;
  @Input() label!: string;
  @Input() type: string = 'text';
  @Input() placeholder: string = '';
  @Input() errorMessages: { [key: string]: string } = {};

  getErrorMessages(): string[] {
    const control = this.formGroup.get(this.controlName);
    if (!control || !control.errors) {
      return [];
    }
    return Object.keys(control.errors).map(errorKey => this.errorMessages[errorKey]);
  }
}
