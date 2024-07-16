import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AddressesData } from 'src/app/models/addresses/addressesData';
import { CartRequest } from 'src/app/models/cart/cartRequest';
import { Users } from 'src/app/models/users/users';
import { CartService } from 'src/app/services/cart/cart.service';
import { UsersService } from 'src/app/services/user/users.service';

@Component({
  selector: 'app-order-admin',
  standalone: true,
  imports:[CommonModule, FormsModule, MatButtonModule],
  templateUrl: './order-admin.component.html',
  styleUrl: './order-admin.component.scss'
})
export class OrderAdminComponent {

  constructor(private userService: UsersService, private router: Router, 
    public dialog: MatDialog, public cartSer: CartService){
    localStorage.removeItem("addressChosen");
  }

  isAdmin: boolean = localStorage.getItem('isAdmin') != null;
  addresses: AddressesData[] = [];
  users: Users[] = [];
  selectedAddressId: string = '';
  selectedUserId: string = '';
  selectNotice: boolean = false;
  complete: boolean = false;
  cartRequest: CartRequest = { data:[] , idUser: 0 };
  ref:string | null = localStorage.getItem('isLogged');

  ngOnInit(){
    if(!this.isAdmin) this.router.navigate(['']); 
    
    this.userService.getUsers().subscribe({
      next: r =>{
        console.log(r);
        this.users = r.data;
      },error: e =>{
        console.log(e);
      }
    });
  }

  handleKeydown(event: KeyboardEvent, userId: number): void {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault(); 
    }
  }

  searchAddresses(id:number){
    if(id != Number(this.selectedUserId)){
      this.userService.getAddressesAdmin(id).subscribe({
        next: r=>{
          this.addresses = r.data;
        },error: e =>{
          console.log(e);
          this.router.navigate(['login']);
        }
      });
    }
  }

  purchase(){
    if(this.selectedAddressId == ''){
      this.selectNotice = true;
    }else{
      this.cartRequest.idUser = Number(this.selectedUserId);
      this.cartRequest.data = JSON.parse(localStorage.getItem('callerCart')!);
      this.cartSer.generateOrder(this.cartRequest, +this.selectedAddressId).subscribe({
        next: ()=>{
          this.complete = true;
          localStorage.removeItem("callerCart");
          setTimeout(() => {
            this.router.navigate(['/manageOrders']);
            window.location.reload();
          }, 3000);
        },error: e =>{
          console.log(e);
        }
      });
    }
  }
}
