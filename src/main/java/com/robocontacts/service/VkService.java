package com.robocontacts.service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.OAuthException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * Created by ekaterina on 30.10.2016.
 */

@Service
public class VkService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkService.class);
    private static final String REDIRECT_URI = "http://localhost:8080/callback";
    private static final Integer APP_ID = 5683209;
    private static final String CLIENT_SECRET = "cEHoBbnhZEcTiDDfA8l1";
    private static final String code = "friends";

    public String auth() {
        return getOAuthUrl();
    }

    private String getOAuthUrl() {
        return "https://oauth.vk.com/authorize?client_id=" + APP_ID + "&display=page&redirect_uri=" +
                REDIRECT_URI + "&scope=groups&response_type=code";
    }


    public void connect(String code) {
        try {
            TransportClient transportClient = new HttpTransportClient();
            VkApiClient vk = new VkApiClient(transportClient);
            UserAuthResponse authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, code)
                    .execute();
            UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
            LOGGER.debug("Actor {}", actor);
        } catch (OAuthException e) {
            e.getRedirectUri();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
