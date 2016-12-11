package com.robocontacts.repository;

import com.robocontacts.domain.FriendsInfoVk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ekaterina on 10.12.2016.
 */

@Repository
public interface FriendsInfoVkRepository extends JpaRepository<FriendsInfoVk, Long> {
}
