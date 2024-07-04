import { Component, Input } from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatBadgeModule} from '@angular/material/badge';

@Component({
  selector: 'app-cart-badge',
  templateUrl: './cart-badge.component.html',
  styleUrls: ['./cart-badge.component.scss'],
  standalone: true,
  imports: [MatBadgeModule, MatButtonModule, MatIconModule]
})
export class CartBadgeComponent {

  @Input() amountOfProducts?: number;

  hidden = false;

  toggleBadgeVisibility() {
    this.hidden = !this.hidden;
  }
}
