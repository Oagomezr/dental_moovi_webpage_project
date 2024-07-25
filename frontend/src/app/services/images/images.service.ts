import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ImageResponse } from 'src/app/models/images/imgResponse';
import { message } from 'src/app/models/message';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ImagesService {

  constructor(private http: HttpClient) { }

  getCarousel(): Observable<ImageResponse> {
    return this.http.get<ImageResponse>(`${environment.url_back}/public/carousel`);
  }

  uploadImage(file: File) {
    const formData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post(`${environment.url_back}/admin/uploadImage`, formData, {withCredentials:true});
  }

  deleteImage(parameter:number): Observable<message>{
    return this.http.delete<message>(`${environment.url_back}/admin/deleteImage/${parameter}`, {withCredentials:true});
  }

}
