package com.titanicos.TitanicInventory.repositories;

import com.titanicos.TitanicInventory.model.Products;
import com.titanicos.TitanicInventory.model.Sales;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SaleRepository extends MongoRepository<Sales, String> {

    public long count();

    @Query("{_id:'?0'}")
    Sales findProductByID(String ID);


    @Query("{active:true}")
    List<Sales> findProductsByActive(boolean active);
}
