package com.titanicos.TitanicInventory.repositories;


import com.titanicos.TitanicInventory.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;


public interface UserRepository extends MongoRepository<User, String> {

    @Query("{_id:'?0'}")
    User findUserByID(String ID);

    @Query("{active:?0}")
    List<User> findUsersByActive(boolean active);
    @Query(value = "{active:?0, rol:?1}",count = true)
    long countByActiveAndRol(Boolean active, String Rol);
}
