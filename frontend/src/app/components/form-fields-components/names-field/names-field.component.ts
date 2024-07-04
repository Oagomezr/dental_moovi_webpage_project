import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-names-field',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './names-field.component.html',
  styleUrls: ['./names-field.component.scss']
})
export class NamesFieldComponent {
  @Input() formName : FormControl = new FormControl('');
  @Input() labelName : string = '';

  containsNumber(value: string | null): boolean {
    if(value == null) return false;
    return /\d/.test(value);
  }
}
