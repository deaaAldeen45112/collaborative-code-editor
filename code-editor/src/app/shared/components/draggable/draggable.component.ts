import { Component, ElementRef, HostListener, Input, ViewChild } from '@angular/core';

@Component({
  selector: 'app-draggable',
  templateUrl: './draggable.component.html',
  styleUrls: ['./draggable.component.css']
})
export class DraggableComponent {
  @ViewChild('draggable') draggable!: ElementRef;

  @Input() width: string = '100px';
  @Input() height: string = '100px'; 

  private isDragging = false;
  private startX = 0;
  private startY = 0;
  private initialX = 0;
  private initialY = 0;

  ngAfterViewInit(): void {
    this.draggable.nativeElement.style.width = this.width;
    this.draggable.nativeElement.style.height = this.height;
  }

  onMouseDown(event: MouseEvent): void {
    this.isDragging = true;
    this.startX = event.clientX;
    this.startY = event.clientY;

    const rect = this.draggable.nativeElement.getBoundingClientRect();
    this.initialX = rect.left;
    this.initialY = rect.top;

    document.addEventListener('mousemove', this.onMouseMove);
    document.addEventListener('mouseup', this.onMouseUp);
  }

  onMouseMove = (event: MouseEvent): void => {
    if (this.isDragging) {
      const dx = event.clientX - this.startX;
      const dy = event.clientY - this.startY;

      this.draggable.nativeElement.style.left = `${this.initialX + dx}px`;
      this.draggable.nativeElement.style.top = `${this.initialY + dy}px`;
    }
  };

  onMouseUp = (): void => {
    this.isDragging = false;

    const element = this.draggable.nativeElement;
    const { innerWidth: windowWidth, innerHeight: windowHeight } = window;
    const { offsetLeft, offsetTop, offsetWidth, offsetHeight } = element;

    const rightDistance = windowWidth - (offsetLeft + offsetWidth);
    const bottomDistance = windowHeight - (offsetTop + offsetHeight);

    if (rightDistance < 50) {
      element.style.right = '0px';
      element.style.left = 'auto';
    } else {
      element.style.left = `${offsetLeft}px`;
      element.style.right = 'auto';
    }

    if (bottomDistance < 50) {
      element.style.bottom = '0px';
      element.style.top = 'auto';
    } else {
      element.style.top = `${offsetTop}px`;
      element.style.bottom = 'auto';
    }

    document.removeEventListener('mousemove', this.onMouseMove);
    document.removeEventListener('mouseup', this.onMouseUp);
  };
}
