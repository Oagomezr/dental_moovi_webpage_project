import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CartRequest } from 'src/app/models/cart/cartRequest';
import { CartResponse } from 'src/app/models/cart/cartResponse';
import { message } from 'src/app/models/message';
import { ProductsData } from 'src/app/models/products/productsData';
import { ProductsResponse } from 'src/app/models/products/productsResponse';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {

  isAdmin: boolean = localStorage.getItem('isAdmin') != null;

  access: string = this.isAdmin ? "admin" : "public";

  constructor(private http: HttpClient, private router: Router) { }

  getProductsByCategory(name:string, currentPage: number, productsPerPage: number): Observable<ProductsResponse>{
    return this.http.get<ProductsResponse>(`${environment.url_back}/${this.access}/products/category/${name}/${currentPage}/${productsPerPage}`, {withCredentials:this.isAdmin});
  }

  getProductByName(name:string): Observable<ProductsData>{
    return this.http.get<ProductsData>(`${environment.url_back}/${this.access}/products/${name}`, {withCredentials:this.isAdmin});
  }

  getProductsBySearch(search: string, limit: boolean, currentPage: number, productsPerPage: number): Observable<ProductsResponse>{
    return this.http.get<ProductsResponse>(`${environment.url_back}/${this.access}/products/search/${search}/${limit}/${currentPage}/${productsPerPage}`, {withCredentials:this.isAdmin});
  }

  updateMainImage(idImage: number, productName: string): Observable<message>{
    return this.http.put<message>(`${environment.url_back}/admin/products/updateMainImage/${productName}`, idImage, {withCredentials:true});
  }

  uploadImage(file: File, product: string) {
    const formData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post(`${environment.url_back}/admin/products/uploadImage/${product}`, formData, {withCredentials:true});
  }

  deleteImage(parameter:any): Observable<message>{
    return this.http.delete<message>(`${environment.url_back}/admin/products/deleteImage/${parameter}`, {withCredentials:true});
  }

  updateVisibility(visibility: boolean, productName: string): Observable<message>{
    return this.http.put<message>(`${environment.url_back}/admin/products/visibility/${productName}`, !visibility, {withCredentials:true});
  }

  updateProductInfo(option: number, productName: string, newInfo: string): Observable<message>{
    return this.http.put<message>(`${environment.url_back}/admin/products/updateProductInfo/${productName}/${option}`, newInfo, {withCredentials:true});
  }

  updateProductInformation(option:number, productName:string, newValue:any){
    this.updateProductInfo(option, productName, newValue).subscribe({
      next: ()=>{
        if(option == 0){
          this.router.navigate([`/product/${newValue}`]).then(() => {
            window.location.reload();
          });
        }else{
          window.location.reload();
        }
      },
      error: error=>{
        console.error(error);
      }
    });
  }

  createProduct(categoryName: string): Observable<boolean> {
    return this.http.post<boolean>(`${environment.url_back}/admin/products/createProduct`, categoryName, {withCredentials:true});
  }

  getShoppingCartProducts(store: CartRequest): Observable<CartResponse>{
    return this.http.post<CartResponse>(`${environment.url_back}/${this.access}/shoppingCart`, store, {withCredentials:this.isAdmin});
  }

}
