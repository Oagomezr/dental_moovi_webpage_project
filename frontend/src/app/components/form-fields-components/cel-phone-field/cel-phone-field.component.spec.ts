import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CelPhoneFieldComponent } from './cel-phone-field.component';

describe('CelPhoneFieldComponent', () => {
  let component: CelPhoneFieldComponent;
  let fixture: ComponentFixture<CelPhoneFieldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CelPhoneFieldComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CelPhoneFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
