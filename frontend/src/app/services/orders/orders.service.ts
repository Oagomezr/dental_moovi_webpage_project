import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { OrderResponse } from 'src/app/models/orders/ordersResponse';
import { message } from 'src/app/models/message';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  isAdmin: boolean = localStorage.getItem('isAdmin') != null;
  access: string = this.isAdmin ? "admin" : "user";

  constructor(private http: HttpClient) { }

  getPdf(id: number): Observable<Blob> {
    return this.http.get(`${environment.url_back}/${this.access}/order/${id}`, { responseType: 'blob', withCredentials:true });
  }
  
  getPdfsList():  Observable<OrderResponse>{
    return this.http.get<OrderResponse>(`${environment.url_back}/${this.access}/order`, { withCredentials:true });
  }

  updateStatusOrder(idOrder: number, status: string): Observable<message>{
    return this.http.put<message>(`${environment.url_back}/admin/order/${status}`, idOrder, {withCredentials:true});
  }
}
