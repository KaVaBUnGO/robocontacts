package com.robocontacts.repository;

import com.robocontacts.domain.MatchedContacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ekaterina on 10.12.2016.
 */

@Repository
public interface MatchedContactsRepository extends JpaRepository<MatchedContacts, Long> {


}
