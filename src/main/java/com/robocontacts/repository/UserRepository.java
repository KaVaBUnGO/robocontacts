package com.robocontacts.repository;

import com.robocontacts.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ekaterina on 16.09.2016.
 */

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    User findByLogin(String login);
    User findById(Long id);
}

