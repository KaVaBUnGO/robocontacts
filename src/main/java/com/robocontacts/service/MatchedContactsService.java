package com.robocontacts.service;

import com.robocontacts.domain.FriendsInfoGoogle;
import com.robocontacts.domain.FriendsInfoVk;
import com.robocontacts.domain.MatchedContacts;
import com.robocontacts.repository.MatchedContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by ekaterina on 10.12.2016.
 */

@Service
public class MatchedContactsService {

    private final MatchedContactsRepository matchedContactsRepository;
    private final UserService userService;

    @Autowired
    public MatchedContactsService(MatchedContactsRepository matchedContactsRepository, UserService userService) {
        this.matchedContactsRepository = matchedContactsRepository;
        this.userService = userService;
    }

    public MatchedContacts save(FriendsInfoVk friendsInfoVk, FriendsInfoGoogle friendsInfoGoogle){
        MatchedContacts matchedContacts = new MatchedContacts();
        matchedContacts.setUserId(userService.getCurrentUser().getId());
        matchedContacts.setGoogleId(friendsInfoGoogle.getUserId());
        matchedContacts.setVkId(friendsInfoVk.getUserId());
        matchedContacts.setVkName(friendsInfoVk.getFirstName()+" " + friendsInfoVk.getLastName());
        matchedContacts.setGoogleName(friendsInfoGoogle.getFullName());
        matchedContacts.setVkPhoto(friendsInfoVk.getSmallPhotoUrl());
        matchedContacts.setGooglePhoto(friendsInfoGoogle.getPhotoUrl());
        return matchedContactsRepository.save(matchedContacts);
    }

}
