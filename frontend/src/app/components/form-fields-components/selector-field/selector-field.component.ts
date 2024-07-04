import { Component, Input } from '@angular/core';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Enum1 } from 'src/app/models/enums/enum1/enum1';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-selector-field',
  templateUrl: './selector-field.component.html',
  styleUrls: ['./selector-field.component.scss'],
  standalone: true,
  imports: [CommonModule ,MatFormFieldModule, MatInputModule, MatSelectModule, FormsModule, ReactiveFormsModule],
})
export class SelectorFieldComponent {

  @Input() formSelector !: FormControl;
  @Input() fields!: Enum1[];
  @Input() labelName !:string;
  @Input() errorLabel !:string;
}
