import { FormControl, Validators } from '@angular/forms';
import {PHONE_NUMBER_REGEX} from "./regex-patterns.constants";


export const EMAIL_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.email,
]);

export const PHONE_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.pattern(PHONE_NUMBER_REGEX),
]);

export const NAME_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.minLength(2),
]);

export const AGE_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.min(18),
]);

export const PASSWORD_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.minLength(6),
]);
export const NEW_PASSWORD_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.minLength(6),
]);

export const CONFIRM_PASSWORD_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.minLength(6),
]);


export const FULL_NAME_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.maxLength(20),
  Validators.minLength(3),
]);


export const SUBJECT_CONTACT_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.maxLength(20),
  Validators.minLength(3),
]);

export const CONTACT_MSG_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.maxLength(255),
  Validators.minLength(3),
]);

export const FIRST_NAME_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.maxLength(25),
  Validators.minLength(3),
]);


export const LAST_NAME_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.maxLength(25),
  Validators.minLength(3),
]);


export const ABOUT_US_TITLE: FormControl = new FormControl('', [
  Validators.required,
  Validators.maxLength(50),
  Validators.minLength(3),
]);

export const PLAN_CONTROL: FormControl = new FormControl('', [
  Validators.required,
  Validators.maxLength(50),
]);

