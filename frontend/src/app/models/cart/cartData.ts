import { Images } from "../Images";

export interface CartDtoResponse {
    id:number;
    image:Images;
    productName: string;
    prize: number;
    amount: number;
    subtotal: number;
}