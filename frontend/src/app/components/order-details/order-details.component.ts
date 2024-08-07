import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AddressesData } from 'src/app/models/addresses/addressesData';
import { UsersService } from 'src/app/services/user/users.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { CartRequest } from 'src/app/models/cart/cartRequest';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.scss'],
  standalone: true,
  imports:[CommonModule, FormsModule, MatButtonModule]
})
export class OrderDetailsComponent {
  
  constructor(private userService: UsersService, private router: Router, 
    public dialog: MatDialog, public cartSer: CartService){
    localStorage.removeItem("addressChosen");
  }

  addresses: AddressesData[] = [];
  selectedAddressId: string = '';
  selectNotice: boolean = false;
  complete: boolean = false;
  cartRequest: CartRequest = { data:[] , idUser: 0 };
  ref:string | null = localStorage.getItem('isLogged');

  ngOnInit(){
    if(this.cartSer.cartResponse.amountOfProducts == 0 ){ 
      this.router.navigate(['/manageOrders']);
    }
    this.userService.getAddresses().subscribe({
      next: response=>{
        console.log(response);
        if(response.data.length == 0) this.addAddress();
        this.addresses = response.data;
      },error: e =>{
        console.log(e);
        this.router.navigate(['login']);
      }
    });
    localStorage.removeItem("purchase");
  }

  addAddress(){
    localStorage.setItem("addressNotice", "true");
    this.router.navigate(['settings/addresses/address']);
  }

  purchase(){
    if(this.selectedAddressId == ''){
      this.selectNotice = true;
    }else{
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
