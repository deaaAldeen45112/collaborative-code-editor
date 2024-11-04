import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {


  public AUTH_TOKEN: string = "auth-token";
  public USER_SESSION_KEY: string = "user-session-key";
  public ANSWER_SAVED: string = "answer-save";
  public TIME_REMAINING: string = "time-remaining";
  public EXAM: string = "exam";
  public COMPANY: string = "company";
  public USER_ID: string = "userId";
  public ROLE_ID: string = "roleId";
  public PAYLOAD: string = "payload";
  public STUDENT_SCORE: string = "student-score";
  public NUMBER_OF_QUESTIONS: string = "number-of-Qusstions";
  public EMPLOYEE_DTO='employeeDTO';
  public PROJECT_ID:string='project-id';
  public PROJECT_KEY:string='project-key';
  constructor() { }

  setItem(key: string, value: any): void {
    try {
      if (value === undefined) {
        console.warn(`Attempted to store 'undefined' value for key '${key}' in localStorage.`);
        return;
      }

      const valueToStore = typeof value === 'object' ? JSON.stringify(value) : value;
      localStorage.setItem(key, valueToStore);
    } catch (error) {
      console.error(`Error storing item with key '${key}' in localStorage:`, error);
    }
  }

  getItem<T>(key: string): T | null {
    try {
      const item = localStorage.getItem(key);
      if (item === null) {
        return null;
      }

      try {
        return JSON.parse(item) as T;
      } catch {
        return item as unknown as T;
      }
    } catch (error) {
      console.error(`Error retrieving item with key '${key}' from localStorage:`, error);
      return null;
    }
  }
  getToken():string{
    return this.getItem(this.AUTH_TOKEN) || '';
  } 
  removeItem(key: string): void {
    try {
      localStorage.removeItem(key);
    } catch (error) {
      console.error(`Error removing item with key '${key}' from localStorage:`, error);
    }
  }
  getProjectId():string{
    return this.getItem(this.PROJECT_ID) || '';
  }
  clear(): void {
    try {
      localStorage.clear();
    } catch (error) {
      console.error('Error clearing localStorage:', error);
    }
  }
}
