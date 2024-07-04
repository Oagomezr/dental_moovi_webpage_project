import { Images } from "../Images";

export interface ProductsData {
    id:number;
    nameProduct: string;
    unitPrice: number;
    description: string;
    shortDescription: string;
    stock: number;
    images:Images[];
    location: string[];
    hidden:string | null;
    category: string | null;
}