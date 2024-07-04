import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HoverBoxCartComponent } from './hover-box-cart.component';

describe('HoverBoxCartComponent', () => {
  let component: HoverBoxCartComponent;
  let fixture: ComponentFixture<HoverBoxCartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HoverBoxCartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HoverBoxCartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
