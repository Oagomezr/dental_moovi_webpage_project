import { CartDtoResponse } from "./cartData";

export interface CartResponse {
    data: CartDtoResponse[];
    total: number;
    amountOfProducts: number;
}