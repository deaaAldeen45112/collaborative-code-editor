import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoderLayoutComponent } from './coder-layout.component';

describe('CoderLayoutComponent', () => {
  let component: CoderLayoutComponent;
  let fixture: ComponentFixture<CoderLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CoderLayoutComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CoderLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
