import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmCodeFieldComponent } from './confirm-code-field.component';

describe('ConfirmCodeFieldComponent', () => {
  let component: ConfirmCodeFieldComponent;
  let fixture: ComponentFixture<ConfirmCodeFieldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmCodeFieldComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmCodeFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
