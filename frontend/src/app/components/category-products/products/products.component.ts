import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CartDtoRequest } from 'src/app/models/cart/cartStore';
import { ProductsData } from 'src/app/models/products/productsData';
import { ProductsService } from 'src/app/services/products/products.service';

@Component({
    selector: 'app-products',
    templateUrl: './products.component.html',
    styleUrls: ['./products.component.scss'],
    standalone: true,
    imports: [CommonModule, FormsModule]
})
export class ProductsComponent {

    constructor(private productsSer: ProductsService, private router: Router) {}

    ngOnInit() { this.getProducts(); }

    @Output() sendCurrentPage = new EventEmitter<number>();
    @Input() locationCategory: string[] = [];

    isAdmin: boolean = localStorage.getItem('isAdmin') != null;
    isInCategory: boolean = true;
    products?: ProductsData[];
    currentPage = 1;
    productsPerPage = 8;
    totalPages: number = 0;
    edit: boolean = false;
    textEdit:string = '';
    productEdit: string = '';
    callerCart: CartDtoRequest[] = [];
    amount?: number;

    get pages() {
        return Array.from({ length: this.totalPages });
    }

    prevPage() {
        this.currentPage--;
        this.getProducts();
    }

    nextPage() {
        this.currentPage++;
        this.getProducts();
    }

    changePage(pageNumber: number) {
        this.currentPage = pageNumber;
        this.getProducts();
    }

    private getProducts(){
        if(this.locationCategory[1] == "Buscar"){
            this.productsSer.getProductsBySearch(this.locationCategory[0], false, this.currentPage, this.productsPerPage).subscribe({
                next: response => {
                    this.products = response.data;
                    if(this.totalPages < 1) this.totalPages = Math.ceil(response.totalProducts / this.productsPerPage);
                    if(this.totalPages > 1) this.sendCurrentPage.emit(this.currentPage);
                    window.scrollTo({ top: 100, behavior: 'smooth' });
                    console.log(response);
                    console.log(this.totalPages);
                    this.isInCategory = false;
                },
                error: error => {
                    console.error("Products not found:", error);
                }
            });
        }else{
            this.productsSer.getProductsByCategory(this.locationCategory[0], this.currentPage, this.productsPerPage).subscribe({
                next: response =>{
                    this.products = response.data;
                    if(this.totalPages < 1) this.totalPages = Math.ceil(response.totalProducts / this.productsPerPage);
                    if(this.totalPages > 1) this.sendCurrentPage.emit(this.currentPage);
                    window.scrollTo({ top: 100, behavior: 'smooth' });
                },
                error: error =>{
                    console.error('Error to get products', error)
                }
            });
        }
    }

    editProductInformation(shortDescription:string, nameProduct: string){
        this.textEdit = shortDescription;
        this.edit=!this.edit;
        this.productEdit = nameProduct;
    }

    updateProductInformation(nameProduct:string){
        this.productsSer.updateProductInformation(4, nameProduct, this.textEdit);
    }

    createProduct(){
        this.productsSer.createProduct(this.locationCategory[0]).subscribe({
            next : () =>{
                this.router.navigate(['/product/Nombre del nuevo producto']);
            },error : error =>{
                console.log(error);
                console.log(this.locationCategory[0]);
            }
        });
    }

    addToCart(id:number, prize:number, amount:string){

        this.callerCart = localStorage.getItem('callerCart') ? JSON.parse(localStorage.getItem('callerCart')!) : [];
        let shouldExit = false;
        this.callerCart.forEach(elem => {
            if(elem.id == id){
                elem.amount += +amount;
                localStorage.setItem('callerCart', JSON.stringify(this.callerCart));
                shouldExit= true;
            }
        });

        if(shouldExit){
            window.location.reload();
            return;
        }

        let productAdded: CartDtoRequest ={
            id: id,
            prize: prize,
            amount: +amount
        }

        this.callerCart.push(productAdded);
        localStorage.setItem('callerCart', JSON.stringify(this.callerCart));
        console.log(this.callerCart);
        window.location.reload();
    }
}
