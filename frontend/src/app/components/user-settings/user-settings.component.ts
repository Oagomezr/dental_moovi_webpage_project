import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PasswordFieldComponent } from "../form-fields-components/password-field/password-field.component";
import { MatButtonModule } from '@angular/material/button';
import { AuthenticateService } from 'src/app/services/authenticate/authenticate.service';

@Component({
    selector: 'app-user-settings',
    templateUrl: './user-settings.component.html',
    styleUrls: ['./user-settings.component.scss'],
    standalone: true,
    imports: [CommonModule, PasswordFieldComponent, MatButtonModule, FormsModule, ReactiveFormsModule]
})
export class UserSettingsComponent {
  
  constructor(private router: Router, private authSer: AuthenticateService){}

  isLogged: boolean = localStorage.getItem('isLogged') != null;
  pwUpdated: boolean = false;
  pwError: boolean = false;

  cache:string = localStorage.getItem('isLogged')!;

  changePasswordFormGroup = new FormGroup({
    oldP: new FormControl('', [Validators.required]),
    newP: new FormControl('', [Validators.required])
  });

  userUpdated:boolean = localStorage.getItem('userUpdated') != null;

  changePassword(){
    console.log(this.changePasswordFormGroup.value);
    this.authSer.uPw(this.changePasswordFormGroup.value, this.cache).subscribe({
      next: reponse =>{
        console.log(reponse);
        this.pwUpdated = true;
        this.pwError = false;
      },
      error: e =>{
        console.log(e);
        this.pwError = true;
        this.pwUpdated = false;
        this.authSer.errorCount();
      }
    });
  }
  
  ngOnInit(){ 
    if(!this.isLogged) this.router.navigate(['/']); 
    if(this.userUpdated) {
      window.location.reload();
      localStorage.removeItem('userUpdated');
    }
  }
  
}
