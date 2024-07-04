import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-direction',
  templateUrl: './direction.component.html',
  styleUrls: ['./direction.component.scss'],
  standalone: true,
  imports:[CommonModule]
})
export class DirectionComponent {

  @Input() currentPageOrProduct?: string;
  @Input() locationCategory: string[] = [];

}
