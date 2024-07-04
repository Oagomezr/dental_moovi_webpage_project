import { AddressesData } from "./addresses/addressesData";
import { Roles } from "./roles";

export interface Users {
    idUser?: number | null;
    firstName?: string | null;
    lastName?: string | null;
    email?: string | null;
    celPhone?: string | null;
    birthdate?: string | null;
    gender?: string | null;
    password?: string | null;
    roles?: Roles[];
    addresses?: AddressesData[];
}