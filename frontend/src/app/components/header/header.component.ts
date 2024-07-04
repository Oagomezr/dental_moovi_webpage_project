import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CartRequest } from 'src/app/models/cart/cartRequest';
import { CategoriesData } from 'src/app/models/categories/categoriesData';
import { ProductsData } from 'src/app/models/products/productsData';
import { AuthenticateService } from 'src/app/services/authenticate/authenticate.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { CategoriesService } from 'src/app/services/categories/categories.service';
import { ProductsService } from 'src/app/services/products/products.service';
import { UsersService } from 'src/app/services/user/users.service';
import { HoverBoxProfileComponent } from "./hover-box-profile/hover-box-profile.component";
import { HoverBoxProductsComponent } from "./hover-box-products/hover-box-products.component";
import { CartBadgeComponent } from "./cart-badge/cart-badge.component";
import { HoverBoxCartComponent } from "./hover-box-cart/hover-box-cart.component";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss'],
    standalone: true,
    imports: [CommonModule, HoverBoxProfileComponent, HoverBoxProductsComponent, CartBadgeComponent, HoverBoxCartComponent]
})
export class HeaderComponent {

  constructor(
    private userSer: UsersService, private productsSer: ProductsService, 
    public cartSer: CartService, private router: Router, 
    private categoriesSer: CategoriesService, private authSer: AuthenticateService){}

  name?: string|null;
  categories?: CategoriesData[];
  cartRequest: CartRequest = { data: [] };
  ref:string | null = localStorage.getItem('isLogged');
  isAuthenticate:boolean = this.ref != null;
  isAdmin: boolean = localStorage.getItem('isAdmin') != null;

  showHoverBoxProfile: boolean = true;
  showHoverBoxProducts: boolean = true;
  showHoverBoxCart: boolean = true;

  products?: ProductsData[];
  notFoundProducts: boolean = false;

  ngOnInit(){
    this.getCategories();
    this.getShoppingCart();
    this.getNameOfUser();
  }

  logout(){
    if(this.ref){
      this.authSer.logoutAction();
    }else{
      console.log("User not login");
    }
  }

  showBoxProfile(show: boolean): void {
    this.showHoverBoxProfile = !show;
  }

  showBoxProducts(show: boolean): void{
    this.showHoverBoxProducts = !show;
  }

  showBoxCart(show: boolean): void{
    if(this.cartSer.cartResponse.amountOfProducts>0) 
      this.showHoverBoxCart = !show;
    else this.showHoverBoxCart = true;
  }

  searchProduct(inputSearch: any){
    let nameProduct: string = inputSearch.target.value;
    let productFound = document.getElementById('search');
    if (nameProduct.length > 1){
      if(productFound) productFound.classList.add('search-found');
      this.productsSer.getProductsBySearch(nameProduct, true, 0, 0).subscribe({
        next: response => {
          this.products = response.data;
          this.notFoundProducts = this.products.length == 0;
        },
        error: error => {
          console.error("Products not found:", error);
        }
      });
    }else{
      this.products = [];
      if(productFound) productFound.classList.remove('search-found');
    }
  }

  selectInputSearch(nameProduct: string){
    if (nameProduct && nameProduct.length > 1) {
      const newRoute = '/category/' + nameProduct + ',Buscar';
      this.router.navigateByUrl(newRoute).then(() => {
        window.location.reload();
      });
    } else {
      let inputVoid = document.getElementById('search-products');
      inputVoid?.focus();
    }
  }

  getCategories(){
    this.categoriesSer.getCategories().subscribe({
      next: responseGetC =>{
        this.categories = responseGetC.data;
      },
      error: error=>{
        console.log('Error to get categories', error.error);
      }
    });
  }

  getNameOfUser(){
    if(this.ref){
      console.log("loged");
      this.userSer.getName().subscribe({
        next: r => {
          this.name = r.infoMessage;
          this.isAuthenticate = true;
        },
        error: e => {
          this.isAuthenticate = false;
          localStorage.clear();
          console.log(e);
        }
      });
    }
  }

  getShoppingCart(){
    if(localStorage.getItem('callerCart')){
      let cartData = JSON.parse(localStorage.getItem('callerCart')!);
      this.cartRequest.data = cartData;
      this.productsSer.getShoppingCartProducts(this.cartRequest).subscribe({
        next: response=>{
          this.cartSer.cartResponse = response;
        },error:e=>{
          console.log(e);
          localStorage.removeItem('callerCart');
        }
      });
    }
  }
}
