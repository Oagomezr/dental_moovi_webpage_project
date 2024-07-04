import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { CartResponse } from 'src/app/models/cart/cartResponse';
import { CartDtoRequest } from 'src/app/models/cart/cartStore';

@Component({
  selector: 'app-hover-box-cart',
  templateUrl: './hover-box-cart.component.html',
  styleUrls: ['./hover-box-cart.component.scss'],
  standalone: true,
  imports:[CommonModule]
})
export class HoverBoxCartComponent {

  constructor (private router: Router){}

  isAdmin: boolean = localStorage.getItem('isAdmin') != null;
  callerCart: CartDtoRequest[] = localStorage.getItem('callerCart') ? JSON.parse(localStorage.getItem('callerCart')!) : [];

  @Input() cartResponse!: CartResponse;

  updateAmounts(newValue: any, index: number){
    if (+newValue.value > this.cartResponse.data[index].amount) {
      this.cartResponse.amountOfProducts += (+newValue.value-this.cartResponse.data[index].amount);
    } else if (+newValue.value < this.cartResponse.data[index].amount) {
      this.cartResponse.amountOfProducts -= (this.cartResponse.data[index].amount - +newValue.value);
    } else {
      console.log('El valor sigue siendo el mismo.');
    }

    this.cartResponse.total -= this.cartResponse.data[index].subtotal;
    this.cartResponse.data[index].amount = +newValue.value;
    this.cartResponse.data[index].subtotal = +newValue.value * this.cartResponse.data[index].prize;
    this.cartResponse.total += this.cartResponse.data[index].subtotal;
    
    
    this.callerCart.forEach(elem => {
      if(elem.id == this.cartResponse.data[index].id){
          elem.amount = +newValue.value;
          localStorage.setItem('callerCart', JSON.stringify(this.callerCart));
      }
    });

  }

  updatePrize(newValue: any, index: number){
    this.cartResponse.total -= this.cartResponse.data[index].subtotal;
    this.cartResponse.data[index].prize = +newValue.value;
    this.cartResponse.data[index].subtotal = +newValue.value * this.cartResponse.data[index].amount;
    this.cartResponse.total += this.cartResponse.data[index].subtotal;

    this.callerCart.forEach(elem => {
      if(elem.id == this.cartResponse.data[index].id){
          elem.prize = +newValue.value;
          localStorage.setItem('callerCart', JSON.stringify(this.callerCart));
      }
    });
  }

  deleteElem(index: number){

    this.cartResponse.amountOfProducts -= this.cartResponse.data[index].amount;
    this.cartResponse.total -= this.cartResponse.data[index].subtotal;

    this.callerCart = this.callerCart.filter(elem=>
      elem.id !== this.cartResponse.data[index].id
    );
    this.cartResponse.data.splice(index,1);
    localStorage.setItem('callerCart', JSON.stringify(this.callerCart));
  }

  purchase(){
    if(localStorage.getItem('isLogged') == null){
      localStorage.setItem('shoppingNotice', "true");
      this.router.navigate(['/login']);
    }else{
      localStorage.setItem('purchase', "true");
      this.isAdmin ? this.router.navigate(['/orderAdmin']) : this.router.navigate(['/orderDetails']);
    }
  }
}
