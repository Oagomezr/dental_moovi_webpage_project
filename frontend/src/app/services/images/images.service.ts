import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ImagesService {

  constructor() { }

  decodeAndDisplayImage(base64Text: string, contentType: string) {
    let binaryString = window.atob(base64Text);
    let bytes = new Uint8Array(binaryString.length);
    
    for (let i = 0; i < binaryString.length; i++) {
      bytes[i] = binaryString.charCodeAt(i);
    }
  
    let blob = new Blob([bytes], { type: contentType });
    return URL.createObjectURL(blob);
  }
}
