import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatSectionComponent } from './chat-section.component';

describe('ChatSectionComponent', () => {
  let component: ChatSectionComponent;
  let fixture: ComponentFixture<ChatSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ChatSectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
