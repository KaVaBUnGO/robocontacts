package com.robocontacts.service;

import com.robocontacts.domain.FriendsInfoGoogle;
import com.robocontacts.domain.FriendsInfoVk;
import com.robocontacts.domain.MatchedContacts;
import com.robocontacts.repository.FriendsInfoVkRepository;
import com.robocontacts.repository.MatchedContactsRepository;
import com.robocontacts.repository.UserRepository;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Created by ekaterina on 10.12.2016.
 */

@Service
public class ScheduledService {

    private final SynchronizationService synchronizationService;
    private final VkService vkService;
    private final GoogleService googleService;
    private final FriendsInfoVkRepository friendsInfoVkRepository;
    private final MatchedContactsRepository matchedContactsRepository;
    private final UserRepository userRepository;

    @Autowired
    public ScheduledService(SynchronizationService synchronizationService, VkService vkService, GoogleService googleService, FriendsInfoVkRepository friendsInfoVkRepository, MatchedContactsRepository matchedContactsRepository, UserRepository userRepository) {
        this.synchronizationService = synchronizationService;
        this.vkService = vkService;
        this.googleService = googleService;
        this.friendsInfoVkRepository = friendsInfoVkRepository;
        this.matchedContactsRepository = matchedContactsRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 120000)
    public void synchroPhoto() {


        List<FriendsInfoGoogle> friendsInfoGoogles = googleService.getFriendsInfoGoogle();
        List<FriendsInfoVk> friendsInfoVksRepo = friendsInfoVkRepository.findAll();
        List<MatchedContacts> matchedContactses = matchedContactsRepository.findAll();

        for (MatchedContacts matchedContacts:matchedContactses) {
            String lastPhotoVk = matchedContacts.getVkPhoto();

            //String newPhotoVk = friendsInfoVks.stream().filter(info -> Objects.equals(info.getUserId(), matchedContacts.getVkId())).findAny().get().getSmallPhotoUrl();

            String newPhotoVk = vkService.getVkInfo(matchedContacts.getVkId());

            if (newPhotoVk.equals(lastPhotoVk)) {
                synchronizationService.updatePhoto(newPhotoVk, matchedContacts.getGooglePhoto());
            }
            if (!lastPhotoVk.equals(matchedContacts.getGooglePhoto())){
                synchronizationService.updatePhoto(newPhotoVk, matchedContacts.getGooglePhoto());
            }

            matchedContacts.setVkPhoto(newPhotoVk);
            matchedContactsRepository.save(matchedContacts);
        }



    }




}
