package com.titanicos.TitanicInventory.repositories;

import com.titanicos.TitanicInventory.model.Consults;
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
    Sales findSaleByID(String ID);

    @Aggregation(pipeline = { "{$match:{active:true}}",
            "{$unwind:{path:'$products'}}",
            "{$group: { _id: '$products._id', dato: {$sum: '$products.cantidad'}}}",
            "{$sort:{'dato':-1}}"})
    List<Consults>productsSold();
    @Aggregation(pipeline = { "{$match:{active:true}}",
            "{$unwind:{path:'$products'}}",
            "{$group: { _id: '$products._id', dato: {$sum: '$products.Subtotal'}}}",
            "{$sort:{'dato':-1}}"})
    List<Consults>productsIncome();
    @Aggregation(pipeline = {"{$match:{active:true}}",
            "{$group:{'_id':'$id_vendedor','dato':{$sum:1}}}",
            "{$sort:{'dato':-1}}"})
    List<Consults> sellerSale();
    @Aggregation(pipeline = {"{$match:{active:true}}",
            "{$group:{'_id':'$id_vendedor','dato':{$sum:$total}}}",
            "{$sort:{'dato':-1}}"})
    List<Consults> sellerIngresos();
    @Aggregation(pipeline = {"{$match:{active:true}}",
            "{$group:{_id:{$dateToString:{format:'%Y-%m-%d', date:'$timestamp'}},'dato':{$sum:'$total'}}}",
            "{$sort:{'_id':1}}"})
    List<Consults> ingresosDia();
    @Aggregation(pipeline = {"{$match:{active:true}}",
            "{$group:{_id:{$dateToString:{format:'%Y-%m-%d', date:'$timestamp'}},'dato':{$sum:1}}}",
            "{$sort:{'_id':1}}"})
    List<Consults> ventasDia();
    @Query("{active:?0}")
    List<Sales> findSalesByActive(boolean active);
}
