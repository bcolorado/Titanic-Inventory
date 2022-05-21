package com.titanicos.TitanicInventory;

import com.titanicos.TitanicInventory.model.TestLogEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TestLogRepository extends MongoRepository<TestLogEvent, String> {

    public long count();
}