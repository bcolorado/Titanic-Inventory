package com.titanicos.TitanicInventory.repositories;

import com.titanicos.TitanicInventory.model.Queries;
import com.titanicos.TitanicInventory.model.Sales;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface SaleRepository extends MongoRepository<Sales, String> {
    @Query(value = "{active:?0}",count = true)
    long countByActive(Boolean active);
    @Aggregation(pipeline ={ "{$match:{active:true}}",
            "{$group: { _id: '', total: {$sum: $total}}}"})
    long sumOfTotals();
    @Query("{_id:'?0'}")
    Sales findSaleByID(String ID);

    @Aggregation(pipeline = { "{$match:{active:true}}",
            "{$group: { _id: '$id_vendedor', dato: {$sum:1}}}",
            "{$count: 'id_vendedor'}"})
    long countActiveSellers();
    @Aggregation(pipeline = { "{$match:{active:true}}",
            "{$unwind:{path:'$products'}}",
            "{$group: { _id: '$products._id', dato: {$sum: '$products.cantidad'}}}",
            "{$sort:{'dato':-1}}"})
    List<Queries>productsSold();
    @Aggregation(pipeline = { "{$match:{active:true}}",
            "{$unwind:{path:'$products'}}",
            "{$group: { _id: '$products._id', dato: {$sum: '$products.Subtotal'}}}",
            "{$sort:{'dato':-1}}"})
    List<Queries>productsIncome();
    @Aggregation(pipeline = {"{$match:{active:true}}",
            "{$group:{'_id':'$id_vendedor','dato':{$sum:1}}}",
            "{$sort:{'dato':-1}}"})
    List<Queries> sellerSale();
    @Aggregation(pipeline = {"{$match:{active:true}}",
            "{$group:{'_id':'$id_vendedor','dato':{$sum:$total}}}",
            "{$sort:{'dato':-1}}"})
    List<Queries> sellerIngresos();
    @Aggregation(pipeline = {"{$match:{active:true}}",
            "{$group:{_id:{$dateToString:{format:'%Y-%m-%d', date:'$timestamp'}},'dato':{$sum:'$total'}}}",
            "{$sort:{'_id':1}}"})
    List<Queries> ingresosDia();
    @Aggregation(pipeline = {"{$match:{active:true}}",
            "{$group:{_id:{$dateToString:{format:'%Y-%m-%d', date:'$timestamp'}},'dato':{$sum:1}}}",
            "{$sort:{'_id':1}}"})
    List<Queries> ventasDia();
    @Query("{active:?0}")
    List<Sales> findSalesByActive(boolean active);


    // mismas de arriba pero para fitlrar en rango
    @Query(value = "{active:?0, 'timestamp' : {$gte : ?1, $lt : ?2}}",count = true)
    long countByActiveInRange(Boolean active, Date from, Date to);
    @Aggregation(pipeline ={ "{$match:{active:true,timestamp : {$gte : ?0, $lt : ?1}}}",
            "{$group: { _id: '', total: {$sum: $total}}}"})
    long sumOfTotalsInRange(Date from, Date to);

    @Aggregation(pipeline = { "{$match:{active:true,timestamp : {$gte : ?0, $lt : ?1}}}",
            "{$group: { _id: '$id_vendedor', dato: {$sum:1}}}",
            "{$count: 'id_vendedor'}"})
    long countActiveSellersInRange(Date from, Date to);
    @Aggregation(pipeline = { "{$match:{active:true,timestamp : {$gte : ?0, $lt : ?1}}}",
            "{$unwind:{path:'$products'}}",
            "{$group: { _id: '$products._id', dato: {$sum: '$products.cantidad'}}}",
            "{$sort:{'dato':-1}}"})
    List<Queries>productsSoldInRange(Date from, Date to);
    @Aggregation(pipeline = { "{$match:{active:true,timestamp : {$gte : ?0, $lt : ?1}}}",
            "{$unwind:{path:'$products'}}",
            "{$group: { _id: '$products._id', dato: {$sum: '$products.Subtotal'}}}",
            "{$sort:{'dato':-1}}"})
    List<Queries>productsIncomeInRange(Date from, Date to);
    @Aggregation(pipeline = {"{$match:{active:true,timestamp : {$gte : ?0, $lt : ?1}}}",
            "{$group:{'_id':'$id_vendedor','dato':{$sum:1}}}",
            "{$sort:{'dato':-1}}"})
    List<Queries> sellerSaleInRange(Date from, Date to);
    @Aggregation(pipeline = {"{$match:{active:true,timestamp : {$gte : ?0, $lt : ?1}}}",
            "{$group:{'_id':'$id_vendedor','dato':{$sum:$total}}}",
            "{$sort:{'dato':-1}}"})
    List<Queries> sellerIngresosInRange(Date from, Date to);
    @Aggregation(pipeline = {"{$match:{active:true,timestamp : {$gte : ?0, $lt : ?1}}}",
            "{$group:{_id:{$dateToString:{format:'%Y-%m-%d', date:'$timestamp'}},'dato':{$sum:'$total'}}}",
            "{$sort:{'_id':1}}"})
    List<Queries> ingresosDiaInRange(Date from, Date to);
    @Aggregation(pipeline = {"{$match:{active:true,timestamp : {$gte : ?0, $lt : ?1}}}",
            "{$group:{_id:{$dateToString:{format:'%Y-%m-%d', date:'$timestamp'}},'dato':{$sum:1}}}",
            "{$sort:{'_id':1}}"})
    List<Queries> ventasDiaInRange(Date from, Date to);
}
