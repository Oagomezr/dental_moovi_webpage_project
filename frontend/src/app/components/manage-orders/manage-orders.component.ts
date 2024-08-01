import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrdersService } from 'src/app/services/orders/orders.service';
import { NgxExtendedPdfViewerModule, NgxExtendedPdfViewerService } from 'ngx-extended-pdf-viewer';
import { Orders } from 'src/app/models/orders/orders';
import {MatTabsModule} from '@angular/material/tabs';
import { OrderPanelComponent } from './order-panel/order-panel.component';

@Component({
  selector: 'app-manage-orders',
  standalone: true,
  imports: [CommonModule, NgxExtendedPdfViewerModule, MatTabsModule, OrderPanelComponent],
  providers: [NgxExtendedPdfViewerService],
  changeDetection: ChangeDetectionStrategy.Default,
  templateUrl: './manage-orders.component.html',
  styleUrls: ['./manage-orders.component.scss']
})
export class ManageOrdersComponent {

  ngOnInit(){
    this.getOrders();
  }

  pdfSrc: Blob | null = null;
  loadingPdf: boolean = false;
  nombre:string = "document.pdf";
  pending: Orders[] = [];
  complete: Orders[] = [];
  cancel: Orders[] = [];

  constructor(private orderService: OrdersService) {}

  getPdf(numero:number){
    const orderId = numero; // Cambia esto según sea necesario
    this.orderService.getPdf(orderId).subscribe({
      next: (data: Blob) => {
        this.nombre="perro.pdf";
        this.pdfSrc=data;
        console.log("PDF base64:", this.pdfSrc);
        this.loadingPdf = true;
      },
      error: (err) => {
        console.error('Error to get PDF', err);
        this.loadingPdf = false;
      }
    });
  }

  getOrders(){
    this.orderService.getPdfsList().subscribe({
      next: r => {
        this.pending = r.pending;
        this.complete = r.complete;
        this.cancel = r.cancel;
        console.log(this.pending)
      },error: e=>{
        console.error("Error to get orders: "+e.error.message);
      }
    });
  }
}
