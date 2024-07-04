package com.dentalmoovi.website.models.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import com.dentalmoovi.website.models.entities.enums.GenderList;
import com.dentalmoovi.website.models.entities.many_to_many.UsersRoles;
import com.dentalmoovi.website.models.entities.many_to_many.UsersAddresses;

public record Users(
    @Id Long id,
    String firstName,
    String lastName,
    String email,
    String celPhone,
    LocalDate birthdate,
    GenderList gender,
    String password,
    @MappedCollection(idColumn = "id_user") Set<UsersRoles> roles,
    @MappedCollection(idColumn = "id_user") Set<UsersAddresses> addresses
) {
    public Users{
        if (roles == null) roles = new HashSet<>(); 
        if (addresses == null) addresses = new HashSet<>(); 
    }

    public void addRole(Roles role){
        this.roles.add(new UsersRoles(id, role.id()));
    }

    public Set<Long> getRolesIds(){
        return this.roles.stream()
                    .map(UsersRoles::idRole)
                    .collect(Collectors.toSet());
    }

    public void addAddress(Addresses address){
        this.addresses.add(new UsersAddresses(id, address.id()));
    }

    public Set<Long> getAddressesIds(){
        return this.addresses.stream()
                    .map(UsersAddresses::idAddress)
                    .collect(Collectors.toSet());
    }

    public void deleteAddress(Long addressId) {
        this.addresses.removeIf(addressRef -> addressRef.idAddress().equals(addressId));
    }
}