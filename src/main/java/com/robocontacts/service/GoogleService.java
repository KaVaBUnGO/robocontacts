package com.robocontacts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.robocontacts.dto.GoogleAuthResponse;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by ekaterina on 15.11.2016.
 */

@Service
public class GoogleService {
    private static final Logger log = LoggerFactory.getLogger(GoogleService.class);

    private final ConnectedPlatformService connectedPlatformService;
    private final UserService userService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String CLIENT_ID = "816918176038-l2p8jenb4rrhtfvbtk8a2jji1m9k98vq.apps.googleusercontent.com";
    private final String CLIENT_SECRET = "dI7ikjKLQKSfFuJNfgOVxwPJ";
    private final String REDIRECT_URI = "http://localhost:8080/oauth2callback";
    private final String APPLICATION_NAME = "robocontacts";


    public GoogleService(ConnectedPlatformService connectedPlatformService, UserService userService) {
        this.connectedPlatformService = connectedPlatformService;
        this.userService = userService;
    }


    public String getOAuthUrl() {
        try {
            URIBuilder uriBuilder = new URIBuilder("https://accounts.google.com/o/oauth2/auth");
            uriBuilder.addParameter("client_id", CLIENT_ID);
            uriBuilder.addParameter("redirect_uri", REDIRECT_URI);
            uriBuilder.addParameter("scope", "email");
            uriBuilder.addParameter("response_type", "code");
            return uriBuilder.toString();
        } catch (URISyntaxException e) {
            log.debug("Create Google oauth url failed");
            // todo: implement custom exceptions later
            throw new RuntimeException("Create Google oauth url failed");
        }
    }

    @Transactional
    public void connect(String code){
        try {

            String postToken = getTokenUrl(code);
            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpPost post = new HttpPost(
                    "https://accounts.google.com/o/oauth2/token");
            post.addHeader("Host", "accounts.google.com");
            post.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("code", code));
            urlParameters.add(new BasicNameValuePair("client_id", CLIENT_ID));
            urlParameters.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
            urlParameters.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
            urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpClient.execute(post);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                result.append(line);
            }

            GoogleAuthResponse googleAuthResponse= objectMapper.readValue(result.toString(), GoogleAuthResponse.class);

            GoogleCredential googleCredential = new GoogleCredential().setAccessToken(googleAuthResponse.getAccess_token());

            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();



        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public String getTokenUrl(String code) {
        try {
            URIBuilder uriBuilder = new URIBuilder("https://accounts.google.com/o/oauth2/token");
            uriBuilder.addParameter("code", code);
            uriBuilder.addParameter("client_id", CLIENT_ID);
            uriBuilder.addParameter("client_secret", CLIENT_SECRET);
            uriBuilder.addParameter("redirect_uri", REDIRECT_URI);
            uriBuilder.addParameter("grant_type", "authorization_code");
            return uriBuilder.toString();
        } catch (URISyntaxException e) {
            log.debug("Create Google oauth url failed");
            // todo: implement custom exceptions later
            throw new RuntimeException("Create Google oauth url failed");
        }
    }

}
