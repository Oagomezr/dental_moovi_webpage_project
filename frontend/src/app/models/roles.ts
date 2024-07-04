import { Users } from "./users";

export interface Roles {
    idRole: number;
    nameRole: string;
    users?: Users[];
}