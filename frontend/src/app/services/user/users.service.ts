import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Users } from 'src/app/models/users/users';
import { message } from 'src/app/models/message';
import { AddressesData } from 'src/app/models/addresses/addressesData';
import { AddressesResponse } from 'src/app/models/addresses/addressesResponse';
import { Enum1 } from 'src/app/models/enums/enum1/enum1';
import { UserAuth } from 'src/app/models/users/userAuth';
import { environment } from 'src/environments/environment';
import { UserResponse } from 'src/app/models/users/userResponse';
import { Enum1Response } from 'src/app/models/enums/enum1/enum1Response';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  isAdmin: boolean = localStorage.getItem('isAdmin') != null;
  access: string = this.isAdmin ? "admin" : "public";

  constructor(private http: HttpClient) {}

  createUser(user: Users): Observable<void> {
    return this.http.post<void>(`${environment.url_back}/${this.access}/create`, user , {withCredentials:this.isAdmin});
  }

  sendEmailNotification(email:string): Observable<void>{
    return this.http.post<void>(`${environment.url_back}/public/sendEmail`, email);
  }

  getName(): Observable<message> {
    return this.http.get<message>(`${environment.url_back}/user/name`, {withCredentials:true});
  }

  getUser(): Observable<Users>{
    return this.http.get<Users>(`${environment.url_back}/user/getUser`, {withCredentials:true});
  }

  getUsers(): Observable<UserResponse>{
    return this.http.get<UserResponse>(`${environment.url_back}/admin/getUsers`, {withCredentials:true});
  }

  checkIfValueExists(email: string, signup: boolean) {
    return this.http.get<boolean>(`${environment.url_back}/public/${email}/${signup}`);
  }

  updateUser(user: Users): Observable<message>{
    return this.http.put<message>(`${environment.url_back}/user/update`, user, {withCredentials:true});
  }

  addAddress(address:AddressesData): Observable<message>{
    return this.http.post<message>(`${environment.url_back}/user/addAddress`, address, {withCredentials:true});
  }

  getAddresses(): Observable<AddressesResponse>{
    return this.http.get<AddressesResponse>(`${environment.url_back}/user/getAddresses`, {withCredentials:true});
  }

  getAddressesAdmin(id:number): Observable<AddressesResponse>{
    return this.http.get<AddressesResponse>(`${environment.url_back}/admin/getAddresses/${id}`, {withCredentials:true});
  }

  updateAddress(address:AddressesData): Observable<message>{
    return this.http.put<message>(`${environment.url_back}/user/updateAddress`, address, {withCredentials:true});
  }

  deleteAddress(id:number): Observable<message>{
    return this.http.delete<message>(`${environment.url_back}/user/deleteAddress/${id}`, {withCredentials:true});
  }

  rPw(userCredentials:UserAuth): Observable<message>{
    return this.http.put<message>(`${environment.url_back}/public/rpw`, userCredentials, {withCredentials:true});
  }

  getEnterprises(name:string): Observable<Enum1Response>{
    return this.http.get<Enum1Response>(`${environment.url_back}/public/enterprises/${name}`, {withCredentials:false});
  }

  genders:Enum1[] = [
    {id:'MALE', description: 'Masculino'},
    {id:'FEMALE', description: 'Femenino'},
    {id:'OTHER', description: 'Otro'},
    {id:'UNDEFINED', description: 'Prefiero no decirlo'}
  ];
}
