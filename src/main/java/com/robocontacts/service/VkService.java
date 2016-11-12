package com.robocontacts.service;

import com.robocontacts.domain.ConnectedPlatform;
import com.robocontacts.domain.CurrentUser;
import com.robocontacts.domain.SocialPlatform;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.OAuthException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;


/**
 * Created by ekaterina on 30.10.2016.
 */

@Service
public class VkService {
    private static final Logger log = LoggerFactory.getLogger(VkService.class);
    private final ConnectedPlatformService connectedPlatformService;

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

    public VkService(ConnectedPlatformService connectedPlatformService) {
        this.connectedPlatformService = connectedPlatformService;
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

    public void connect(String code) {
        try {
            TransportClient transportClient = new HttpTransportClient();
            VkApiClient vk = new VkApiClient(transportClient);
            UserAuthResponse authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(appId, appSecret, getRedirectUri(), code)
                    .execute();
            UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());

            ConnectedPlatform connectedPlatform = new ConnectedPlatform();
            CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            connectedPlatform.setUser(currentUser.getUser());
            connectedPlatform.setAccessToken(authResponse.getAccessToken());
            connectedPlatform.setExpiresIn(authResponse.getExpiresIn());
            connectedPlatform.setVkId(authResponse.getUserId());
            connectedPlatform.setSocialPlatform(SocialPlatform.VK);
            connectedPlatformService.save(connectedPlatform);


            log.debug("Actor {}", actor);
        } catch (OAuthException e) {

            e.getRedirectUri();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRedirectUri() {
        return host + redirectUri;
    }


   // public void get


}
