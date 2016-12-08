package com.robocontacts.service;

import com.robocontacts.domain.*;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.exceptions.OAuthException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.friends.responses.GetResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ekaterina on 30.10.2016.
 */

@Service
public class VkService {
    private static final Logger log = LoggerFactory.getLogger(VkService.class);

    private final ConnectedPlatformService connectedPlatformService;
    private final UserService userService;

    @Value("${application.hostname}")
    private String host;

    @Value("${platforms.vk.app.id}")
    private Integer appId;

    @Value("${platforms.vk.app.secret}")
    private String appSecret;

    @Value("${platforms.vk.app.code}")
    private String code;

    @Value("${platforms.vk.app.redirectUrl}")
    private String redirectUri;

    public VkService(ConnectedPlatformService connectedPlatformService, UserService userService) {
        this.connectedPlatformService = connectedPlatformService;
        this.userService = userService;
    }

    public String getOAuthUrl() {
        try {
            URIBuilder uriBuilder = new URIBuilder("https://oauth.vk.com/authorize");
            uriBuilder.addParameter("client_id", String.valueOf(appId));
            uriBuilder.addParameter("display", "page");
            uriBuilder.addParameter("redirect_uri", getRedirectUri());
            uriBuilder.addParameter("scope", "groups");
            uriBuilder.addParameter("response_type", "code");
            return uriBuilder.toString();
        } catch (URISyntaxException e) {
            log.debug("Create VK oauth url failed");
            // todo: implement custom exceptions later
            throw new RuntimeException("Create VK oauth url failed");
        }
    }

    @Transactional
    public void connect(String code) {
        try {
            VkApiClient vk = getVkApiClient();
            UserAuthResponse authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(appId, appSecret, getRedirectUri(), code)
                    .execute();
            UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
            ConnectedPlatform connectedPlatform = new ConnectedPlatform();
            User user = userService.getCurrentUser();
            connectedPlatform.setUser(user);
            connectedPlatform.setAccessToken(authResponse.getAccessToken());
            connectedPlatform.setExpiresIn(authResponse.getExpiresIn());
            connectedPlatform.setVkId(authResponse.getUserId());
            connectedPlatform.setSocialPlatform(SocialPlatform.VK);
            connectedPlatformService.save(connectedPlatform);
            UserInfo userInfo = fillConnectedPlatformInfo(connectedPlatform, user);
            user.setUserInfo(userInfo);
            userService.save(user);
            log.debug("Actor {}", actor);
        } catch (OAuthException e) {
            e.getRedirectUri();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserXtrCounters getVkUserFields(ConnectedPlatform connectedPlatform) {
        try {
            VkApiClient vk = getVkApiClient();
            UserActor actor = new UserActor((int) connectedPlatform.getVkId(), connectedPlatform.getAccessToken());
            List<UserXtrCounters> userXtrCounterses = vk.users().get(actor).fields(
                    UserField.PHOTO_50,
                    UserField.PHOTO_100,
                    UserField.PHOTO_200,
                    UserField.PHOTO_400_ORIG
            ).execute();

            if (CollectionUtils.isNotEmpty(userXtrCounterses)) {
                return userXtrCounterses.stream().findFirst().orElse(null);
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getRedirectUri() {
        return host + redirectUri;
    }

    private VkApiClient getVkApiClient() {
        TransportClient transportClient = new HttpTransportClient();
        return new VkApiClient(transportClient);
    }

    private UserInfo fillConnectedPlatformInfo(ConnectedPlatform connectedPlatform, User user) {
        UserInfo userInfo = new UserInfo();
        UserXtrCounters vkFields = getVkUserFields(connectedPlatform);
        userInfo.setFirstName(vkFields.getFirstName());
        userInfo.setLastName(vkFields.getLastName());
        userInfo.setSmallPhotoUrl(vkFields.getPhoto100());
        userInfo.setMediumPhotoUrl(vkFields.getPhoto200());
        userInfo.setBigPhotoUrl(vkFields.getPhoto400Orig());
        userInfo.setVkId(vkFields.getId());
        userInfo.setUser(user);
        return userInfo;
    }


    public List<FriendsInfo> getFriendsInfo() {
        List<FriendsInfo> listFriends = new ArrayList<>();
        List<Integer> friendsId = getFriendsId();
        FriendsInfo friendsInfo = new FriendsInfo();
        for (Integer id : friendsId) {
            //System.out.print(id+ " ");
            friendsInfo = fillFriendsInfo(id);
            listFriends.add(friendsInfo);
            //System.out.println(friendsInfo.getFirstName());
        }
        return listFriends;
    }


    private UserXtrCounters getFriendsField(Integer friendId) {
        VkApiClient vk = getVkApiClient();
        List<List<UserXtrCounters>> friendsField = new ArrayList<>();
        try {
            List<UserXtrCounters> userXtrCounterses = vk.users().get()
                    .userIds(friendId.toString())
                    .fields(UserField.CONTACTS, UserField.PHOTO_50, UserField.PHOTO_100,
                            UserField.PHOTO_200,
                            UserField.PHOTO_400_ORIG)
                    .execute();
            if (CollectionUtils.isNotEmpty(userXtrCounterses)) {
                return userXtrCounterses.stream().findFirst().orElse(null);
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Integer> getFriends() {
        return getFriendsId();
    }


    private List<Integer> getFriendsId() {
        try {
            VkApiClient vk = getVkApiClient();
            ConnectedPlatform connectedPlatform = new ConnectedPlatform();
            List<ConnectedPlatform> list = connectedPlatformService.getConnectedPlatformByUserId(userService.getCurrentUser().getId());
            for (ConnectedPlatform platform : list) {
                if (platform.getSocialPlatform().toString().equals("VK")) connectedPlatform = platform;
            }
            UserActor actor = new UserActor((int) connectedPlatform.getVkId(), connectedPlatform.getAccessToken());
            GetResponse getResponse = vk.friends().get(actor).execute();
            List<Integer> friendsId = getResponse.getItems();
            return friendsId;
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }


    private FriendsInfo fillFriendsInfo(Integer friendId) {
        FriendsInfo friendsInfo = new FriendsInfo();
        UserXtrCounters vkFields = getFriendsField(friendId);
        friendsInfo.setUserId(friendId);
        friendsInfo.setFirstName(vkFields.getFirstName());
        friendsInfo.setLastName(vkFields.getLastName());
        friendsInfo.setSmallPhotoUrl(vkFields.getPhoto100());
        friendsInfo.setMediumPhotoUrl(vkFields.getPhoto200());
        friendsInfo.setBigPhotoUrl(vkFields.getPhoto400Orig());
        friendsInfo.setPhoneNumber(vkFields.getMobilePhone());
        return friendsInfo;
    }
}
