import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
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

  ref:string | null = localStorage.getItem('isLogged');
  departamens!: Enum1[];
  idDepartament: number = 0;
  municipalies: Enum1[] = [];
  addressNotice: boolean = false;
  isUpdated: boolean = false;
  isEdit:boolean = false;
  user:number = 0;

  constructor(
    private userService: UsersService, private router: Router, private enumSer: EnumsService, private route: ActivatedRoute
  ){}

  ngOnInit(){
    if(this.ref == null) this.router.navigate(['/']);

    if(localStorage.getItem('addressNotice') != null){
      localStorage.removeItem("addressNotice");
      this.addressNotice = true;
    }

    this.route.params.subscribe((params: Params) => {
      this.user = params['user'];
      let address = params['address'];
      this.isEdit = address != 0;
      if(address != 0) this.getAddress(address);
    });
  }

  getAddress(id:number){
    this.userService.getAddress(id).subscribe({
      next: r => {
        //this.response = true;
        this.idDepartament = r.location == '' ? 0 : -1;
        this.addressFormGroup.patchValue({
          id: r.id,
          departament: r.departament,
          location: r.location,
          address: r.address,
          phone: r.phone,
          description: r.departament,
          idMunicipaly: r.idMunicipaly
        });
      },
      error: error => {
        //localStorage.clear();
        console.log("Error to get user info", error);
        //window.location.reload();
      }
    });
  }

  addressFormGroup = new FormGroup({
    id: new FormControl(0),
    departament: new FormControl(''),
    location: new FormControl('', { updateOn: 'blur'}),
    address: new FormControl( '', { updateOn: 'blur'}),
    phone: new FormControl( '', { validators: [Validators.required, Validators.minLength(12), Validators.pattern('^[0-9-]*$')]}),
    description: new FormControl( ''),
    idMunicipaly: new FormControl( 0, { validators: [Validators.required], updateOn: 'blur'}),
  });

  send(){
    this.isEdit ? this.updateAddress() : this.createAddress();
  }

  createAddress(){
    this.user != 0 ? this.addAddressByAdmin() : this.addAddress();
  }

  addAddressByAdmin(){
    this.userService.addAddressAdmin(this.addressFormGroup.value, this.user).subscribe({
      next: () =>{
        this.isUpdated=true;
        setTimeout(() => {
          this.router.navigate(['/orderAdmin']);
        }, 2000);
      },error: e =>{
        console.error(e);
        this.router.navigate(['/settings/addresses']);
      }
    });
  }

  addAddress(){
    this.userService.addAddress(this.addressFormGroup.value).subscribe({
      next: () =>{
        this.isUpdated=true;
        setTimeout(() => {

          let route = this.addressNotice ? '/orderDetails' : '/settings/addresses';
          this.router.navigate([route]);

        }, 2000);
      },error: e =>{
        console.error(e);
        this.router.navigate(['/settings/addresses']);
      }
    });
  }

  updateAddress(){
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

  setDepartament(id:number){
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
    
    if(this.addressFormGroup.get('departament')?.value != ''){
      this.idDepartament = 0;
      this.addressFormGroup.get('departament')?.setValue('');
    } 
  }

  setMunicipaly(id:number){
    this.addressFormGroup.get("idMunicipaly")?.setValue(id);
    console.log(this.addressFormGroup)
  }
}
