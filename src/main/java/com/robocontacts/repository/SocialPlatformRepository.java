package com.robocontacts.repository;

import com.robocontacts.domain.SocialPlatform;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ekaterina on 03.11.2016.
 */
public interface SocialPlatformRepository extends CrudRepository<SocialPlatform, Long> {


}
