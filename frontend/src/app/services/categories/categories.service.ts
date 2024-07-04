import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CategoriesResponse } from 'src/app/models/categories/categoriesResponse';
import { message } from 'src/app/models/message';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  constructor(private http: HttpClient) {}

  getCategories(): Observable<CategoriesResponse>{
    return this.http.get<CategoriesResponse>(`${environment.url_back}/public/categories`);
  }

  updateCategoryName(categoryName: string, newName: string): Observable<message>{
    return this.http.put<message>(`${environment.url_back}/admin/categories/updateName/${categoryName}`, newName, {withCredentials:true});
  }

  updateCategoryLocation(categoryName: string, newName: string): Observable<message>{
    return this.http.put<message>(`${environment.url_back}/admin/categories/updateLocation/${categoryName}`, newName, {withCredentials:true});
  }

  createCategory(parentCategoryName: string, newCategoryName: string): Observable<message>{
    return this.http.post<message>(`${environment.url_back}/admin/categories/create/${parentCategoryName}`, newCategoryName, {withCredentials:true});
  }

  deleteCategory(categoryName: string): Observable<message>{
    return this.http.delete<message>(`${environment.url_back}/admin/categories/delete/${categoryName}`, {withCredentials:true});
  }
}
