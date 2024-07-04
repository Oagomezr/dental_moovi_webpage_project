import { ProductsData } from "./productsData";

export interface ProductsResponse {
    totalProducts: number;
    paginatedProducts: number;
    data: ProductsData[];
}