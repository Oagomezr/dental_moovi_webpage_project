import { Component } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { CategoriesData } from 'src/app/models/categories/categoriesData';
import { CategoriesService } from 'src/app/services/categories/categories.service';
import { DialogComponent } from './../dialog/dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { DirectionComponent } from "./direction/direction.component";
import { ProductsComponent } from "./products/products.component";
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-category-products',
    templateUrl: './category-products.component.html',
    styleUrls: ['./category-products.component.scss'],
    standalone: true,
    imports: [CommonModule, DirectionComponent, ProductsComponent, FormsModule]
})
export class CategoryProductsComponent {

  constructor(private route: ActivatedRoute, private categoriesSer: CategoriesService, 
    public dialog: MatDialog) {}

  actions:string[] = [];
  textCategory: string = '';
  isAdmin: boolean = localStorage.getItem('isAdmin') != null;
  locationCategory: string[] = [];
  currentPage?: string;
  categories?: CategoriesData[];
  beforeExpanded: string[]=[];
  expandedCategories: { [key: string]: boolean } = {};

  ngOnInit(){
    this.categoriesSer.getCategories().subscribe({
      next: responseGetC =>{
        this.categories = responseGetC.data;
      },
      error: error=>{
        console.log('Error to get categories', error);
      }
    });
    this.route.params.subscribe((params: Params) => {
      this.locationCategory = params['parents'].split(',');
      this.toggleSubcategories(this.locationCategory, true);
    });
  }

  toggleSubcategories(categoryAndParents: string[], init: boolean) { 
    if(init){
      for(let i=0; i < categoryAndParents.length ; i++){
        this.expandedCategories[categoryAndParents[categoryAndParents.length-i-1]] = true;
        this.beforeExpanded[i] = categoryAndParents[categoryAndParents.length-i-1];
      }
    }else{
      this.expandedCategories[categoryAndParents[0]] = !this.expandedCategories[categoryAndParents[0]];
      if(this.beforeExpanded && this.beforeExpanded[categoryAndParents.length-1] != categoryAndParents[0]){
        this.expandedCategories[this.beforeExpanded[categoryAndParents.length-1]] = false;
      } 
      this.beforeExpanded[categoryAndParents.length-1] = categoryAndParents[0];
    }
  }
  
  receiveCurrentPage(currentPage : number){
    this.currentPage = 'Pagina '+currentPage;
  }

  checkLocalStorage() {
    const used = Math.round((JSON.stringify(localStorage).length / 1024));

    for (let i = 0, data = "1".repeat(10000); ; i++) {
        try { 
            localStorage.setItem("DATA", data);
            data = data +  "1".repeat(100000);
        } catch(e) {
            const total = Math.round((JSON.stringify(localStorage).length / 1024));
            console.log("Total: " + total + " kB");
            console.log("Used: " + used + " kB");
            console.log("FREE: " + (total - used) + " kB");
            break;
        }
    }

    localStorage.removeItem('DATA');
  }

  updateCategoryName(categoryName:string){
    
    this.categoriesSer.updateCategoryName(categoryName, this.textCategory.toUpperCase()).subscribe({
      next: response =>{
        console.log(response.infoMessage);
        window.location.reload();
      },
      error: error=>{
        console.log(error);
      }
    });
  }

  openDialog(categoryName:string, action:number){
    if(this.textCategory.length > 0 || action == 2){
      this.textCategory = this.textCategory.trim().replace(/\s+/g, ' '); ////delete all spaces at the start and allow only one space between letters

      this.actions = [
        '¿Estás seguro de añadir la nueva categoria '+ this.textCategory.toUpperCase() + '?',
        '¿Estás seguro de cambiar el nombre de '+categoryName+' a ' + this.textCategory.toUpperCase()+ '?',
        '¿Estás seguro de eliminar la categoria '+categoryName+' ?',
        '¿Estás seguro de añadir una nueva categoria llamada '+ this.textCategory.toUpperCase() +' como subcategoria de ' +  categoryName+ '?',
        '¿Estás seguro de mover '+categoryName+' dentro de ' + this.textCategory.toUpperCase()+ '?'
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
              this.createNewCategory(categoryName);
            break;
            case 1:
              this.updateCategoryName(categoryName);
            break;
            case 2:
              this.deleteCategory(categoryName);
            break;
            case 3:
              this.createNewCategory(categoryName);
            break;
            case 4:
              this.updateCategoryLocation(categoryName);
            break;
            default:
              console.log("error");
          }
          console.log('Acción confirmada');
        } else {
          console.log('Acción cancelada');
        }
      });
    }else window.alert('Por favor escribe algo en el campo de texto para realizar esa acción.');
  }

  updateCategoryLocation(categoryName:string){
    if(this.textCategory.toUpperCase() != categoryName){
      this.categoriesSer.updateCategoryLocation(categoryName, this.textCategory.toUpperCase()).subscribe({
        next: response =>{
          console.log(response.infoMessage);
          window.location.reload();
        },
        error: error=>{
          console.log(error);
          window.alert('No se encontro la categoria '+ this.textCategory.toUpperCase()+ 
            ' donde querias mover la categoria '+ categoryName+'\n\n'+
            'Recuerda tener en cuenta el uso de tildes y espacios bien definidos');
        }
      });
    }
  }


  deleteCategory(categoryName:string){
    this.categoriesSer.deleteCategory(categoryName).subscribe({
      next: response=>{
        console.log(response.infoMessage);
        window.location.reload();
      },
      error: error =>{
        console.log(error);
        window.alert('La eliminación de esta categoría no es posible mientras existan productos vinculados a ella.');
      }
    });
  }

  createNewCategory(categoryName:string){
    if(this.textCategory.toUpperCase() != categoryName){
      this.categoriesSer.createCategory(categoryName, this.textCategory.toUpperCase()).subscribe({
        next: response =>{
          console.log(response.infoMessage);
          window.location.reload();
        },
        error: error=>{
          console.log(error);
        }
      });
    }
  }
  
}
