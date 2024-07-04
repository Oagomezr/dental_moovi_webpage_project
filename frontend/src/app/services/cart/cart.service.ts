import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CartRequest } from 'src/app/models/cart/cartRequest';
import { CartResponse } from 'src/app/models/cart/cartResponse';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  isAdmin: boolean = localStorage.getItem('isAdmin') != null;
  access: string = this.isAdmin ? "admin" : "user";
  cartResponse: CartResponse = { data: [], total:0, amountOfProducts:0};

  constructor(private http: HttpClient) {}

  generateOrder(req: CartRequest, idAddress: number): Observable<HttpResponse<Blob>> {
    const url = `${environment.url_back}/${this.access}/generateOrder/${idAddress}`;
    
    return this.http.post(url, req, {
      observe: 'response',
      responseType: 'blob',
      withCredentials: true,
    });
  }
}
