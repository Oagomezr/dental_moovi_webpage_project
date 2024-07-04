import { Orders } from "./orders";

export interface OrderResponse{
    pending:Orders[];
    complete:Orders[];
    cancel:Orders[];
}