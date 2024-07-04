import { Component, Input, OnInit } from '@angular/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatNativeDateModule} from '@angular/material/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-date-field',
  templateUrl: './date-field.component.html',
  styleUrls: ['./date-field.component.scss'],
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatDatepickerModule, MatNativeDateModule, FormsModule, ReactiveFormsModule],
})
export class DateFieldComponent implements OnInit{
  @Input() formBirthDate : FormControl = new FormControl(new Date(1990, 0, 1));
  @Input() labelName: string = '';
  startDate = new Date(1990, 0, 1);
  ngOnInit(): void {
    let currentDate = new Date();
    let eighteenYearsAgo = currentDate.getFullYear() - 18;
    this.startDate = new Date(eighteenYearsAgo, currentDate.getMonth(), currentDate.getDate());
  }
  
}
