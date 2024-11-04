import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, FormArray, Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class FormControllerService {
  constructor(private fb: FormBuilder) {}

  createFormGroup(controlsConfig: { [key: string]: any }): FormGroup {
    return this.fb.group(controlsConfig);
  }

  createFormControl(initialValue: any, validators: any[] = []): FormControl {
    return this.fb.control(initialValue, validators);
  }

  createFormArray(controls: any[] = []): FormArray {
    return this.fb.array(controls);
  }

  addControlToFormArray(formArray: FormArray, control: FormControl | FormGroup): void {
    formArray.push(control);
  }

  removeControlFromFormArray(formArray: FormArray, index: number): void {
    formArray.removeAt(index);
  }

  isControlDirty(control: FormControl): boolean {
    return control.dirty;
  }

  isControlTouched(control: FormControl): boolean {
    return control.touched;
  }

  isControlPristine(control: FormControl): boolean {
    return control.pristine;
  }

  getControlValue(control: FormControl): any {
    return control.value;
  }

  isFormValid(form: FormGroup): boolean {
    return form.valid;
  }

  isFormControlValid(control: FormControl): boolean {
    return control.valid;
  }

  isFormControlInvalid(control: FormControl): boolean {
    return control.invalid;
  }

  getFormSummary(form: FormGroup): any {
    return {
      values: form.value,
      valid: form.valid,
      invalid: form.invalid,
      dirty: form.dirty,
      pristine: form.pristine,
      touched: form.touched,
    };
  }
}
