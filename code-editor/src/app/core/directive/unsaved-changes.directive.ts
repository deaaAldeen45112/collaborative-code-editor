import { Directive, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[appUnsavedChanges]'
})
export class UnsavedChangesDirective {

  // @Input('appUnsavedChanges') hasUnsavedChanges: boolean = false;

  @HostListener('window:beforeunload', ['$event'])
  unloadNotification($event: BeforeUnloadEvent): void {
    // if (this.hasUnsavedChanges) {
      const confirmationMessage = 'You have unsaved changes. Are you sure you want to leave?';
      $event.returnValue = confirmationMessage;
    // }
  }
}
