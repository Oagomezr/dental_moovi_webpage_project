import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CPW } from 'src/app/models/cPw';
import { message } from 'src/app/models/message';
import { UserAuth } from 'src/app/models/users/userAuth';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticateService {

  private count: number = 0;

  constructor(private readonly http: HttpClient) {}

  login(userAuth: UserAuth): Observable<any> {
    return this.http.post<any>(`${environment.url_back}/public/login`, userAuth);
  }

  checkRole(userAuth: UserAuth): Observable<any> {
    return this.http.post<any>(`${environment.url_back}/public/isAuthorized`, userAuth);
  }

  logout() {
    return this.http.delete(`${environment.url_back}/public/logout`);
  }
  
  uPw(data:CPW, ref:string): Observable<message>{
    return this.http.put<message>(`${environment.url_back}/user/upw/${ref}`, data, {withCredentials:true});
  }

  permission(){
    return this.http.get<boolean>(`${environment.url_back}/user/permission`, {withCredentials:true});
  }

  logoutAction(){
    this.logout().subscribe({
      next: () => {
        console.log("Logout complete");
        localStorage.clear();
        window.location.reload();
      },
      error: error => {
        console.error('Error in logout:', error);
      }
    });
  }
  
  errorCount(){
    this.count +=1;
    if(this.count > 2){
      this.logoutAction();
      this.count = 0;
      window.location.reload();
    }
  }

  
}
