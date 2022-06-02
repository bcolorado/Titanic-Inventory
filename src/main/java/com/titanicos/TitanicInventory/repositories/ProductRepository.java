package com.titanicos.TitanicInventory.repositories;

import com.titanicos.TitanicInventory.model.LogEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<LogEvent, String> {

    public long count();
}