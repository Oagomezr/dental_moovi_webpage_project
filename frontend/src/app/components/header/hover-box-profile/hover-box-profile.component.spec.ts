import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HoverBoxProfileComponent } from './hover-box-profile.component';

describe('HoverBoxProfileComponent', () => {
  let component: HoverBoxProfileComponent;
  let fixture: ComponentFixture<HoverBoxProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HoverBoxProfileComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HoverBoxProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
