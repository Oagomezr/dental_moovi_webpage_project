import { Component, Renderer2 } from '@angular/core';

@Component({
  selector: 'app-linked-in-badge',
  standalone: true,
  imports: [],
  templateUrl: './linked-in-badge.component.html',
  styleUrl: './linked-in-badge.component.scss'
})
export class LinkedInBadgeComponent {
  constructor(private renderer: Renderer2) { }

  ngOnInit(): void {
    const script = this.renderer.createElement('script');
    script.src = 'https://platform.linkedin.com/badges/js/profile.js';
    script.async = true;
    script.defer = true;
    this.renderer.appendChild(document.body, script);
  }
}
