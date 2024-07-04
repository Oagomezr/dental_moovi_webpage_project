import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HoverBoxProductsComponent } from './hover-box-products.component';

describe('HoverBoxProductsComponent', () => {
  let component: HoverBoxProductsComponent;
  let fixture: ComponentFixture<HoverBoxProductsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HoverBoxProductsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HoverBoxProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
