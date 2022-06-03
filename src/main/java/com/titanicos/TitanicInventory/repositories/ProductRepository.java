package com.titanicos.TitanicInventory.repositories;

import com.titanicos.TitanicInventory.model.LogEvent;
import com.titanicos.TitanicInventory.model.Products;
import com.titanicos.TitanicInventory.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Products, String> {

    public long count();

    @Query("{_id:'?0'}")
    Products findProductByID(String ID);


    @Query("{active:true}")
    List<Products> findProductsByActive(boolean active);

}