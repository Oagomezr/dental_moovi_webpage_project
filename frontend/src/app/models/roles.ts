import { Users } from "./users/users";

export interface Roles {
    idRole: number;
    nameRole: string;
    users?: Users[];
}