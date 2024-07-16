import { CartDtoRequest } from "./cartStore";

export interface CartRequest {
    data: CartDtoRequest[];
    idUser: number;
}