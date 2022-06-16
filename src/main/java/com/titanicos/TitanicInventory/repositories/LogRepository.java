package com.titanicos.TitanicInventory.repositories;

import com.titanicos.TitanicInventory.model.LogEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface LogRepository extends MongoRepository<LogEvent, String> {

    @Query(value = "{'timestamp' : {$gte : ?0, $lt : ?1}}", sort = "{'timestamp' : -1}")
    List<LogEvent> inRange(Date from, Date to);
    public long count();
}