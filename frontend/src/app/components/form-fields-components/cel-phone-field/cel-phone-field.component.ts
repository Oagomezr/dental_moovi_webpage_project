import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-cel-phone-field',
  templateUrl: './cel-phone-field.component.html',
  styleUrls: ['./cel-phone-field.component.scss'],
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatIconModule, FormsModule, ReactiveFormsModule, CommonModule],
})
export class CelPhoneFieldComponent {
  @Input() formCelphone : FormControl = new FormControl('');
  isPhoneNumberComplete: boolean = true;

  formatPhoneNumber(event: KeyboardEvent) {
    let value = this.formCelphone.value;
    let lastCharacter = value.slice(-1);

    if ((value.length == 3 || value.length == 7) && event.key != 'Backspace' && !/\D/.test(lastCharacter)) {
      this.formCelphone.setValue(value + '-');
    }else if(event.key != 'Backspace' && /[^0-9-]/.test(lastCharacter)){
      this.formCelphone.setValue(value.slice(0, -1));
    }
  }

  
}
