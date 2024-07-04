import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { uniqueValueValidator } from 'src/app/validators/userFieldsValidator';
import { UsersService } from 'src/app/services/user/users.service';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ConfirmCodeFieldComponent } from '../form-fields-components/confirm-code-field/confirm-code-field.component';

@Component({
  selector: 'app-forgotten-pw',
  standalone: true,
  imports: [CommonModule, MatFormFieldModule, ReactiveFormsModule, 
    ConfirmCodeFieldComponent, MatInputModule, MatButtonModule],
  templateUrl: './forgotten-pw.component.html',
  styleUrls: ['./forgotten-pw.component.scss']
})
export class ForgottenPwComponent {

  ngOnInit(){
    if(localStorage.getItem('isLogged') != null){
      this.router.navigate(['/']);
    }
  }

  constructor(private userService: UsersService, private router: Router){}

  email : string = '';
  confirm: boolean = false;
  badCode: boolean = false;
  showOK: boolean = false;

  emailFormGroup = new FormGroup({
    userName: new FormControl('', { validators: [Validators.required],
        asyncValidators: [uniqueValueValidator(this.userService, false)],
        updateOn: 'blur'}),
    password: new FormControl(''),
    code: new FormControl('------')
  });


  confirmCode(code: string){
    this.emailFormGroup.get('code')?.setValue(code);
    if (/^\d*$/.test(this.emailFormGroup.get('code')?.value || 'x')) {
      this.userService.rPw(this.emailFormGroup.value).subscribe({
        next: r =>{
          console.log(r);
          this.showOK = true;
        },error: e=>{
          console.error(e.error.message);
        }
      });
    }
  }

  sendCode(): void {
    if(this.emailFormGroup.valid){
      this.email = this.emailFormGroup.get('userName')?.value ?? '';
      this.userService.sendEmailNotification(this.email).subscribe({
        next: () => {
          this.confirm = true;
        },
        error: error => {
          console.error(error.error.message);
        }
      });
    }
  }
}
