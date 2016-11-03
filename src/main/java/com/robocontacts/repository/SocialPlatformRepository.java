package com.robocontacts.repository;

import com.robocontacts.domain.ConnectedPlatform;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ekaterina on 03.11.2016.
 */
public interface SocialPlatformRepository extends CrudRepository<ConnectedPlatform, Long> {


}
