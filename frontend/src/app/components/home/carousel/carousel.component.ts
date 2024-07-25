import { CommonModule } from '@angular/common';
import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { Images } from 'src/app/models/images/Images';
import { ImagesService } from 'src/app/services/images/images.service';

@Component({
  selector: 'app-carousel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './carousel.component.html',
  styleUrl: './carousel.component.scss'
})
export class CarouselComponent {

  constructor(private imgSer: ImagesService){}

  @Input() imageBase64: Images[] =[];
  @Input() indicators: boolean = true;
  @Input() controls: boolean = true;
  @Input() autoSlide: boolean = false;
  @Input() slideInterval = 3000;

  selectedIndex:number = 0;

  selectedFile: File | undefined;

  @ViewChild('fileInput') fileInput!: ElementRef;

  interval: any;

  

  ngOnInit(){
      this.autoSlideImages();
  }

  selectImg(i:number){
    this.selectedIndex = i;
    this.stopAutoSlide();
  }

  prev(){
    this.stopAutoSlide();
    if(this.selectedIndex == 0) 
      this.selectedIndex = this.imageBase64.length - 1;
    else{
      this.selectedIndex--;
    }
  }

  next(){
    if(this.selectedIndex == this.imageBase64.length - 1) 
      this.selectedIndex = 0;
    else{
      this.selectedIndex++;
    }
  }

  autoSlideImages(){
    if (this.autoSlide) {
      this.interval = setInterval(()=>{
        this.next();
      }, this.slideInterval);
    }
  }

  stopAutoSlide() {
    if (this.interval) {
      clearInterval(this.interval);
      this.interval = null;
    }
  }

  handleKeyDown(event: KeyboardEvent) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
    }
  }

  openFileExplorer() {
    this.fileInput.nativeElement.click();
  }
  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    
    if (!this.selectedFile) {
      console.log('No se ha seleccionado ningún archivo.');
      return;
    }
    console.log('Archivo seleccionado:', this.selectedFile);
    
    this.imgSer.uploadImage(this.selectedFile).subscribe({
      next: response => {
        console.log('Archivo subido con éxito.', response);
        window.location.reload();
      },
      error: error=>{
        console.error("Error al subir la imagen: "+error);
      }
    });
  }

  deleteImage(idImage:number){
    
    let parameter = idImage;

    this.imgSer.deleteImage(parameter).subscribe({
      next: () =>{
        window.location.reload();
      },
      error: error =>{
        console.error(error);
      }
    });
  }
}
