package com.robocontacts.service;

import com.robocontacts.domain.ConnectedPlatform;
import com.robocontacts.repository.ConnectedPlatformRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ekaterina on 10.11.2016.
 */

@Service
public class ConnectedPlatformService {

    private static final Logger log = LoggerFactory.getLogger(ConnectedPlatformService.class);
    private final ConnectedPlatformRepository connectedPlatformRepository;

    @Autowired
    public ConnectedPlatformService(ConnectedPlatformRepository connectedPlatformRepository) {
        this.connectedPlatformRepository = connectedPlatformRepository;
    }

    @Transactional(readOnly = true)
    public List<ConnectedPlatform> getConnectedPlatformByUserId(Long id) {
        log.debug("Getting connectedPlatform by userId");
        return connectedPlatformRepository.findByUserId(id);
    }

    @Transactional
    public ConnectedPlatform save(ConnectedPlatform connectedPlatform) {
        log.debug("Saving platform={}\", connectedPlatform");
        return connectedPlatformRepository.save(connectedPlatform);
    }

    @Transactional
    public void delete(ConnectedPlatform connectedPlatform) {
        connectedPlatformRepository.delete(connectedPlatform);
    }
}
