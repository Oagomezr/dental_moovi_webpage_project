import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ProductsData } from 'src/app/models/products/productsData';
import { ProductsService } from 'src/app/services/products/products.service';
import { DialogComponent } from '../../dialog/dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { DirectionComponent } from "../direction/direction.component";
import { VisibilityIconComponent } from "./visibility-icon/visibility-icon.component";
import { FormsModule } from '@angular/forms';
import { CartDtoRequest } from 'src/app/models/cart/cartStore';
import { AutocompleteFieldComponent } from '../../form-fields-components/autocomplete-field/autocomplete-field.component';
import { EnumsService } from 'src/app/services/enums/enums.service';
import { Enum1 } from 'src/app/models/enums/enum1/enum1';

@Component({
    selector: 'app-product',
    templateUrl: './product.component.html',
    styleUrls: ['./product.component.scss'],
    standalone: true,
    imports: [CommonModule, DirectionComponent, VisibilityIconComponent, FormsModule, AutocompleteFieldComponent]
})
export class ProductComponent {

  constructor(private route: ActivatedRoute, private router: Router,
    private productSer: ProductsService, public dialog: MatDialog, private enumSer: EnumsService) {}

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      let product = params['product'];
      this.isNew = product == 0;
      if(!this.isNew){
        this.productSer.getProductByName(product).subscribe({
          next: response => {
            this.product = response;
            this.nameProduct = response.nameProduct
            this.locationCategory = response.location;
          },
          error: error => {
            console.error("error to load product: "+error);
          }
        });
      }
    });
  }

  locationCategory: string[] = [];
  nameProduct: string = '';
  indexImg: number = 0;
  isAdmin: boolean = localStorage.getItem('isAdmin') != null;
  editArray: boolean[] = [true, true, true, true, true];
  textEdit: any = '';
  callerCart: CartDtoRequest[] = [];
  categories!: Enum1[];
  isNew: boolean = false;

  product: ProductsData ={
    id:0,
    nameProduct: 'Nombre del nuevo producto',
    unitPrice: 0,
    description: 'descripcion del nuevo producto',
    shortDescription: 'descripcion corta nuevo producto',
    stock: 0,
    images:[],
    location: [],
    hidden: null,
    category: null,
    idCategory: 0
  };

  

  changeImg(index: number){
    this.indexImg = index;
  }

  actions:string[] = [];
  openDialog(parameter:any, action:number){

    this.actions = [
      '¿Estás segur@ de establecer esta imagen coomo principal?',
      '¿Estás segur@ de eliminar esta imagen?',
      '¿Estas segur@ de que quieres cambiar la visibilidad del producto?'
    ];

    const dialogRef = this.dialog.open(DialogComponent, {
      width: '250px',
      data: {
        title: 'Confirmación',
        message: this.actions[action]
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        switch (action) {
          case 0:
            this.updateMainImage(parameter);
          break;
          case 1:
            this.deleteImage(parameter);
          break;
          case 2:
            this.changeVisibility(parameter);
          break;
          default:
            console.log("error");
        }
      }else{
        console.log("Operacion cancelada");
      }
    });
  }

  @ViewChild('fileInput') fileInput!: ElementRef;
  selectedFile: File | undefined;

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
    
    this.productSer.uploadImage(this.selectedFile, this.product.id).subscribe({
      next: response => {
        console.log('Archivo subido con éxito al backend.', response);
        window.location.reload();
      },
      error: error=>{
        console.error("Error al subir la imagen: "+error);
      }
    });
  }

  updateMainImage(idImage:number){
    this.productSer.updateMainImage(idImage,this.product.id).subscribe({
      next: () =>{
        window.location.reload();
      },
      error: error =>{
        console.error(error);
      }
    });
  }

  deleteImage(idImage:number){
    
    let parameter = this.product.images.length == 1 ? this.product.nameProduct : idImage;

    this.productSer.deleteImage(parameter).subscribe({
      next: () =>{
        window.location.reload();
      },
      error: error =>{
        console.error(error);
      }
    });
  }

  changeVisibility(visibility:boolean){
    this.productSer.updateVisibility(visibility, this.product.id).subscribe({
      next: ()=>{
        window.location.reload();
      },
      error: error=>{
        console.error(error);
      }
    });
  }

  editProductInformation(option:number){
    this.editArray =  [true, true, true, true, true];
    this.editArray[option] = !this.editArray[option];
    switch (option) {
      case 0:
        this.textEdit = this.product.nameProduct;
      break;
      case 1:
        this.textEdit = this.product.unitPrice;
      break;
      case 2:
        this.textEdit = this.product.description;
      break;
      case 3:
        this.textEdit = this.product.stock;
      break;
      case 4:
        console.log("category");
      break;
      default:
        console.log("error");
    }
  }

  setProductInformation(option:number){
    switch (option) {
      case 0:
        this.product.nameProduct = this.textEdit;
      break;
      case 1:
        this.product.unitPrice = this.textEdit;
      break;
      case 2:
        this.product.description = this.textEdit;
      break;
      case 3:
        this.product.stock = this.textEdit;
      break;
      case 4:
        console.log("category");
      break;
      default:
        console.log("error");
    }
  }

  cancelProductInformation(){
    this.editArray =  [true, true, true, true, true];
  }

  updateProductInformation(option:number){
    
    if(this.isNew){
      this.editArray[option] = !this.editArray[option];
      this.setProductInformation(option);
      console.log(this.product);
    } 
    else this.productSer.updateProductInformation(option, this.product.id, this.textEdit);
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

  searchCategories(value:string){
    if (value.length >1) {
      this.enumSer.getCategories(value).subscribe({
        next: response => {
          this.categories = response.data;
        },
        error: error => {
          console.error("categories not found: ", error);
        }
      });
    } else {
      this.categories = [];
    }
  }

  setCategory(id:number){
    if(this.isNew){
      this.product.idCategory = id;
    } 
    else this.textEdit = id;
  }

  createProduct(){
    this.productSer.createProduct(this.product).subscribe({
        next : r =>{
          this.router.navigate(["/product/"+r.infoMessage]);
        },error : error =>{
            console.log(error);
        }
    });
  }
}
