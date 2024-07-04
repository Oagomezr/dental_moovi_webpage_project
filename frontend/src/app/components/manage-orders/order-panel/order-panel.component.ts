import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { MatTabsModule } from '@angular/material/tabs';
import { OrdersService } from 'src/app/services/orders/orders.service';
import { Orders } from 'src/app/models/orders/orders';

@Component({
  selector: 'app-order-panel',
  standalone: true,
  imports: [CommonModule, NgxExtendedPdfViewerModule, MatTabsModule],
  templateUrl: './order-panel.component.html',
  styleUrls: ['./order-panel.component.scss']
})
export class OrderPanelComponent {

  constructor(private orderService: OrdersService) {}

  @Input() data: Orders[] = [];
  @Input() typeOrder: string = "";

  buttonSelected: number =-1;
  pdfSrc: Blob | null = null;
  loadingPdf: boolean = false;
  pdfName:string = "document.pdf";
  isAdmin: boolean = localStorage.getItem('isAdmin') != null;

  getPdf(id:number, name:string){
    this.buttonSelected = id; // Cambia esto según sea necesario
    this.orderService.getPdf(id).subscribe({
      next: (data: Blob) => {
        this.pdfSrc=data;
        this.pdfName=name;
        this.loadingPdf = true;
      },
      error: (e) => {
        console.error('Error to get PDF:', e.error.message);
        this.loadingPdf = false;
      }
    });
  }

  updateOrderStatus(isOtherButton:boolean, order:number){
    if(isOtherButton){
      
    }
  }
  
}
