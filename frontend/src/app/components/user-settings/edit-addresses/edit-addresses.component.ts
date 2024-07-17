import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AddressesData } from 'src/app/models/addresses/addressesData';
import { UsersService } from 'src/app/services/user/users.service';
import { DialogComponent } from '../../dialog/dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-edit-addresses',
  templateUrl: './edit-addresses.component.html',
  styleUrls: ['./edit-addresses.component.scss'],
  standalone: true, 
  imports: [CommonModule, MatButtonModule]
})
export class EditAddressesComponent {

  addresses: AddressesData[] = [];
  ref:string | null = localStorage.getItem('isLogged');
  isDelete:boolean = false;

  constructor(private userService: UsersService, private router: Router, public dialog: MatDialog){}

  ngOnInit(){
    localStorage.removeItem("addressChosen");
    this.userService.getAddresses().subscribe({
      next: r=>{
        this.addresses = r.data;
      },error: e =>{
        console.log(e);
        this.router.navigate(['login']);
      }
    });
  }

  openDialog(id:number, address:string){

    const dialogRef = this.dialog.open(DialogComponent, {
      width: '390px',
      data: {
        title: 'Confirmación',
        message: "¿Segur@ que deseas eliminar el domicilio: "+address+ "?"
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.deleteAddress(id).subscribe({
          next:()=>{
            this.isDelete = true;
            setTimeout(() => {
              window.location.reload();
            }, 2000);
          },error:e=>{
            console.log(e);
          }
        });
      }else{
        console.log("Operacion cancelada");
      }
    });
  }
}
