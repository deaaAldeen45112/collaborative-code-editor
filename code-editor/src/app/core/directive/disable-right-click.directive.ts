import { Directive, HostListener } from '@angular/core';

@Directive({
  selector: '[disable-right-click]'
})
export class DisableRightClickDirective {
  @HostListener('contextmenu', ['$event'])
  onRightClick(event: MouseEvent): void {
    event.preventDefault();
  }
}
