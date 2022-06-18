package com.titanicos.TitanicInventory.repositories;

import com.titanicos.TitanicInventory.model.Products;
import com.titanicos.TitanicInventory.model.Sales;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SaleRepository extends MongoRepository<Sales, String> {
    @Query(value = "{active:?0}",count = true)
    long countByActive(Boolean active);
    @Aggregation(pipeline ={ "{$match:{active:true}}",
            "{$group: { _id: '', total: {$sum: $total}}}"})
    double sumOfTotals();
    @Query("{_id:'?0'}")
    Sales findProductByID(String ID);


    @Query("{active:?0}")
    List<Sales> findSalesByActive(boolean active);
}
