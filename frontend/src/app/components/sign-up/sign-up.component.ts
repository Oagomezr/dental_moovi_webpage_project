import { Component } from '@angular/core';
import { UsersService } from 'src/app/services/user/users.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { uniqueValueValidator } from 'src/app/validators/userFieldsValidator';
import { Router } from '@angular/router';
import { AsyncPipe, CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { DateFieldComponent } from "../form-fields-components/date-field/date-field.component";
import { SelectorFieldComponent } from "../form-fields-components/selector-field/selector-field.component";
import { CelPhoneFieldComponent } from "../form-fields-components/cel-phone-field/cel-phone-field.component";
import { PasswordFieldComponent } from "../form-fields-components/password-field/password-field.component";
import { ConfirmCodeFieldComponent } from "../form-fields-components/confirm-code-field/confirm-code-field.component";
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { Enum1 } from 'src/app/models/enums/enum1/enum1';
import { NamesFieldComponent } from '../form-fields-components/names-field/names-field.component';
import { AutocompleteFieldComponent } from '../form-fields-components/autocomplete-field/autocomplete-field.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@Component({
    selector: 'app-sign-up',
    templateUrl: './sign-up.component.html',
    styleUrls: ['./sign-up.component.scss'],
    standalone: true,
    imports: [
      CommonModule, MatFormFieldModule, ReactiveFormsModule, DateFieldComponent, AutocompleteFieldComponent,
      SelectorFieldComponent, CelPhoneFieldComponent, PasswordFieldComponent, ConfirmCodeFieldComponent, 
      MatInputModule, MatButtonModule, MatSelectModule, MatDatepickerModule, MatAutocompleteModule,
      MatNativeDateModule, MatIconModule, NamesFieldComponent, AsyncPipe]
})
export class SignUpComponent {

  isAuthenticate:boolean = localStorage.getItem('isLogged') != null; 
  isAdmin: boolean = localStorage.getItem('isAdmin') != null;

  ngOnInit(){
    if(this.isAuthenticate && !this.isAdmin) this.router.navigate(['/']);
    
  }

  enterprises!: Enum1[];

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
      asyncValidators: uniqueValueValidator(this.userService, true),
      updateOn: 'blur'}),

    enterprise: new FormControl('', { validators: 
      [Validators.pattern('^[^0-9,!@#$%^&*()_+={}<>?/|\'":;`~]*$'), Validators.minLength(3)], 
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

  genders:Enum1[] = this.userService.genders;

  confirm : boolean = false;

  email : string = '';

  constructor(private userService: UsersService, private router: Router){}

  sendCode(): void {
    if(this.isAdmin){
      this.confirm = true;
      this.confirmCode('12');
      return;
    }
    if(this.userFormGroup.valid){
      this.organizeInformation();
      this.email = this.userFormGroup.get('email')?.value ?? '';
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

  searchEnterprises(event:any){
    let value = event.target.value;
    /* this.userFormGroup.get('enterprise')?.setValue(value);
    console.log(this.userFormGroup.get('enterprise')); */
    if (value.length >1) {
      this.userService.getEnterprises(value).subscribe({
        next: r => {
          this.enterprises = r.data;
        },
        error: error => {
          console.error("Enterprise not found: ", error);
        }
      });
    } else {
      this.enterprises = [];
    }
  }

  //process each field to have organized information to database
  private organizeInformation(): void{
    //the fields we'll organize
    const fields = ["username", "firstName", "lastName", "email"];

    Object.keys(this.userFormGroup.controls).forEach(key => { //iterate all fields
      if(fields.includes(key)){ //select only fields we'll organize
        let keyValue = this.userFormGroup.get(key)?.value;
        this.userFormGroup.get(key)?.setValue(this.changeTheText(keyValue));
      }
    });
  }

  //It allow us to change the first letter to upper case and the rest lower case at start or after a space
  private changeTheText(value: string): string {
    let word = value.charAt(0).toUpperCase()+ value.slice(1).toLowerCase(); //Change the first letter to upper case and the rest lower case
    word = word.normalize('NFD').replace(/[\u0300-\u036f]/g, ''); //replace all accents letters to normal letters
    word = word.replace(/(\s|^)\w/g, (l) => l.toUpperCase()); //transform to upperCase after the space character
    return word.trim().replace(/\s+/g, ' '); //delete all spaces at the start and allow only one space between letters
  }

  typeCode(index: number, event: any) {
    let character = event.target.value;

    let charaters = this.userFormGroup.get('code')?.value?.split('') ?? [''];
    
    //If the entered value is numeric, add and focus the next input
    if (/^\d*$/.test(character) && character != '') {

      charaters[index] = character;
      this.userFormGroup.get('code')?.setValue(charaters.join(''));

      let nextInput = event.target.nextElementSibling as HTMLInputElement;
      if (nextInput) {
        nextInput.focus();
      }
      console.log(this.userFormGroup.get('code')?.value);

    } else {
      //If the entered value is not numeric, delete the content
      event.target.value = '';
    }
  }
  
  showRegisterOK: boolean = false;
  badCode: boolean = false;
  confirmCode(code: string){
    this.userFormGroup.get('code')?.setValue(code);
    if (/^\d*$/.test(this.userFormGroup.get('code')?.value || 'x')) {
      this.userService.createUser(this.userFormGroup.value).subscribe({
        next: () => {
          this.showRegisterOK = true;
          localStorage.removeItem("register");
          localStorage.setItem("register", "true");
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 3000);
        },
        error: error => {
          console.error(error);
          this.badCode = true;
        }
      });
    }
  }
}
