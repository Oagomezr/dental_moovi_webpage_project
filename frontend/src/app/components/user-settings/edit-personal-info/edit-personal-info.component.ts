import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/services/user/users.service';
import { uniqueValueValidator } from 'src/app/validators/userFieldsValidator';
import { DateFieldComponent } from "../../form-fields-components/date-field/date-field.component";
import { SelectorFieldComponent } from "../../form-fields-components/selector-field/selector-field.component";
import { CelPhoneFieldComponent } from "../../form-fields-components/cel-phone-field/cel-phone-field.component";
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Enum1 } from 'src/app/models/enums/enum1/enum1';
import { NamesFieldComponent } from '../../form-fields-components/names-field/names-field.component';

@Component({
    selector: 'app-edit-personal-info',
    templateUrl: './edit-personal-info.component.html',
    styleUrls: ['./edit-personal-info.component.scss'],
    standalone: true,
    imports: [
      CommonModule, MatFormFieldModule, ReactiveFormsModule, DateFieldComponent, 
      SelectorFieldComponent, CelPhoneFieldComponent, MatFormFieldModule, MatInputModule, 
      MatButtonModule, NamesFieldComponent]
})
export class EditPersonalInfoComponent implements OnInit{
  isUpdated: boolean = false;
  birthdate:string = '';
  ref:string | null = localStorage.getItem('isLogged');
  isLogged: boolean = this.ref != null;
  response: boolean = false;
  genders:Enum1[] = this.userSer.genders;

  constructor(private userSer: UsersService, private router: Router){}

  ngOnInit(){
    if(!this.isLogged) this.router.navigate(['/']);

    this.userSer.getUser().subscribe({
      next: r => {
        console.log(r);
        this.response = true;
        this.userFormGroup.patchValue({
          firstName: r.firstName,
          lastName: r.lastName,
          celPhone: r.celPhone,
          birthdate: r.birthdate+'T00:00:00',
          gender: r.gender
        });
      },
      error: error => {
        localStorage.clear();
        console.log("Error to get user info", error);
        window.location.reload();
      }
    });
  }

  userFormGroup = new FormGroup({
    idUser: new FormControl(0),

    firstName: new FormControl('', { validators: 
      [Validators.required, Validators.pattern('^[^0-9,!@#$%^&*()_+={}<>?/|\'":;`~]*$'), Validators.minLength(3)], 
      updateOn: 'blur'}),

    lastName: new FormControl('', { validators: 
      [Validators.required, Validators.pattern('^[^0-9,!@#$%^&*()_+={}<>?/|\'":;`~]*$'), Validators.minLength(3)], 
      updateOn: 'blur'}),
    
    email: new FormControl( '', { validators: 
      [Validators.required, Validators.email],
      asyncValidators: uniqueValueValidator(this.userSer, true),
      updateOn: 'blur'}),

    celPhone: new FormControl('', { validators: 
      [Validators.required, Validators.minLength(12), Validators.pattern('^[0-9-]*$')]}),
    
    birthdate: new FormControl(''),

    gender: new FormControl('', Validators.required),

    password: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
      Validators.pattern(/^(?=.*[a-zA-Z])(?=.*\d).+$/)
    ]),

    code: new FormControl('------')
  });

  update(){
    this.userSer.updateUser(this.userFormGroup.value).subscribe({
      next: () => {
        this.isUpdated=true;
        setTimeout(() => {
          this.router.navigate(['/settings']);
          localStorage.setItem('userUpdated','updated');
        }, 2000);
      },
      error: error => {
        console.error(error);
      }
    });
  }
}
