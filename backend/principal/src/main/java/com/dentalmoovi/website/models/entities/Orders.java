package com.dentalmoovi.website.models.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import com.dentalmoovi.website.models.entities.enums.StatusOrderList;
import com.dentalmoovi.website.models.entities.many_to_many.OrdersProducts;

public record Orders(
    @Id Long id,
    byte[] orderFile,
    StatusOrderList status,
    LocalDateTime date,
    Long idUser,
    Long idAddress,
    @MappedCollection(idColumn = "id_order") Set<OrdersProducts> products
) {
    public Orders{
        if (products == null) products = new HashSet<>(); 
    }

    public void addProduct(Long idProduct, int amount){
        this.products.add(new OrdersProducts(id ,idProduct, amount));
    }

    public Set<Long> getProductsIds(){
        return this.products.stream()
                    .map(OrdersProducts::idProduct)
                    .collect(Collectors.toSet());
    }

    public void deleteProduct(Long productId) {
        this.products.removeIf(productsRef -> productsRef.idProduct().equals(productId));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
        result = prime * result + ((idAddress == null) ? 0 : idAddress.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Orders other = (Orders) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (status != other.status)
            return false;
        if (idUser == null) {
            if (other.idUser != null)
                return false;
        } else if (!idUser.equals(other.idUser))
            return false;
        if (idAddress == null) {
            if (other.idAddress != null)
                return false;
        } else if (!idAddress.equals(other.idAddress))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return  "id: "+ id+
                " idUser: "+idUser+
                " idAddress"+idAddress;
    }
}
