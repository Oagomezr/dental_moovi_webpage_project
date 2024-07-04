import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AddressesData } from 'src/app/models/addresses/addressesData';
import { UsersService } from 'src/app/services/user/users.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CelPhoneFieldComponent } from "../../../form-fields-components/cel-phone-field/cel-phone-field.component";
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { EnumsService } from 'src/app/services/enums/enums.service';
import { Enum1 } from 'src/app/models/enums/enum1/enum1';
import { AutocompleteFieldComponent } from 'src/app/components/form-fields-components/autocomplete-field/autocomplete-field.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSelectModule } from '@angular/material/select';

@Component({
    selector: 'app-edit-address',
    templateUrl: './edit-address.component.html',
    styleUrls: ['./edit-address.component.scss'],
    standalone: true,
    imports: [CommonModule, MatFormFieldModule, AutocompleteFieldComponent,
      ReactiveFormsModule, CelPhoneFieldComponent, MatInputModule, MatButtonModule, MatAutocompleteModule,
      MatSlideToggleModule, MatSelectModule]
})
export class EditAddressComponent {

  addressInfo : AddressesData = localStorage.getItem('addressChosen') ? JSON.parse(localStorage.getItem('addressChosen')!) : {
    id:0,
    departament: '',
    location: '',
    address: '',
    phone: '',
    description: '',
    idMunicipaly: 0
  }

  ref:string | null = localStorage.getItem('isLogged');
  isEdit:boolean = this.addressInfo.id != 0;
  addressNotice: boolean = false;

  constructor(private userService: UsersService, private router: Router, private enumSer: EnumsService){
    console.log(this.addressInfo);
    if(localStorage.getItem('addressNotice') != null){
      localStorage.removeItem("addressNotice");
      this.addressNotice = true;
    }
    if(this.ref == null) this.router.navigate(['/']);
  }

  isUpdated: boolean = false;

  addressFormGroup = new FormGroup({
    id: new FormControl( this.addressInfo.id ),
    departament: new FormControl(this.addressInfo.departament),
    location: new FormControl(this.addressInfo.location, { updateOn: 'blur'}),
    address: new FormControl( this.addressInfo.address, { updateOn: 'blur'}),
    phone: new FormControl( this.addressInfo.phone, { validators: [Validators.required, Validators.minLength(12), Validators.pattern('^[0-9-]*$')]}),
    description: new FormControl( this.addressInfo.description),
    idMunicipaly: new FormControl( this.addressInfo.idMunicipaly, { validators: [Validators.required], updateOn: 'blur'}),
  });

  send(){
    if(this.addressInfo.id == 0){
      console.log(this.addressFormGroup);
      this.userService.addAddress(this.addressFormGroup.value).subscribe({
        next: () =>{
          this.isUpdated=true;
          setTimeout(() => {
            if(this.addressNotice){
              this.router.navigate(['/orderDetails']);
            }else{
              this.router.navigate(['/settings/addresses']);
            }
          }, 2000);
        },error: e =>{
          console.error(e);
          this.router.navigate(['/settings/addresses']);
        }
      });
    }else{
      this.userService.updateAddress(this.addressFormGroup.value).subscribe({
        next: () =>{
          this.isUpdated=true;
          setTimeout(() => {
            this.router.navigate(['/settings/addresses']);
          }, 2000);
        },error: e =>{
          console.error(e);
          this.router.navigate(['/settings/addresses']);
        }
      });
    }
  }

  departamens!: Enum1[];
  
  searchDepartaments(value:string){
    if (value.length >1) {
      this.enumSer.getDepartaments(value).subscribe({
        next: response => {
          this.departamens = response.data;
        },
        error: error => {
          console.error("Departaments not found: ", error);
        }
      });
    } else {
      this.departamens = [];
    }
  }

  idDepartament: number = this.addressInfo.location == '' ? 0 : -1;
  municipalies: Enum1[] = [];
  setDepartament(id:number){
    console.log(id);
    this.idDepartament = id;
    this.addressFormGroup.get("idMunicipaly")?.setValue(0);
    this.municipalies = [];
  }

  searchMunicipalies(value:string){
    let departament = this.idDepartament;
    if (value.length >1 && departament != 0) {
      this.enumSer.getMunicipalies(value, departament).subscribe({
        next: response => {
          this.municipalies = response.data;
        },
        error: error => {
          console.error("Municipaly not found:", error);
        }
      });
    } else this.municipalies = [];
    
    if(this.addressInfo.departament != ''){
      this.idDepartament = 0;
      this.addressInfo.departament = '';
    } 
  }

  setMunicipaly(id:number){
    this.addressFormGroup.get("idMunicipaly")?.setValue(id);
    console.log(this.addressFormGroup)
  }
}
