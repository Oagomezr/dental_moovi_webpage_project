import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkedInBadgeComponent } from './linked-in-badge.component';

describe('LinkedInBadgeComponent', () => {
  let component: LinkedInBadgeComponent;
  let fixture: ComponentFixture<LinkedInBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LinkedInBadgeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LinkedInBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
