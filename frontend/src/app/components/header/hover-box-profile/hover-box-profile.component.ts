import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Users } from 'src/app/models/users';

@Component({
  selector: 'app-hover-box-profile',
  templateUrl: './hover-box-profile.component.html',
  styleUrls: ['./hover-box-profile.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class HoverBoxProfileComponent {
  @Input() nameUser?:string|null;
}
