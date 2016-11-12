package com.robocontacts.repository;

import com.robocontacts.domain.ConnectedPlatform;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ekaterina on 03.11.2016.
 */
public interface ConnectedPlatformRepository extends CrudRepository<ConnectedPlatform, Long> {

    List<ConnectedPlatform> findByUserId(Long userId);

}
