import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { CategoriesData } from 'src/app/models/categories/categoriesData';

@Component({
  selector: 'app-hover-box-products',
  templateUrl: './hover-box-products.component.html',
  styleUrls: ['./hover-box-products.component.scss'],
  standalone: true,
  imports:[CommonModule]
})
export class HoverBoxProductsComponent {
  @Input() categories?: CategoriesData[];

  assignClass(number: number): number {
    return (number - 1) % 4;
  }


}
