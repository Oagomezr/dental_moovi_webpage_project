import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NamesFieldComponent } from './names-field.component';

describe('NamesFieldComponent', () => {
  let component: NamesFieldComponent;
  let fixture: ComponentFixture<NamesFieldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ NamesFieldComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NamesFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
