import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-confirm-code-field',
  templateUrl: './confirm-code-field.component.html',
  styleUrls: ['./confirm-code-field.component.scss'],
  standalone: true,
  imports:[CommonModule, FormsModule, MatButtonModule]
})
export class ConfirmCodeFieldComponent {

  ngOnInit(){
    this.waitTime = 60;
    this.counter();
  }

  @Input() badCode: boolean = false;
  @Input() email: string = '';
  @Output() confirm = new EventEmitter<string>();
  @Output() reSend = new EventEmitter<void>();

  inputValues: string[] = ['', '', '', '', '', ''];

  code: string = '------';
  waitTime: number = 0;

  typeCode(index: number, event: any) {
    const character = event.target.value;
    const isNumeric = /^\d*$/.test(character) && character !== '';
    const nextInput = event.target.nextElementSibling as HTMLInputElement;

    if (character.length === 1) {
      if (isNumeric) {
        let characters = this.code?.split('') || [''];
        characters[index] = character;
        this.code = characters.join('');
        if (nextInput) nextInput.focus();
      } else {
        event.target.value = '';
      }
    } else if (character.length === 6 && isNumeric) {
        this.code = character;
        this.inputValues = character.split('');
    } else if (character.length > 2) {
        alert("El código pegado es incorrecto");
        this.inputValues = ['', '', '', '', '', ''];
        event.target.value = '';
    } else
        event.target.value = event.target.value.slice(0, -1);
  }

  
  counter(){
    setTimeout(() => {
      this.waitTime -= 1;
      if(this.waitTime > 0) this.counter();
    }, 1000);
  }

  confirmCode(){
    this.confirm.emit(this.code);
  }

  resendCode(){
    this.reSend.emit();
    this.waitTime = 60;
    this.counter();
  }
}
