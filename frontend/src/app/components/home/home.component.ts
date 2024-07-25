import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CarouselComponent } from "./carousel/carousel.component";
import { ImagesService } from 'src/app/services/images/images.service';
import { Images } from 'src/app/models/images/Images';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [CommonModule, CarouselComponent]
})
export class HomeComponent {

  constructor(private imgSer: ImagesService){}

  images: Images[] = [];
  isAdmin: boolean = localStorage.getItem('isAdmin') != null;

  ngOnInit(){
    this.getCarousel();
  }

  getCarousel(){
    this.imgSer.getCarousel().subscribe({
      next: r =>{
        this.images = r.data;
      },error: e =>{
        console.log("Error to get carousel images: "+e)
      }
    });
  }

}
