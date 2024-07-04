import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { OrderResponse } from 'src/app/models/orders/ordersResponse';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  isAdmin: boolean = localStorage.getItem('isAdmin') != null;
  access: string = this.isAdmin ? "admin" : "public";

  constructor(private http: HttpClient) { }

  getPdf(id: number): Observable<Blob> {
    return this.http.get(`${environment.url_back}/${this.access}/order/${id}`, { responseType: 'blob', withCredentials:true });
  }
  
  getPdfsList(status:string, orderBy:boolean):  Observable<OrderResponse>{
    return this.http.get<OrderResponse>(`${environment.url_back}/admin/order/${status}/${orderBy}`, { withCredentials:true });
  }
}
