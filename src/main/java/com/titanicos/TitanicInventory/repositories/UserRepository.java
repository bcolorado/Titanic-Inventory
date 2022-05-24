package com.titanicos.TitanicInventory.repositories;


import com.titanicos.TitanicInventory.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface UserRepository extends MongoRepository<User, String> {

    @Query("{_id:'?0'}")
    User findUserByID(String ID);

    public long count();
}
