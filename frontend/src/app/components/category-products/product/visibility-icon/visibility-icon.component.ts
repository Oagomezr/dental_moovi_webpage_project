import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-visibility-icon',
  templateUrl: './visibility-icon.component.html',
  styleUrls: ['./visibility-icon.component.scss'],
  standalone: true,
  imports: [MatIconModule, CommonModule]
})
export class VisibilityIconComponent {
  
  @Input() hide: boolean = false;

  @Output() changeVisibility = new EventEmitter<boolean>();
  
  togglePasswordVisibility(event: Event) {
    event.preventDefault();
    this.changeVisibility.emit(!this.hide);
  }
}
