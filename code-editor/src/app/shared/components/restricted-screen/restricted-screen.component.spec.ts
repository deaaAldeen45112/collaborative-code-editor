import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RestrictedScreenComponent } from './restricted-screen.component';

describe('RestrictedScreenComponent', () => {
  let component: RestrictedScreenComponent;
  let fixture: ComponentFixture<RestrictedScreenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RestrictedScreenComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RestrictedScreenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
