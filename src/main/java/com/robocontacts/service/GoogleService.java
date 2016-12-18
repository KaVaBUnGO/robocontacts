package com.robocontacts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.Link;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.PreconditionFailedException;
import com.google.gdata.util.ServiceException;
import com.robocontacts.domain.*;
import com.robocontacts.dto.GoogleAuthResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ekaterina on 15.11.2016.
 */

@Service
public class GoogleService {
    private static final Logger log = LoggerFactory.getLogger(GoogleService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ConnectedPlatformService connectedPlatformService;
    private final UserService userService;

    private final String CLIENT_ID = "816918176038-l2p8jenb4rrhtfvbtk8a2jji1m9k98vq.apps.googleusercontent.com";
    private final String CLIENT_SECRET = "dI7ikjKLQKSfFuJNfgOVxwPJ";
    private final String REDIRECT_URI = "http://localhost:8080/oauth2callback";
    private final String APPLICATION_NAME = "robocontacts";
    private final String SCOPE = "https://www.google.com/m8/feeds";
    private final String USER_EMAIL = "katyushkachernova@gmail.com";


    @Autowired
    public GoogleService(ConnectedPlatformService connectedPlatformService, UserService userService) {
        this.connectedPlatformService = connectedPlatformService;
        this.userService = userService;
    }

    public String getOAuthUrl() {
        try {
            URIBuilder uriBuilder = new URIBuilder("https://accounts.google.com/o/oauth2/auth");
            uriBuilder.addParameter("client_id", CLIENT_ID);
            uriBuilder.addParameter("redirect_uri", REDIRECT_URI);
            uriBuilder.addParameter("scope", SCOPE);
            uriBuilder.addParameter("response_type", "code");
            return uriBuilder.toString();
        } catch (URISyntaxException e) {
            log.debug("Create Google oauth url failed");
            // todo: implement custom exceptions later
            throw new RuntimeException("Create Google oauth url failed");
        }
    }

    @Transactional
    public void connect(String code) {
        try {

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
            String content = IOUtils.toString(response.getEntity().getContent());
            GoogleAuthResponse googleAuthResponse = objectMapper.readValue(content, GoogleAuthResponse.class);

            ConnectedPlatform connectedPlatform = new ConnectedPlatform();
            User user = userService.getCurrentUser();
            connectedPlatform.setUser(user);
            connectedPlatform.setAccessToken(googleAuthResponse.getAccess_token());
            connectedPlatform.setSocialPlatform(SocialPlatform.GOOGLE);
            connectedPlatform.setExpiresIn(googleAuthResponse.getExpires_in());
            connectedPlatform.setVkId(0);
            connectedPlatformService.save(connectedPlatform);
            ArrayList<String> SCOPES = new ArrayList<>();
            SCOPES.add(SCOPE);
            GoogleCredential.Builder builder = new GoogleCredential.Builder();
            builder.setTransport(GoogleNetHttpTransport.newTrustedTransport());
            builder.setJsonFactory(JacksonFactory.getDefaultInstance());
            builder.setClientSecrets(CLIENT_ID, CLIENT_SECRET);
            GoogleCredential googleCredential1 = builder.build();
            googleCredential1.setAccessToken(googleAuthResponse.getAccess_token()).setExpiresInSeconds(googleAuthResponse.getExpires_in());
            List<FriendsInfoGoogle> friendsInfoGoogles = new ArrayList<>();
            ContactsService contactsService = new ContactsService(APPLICATION_NAME);
            contactsService.setOAuth2Credentials(googleCredential1);
            Query query = new Query(new URL("https://www.google.com/m8/feeds/contacts/default/full"));
            query.setMaxResults(10_000);
            ContactFeed allContactsFeed = contactsService.getFeed(query, ContactFeed.class);
            for (ContactEntry contact : allContactsFeed.getEntries()) {
                if (contact.hasPhoneNumbers()) {
                    FriendsInfoGoogle friendsInfoGoogle = new FriendsInfoGoogle();
                    friendsInfoGoogle.setUserId(contact.getId());
                    friendsInfoGoogle.setPhoneNumber(contact.getPhoneNumbers().toString());
                    friendsInfoGoogle.setFullName(contact.getName().getFullName().toString());
                    friendsInfoGoogle.setPhotoUrl(contact.getContactPhotoLink().getHref());
                    friendsInfoGoogles.add(friendsInfoGoogle);
                    if (contact.hasName()) {
                        Name name = contact.getName();
                        if (name.hasFullName()) {
                            String fullNameToDisplay = name.getFullName().getValue();
                            System.out.print("\t\t" + fullNameToDisplay);
                        } else {
                            System.out.println("\t\t (no full name found)");
                        }
                    }
                    for (Email email : contact.getEmailAddresses()) {
                        System.out.print(" " + email.getAddress());

                        //System.out.println(contact.getEmailAddresses());
                    }
                    for (PhoneNumber phoneNumber : contact.getPhoneNumbers())
                        System.out.print(" " + phoneNumber.getPhoneNumber());

                    byte[] updPhoto = getBytePhoto();

                    //updatePhoto(contactsService, updPhoto, contact);
                }
                System.out.println(" " + contact.getId());
            }


        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // } catch (ServiceException e) {
            //    e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    public List<FriendsInfoGoogle> getFriendsInfoGoogle() {
        List<FriendsInfoGoogle> friendsInfoGoogles = new ArrayList<>();
        ContactsService contactsService = getContactsService();
        try {
            Query query = new Query(new URL("https://www.google.com/m8/feeds/contacts/default/full"));
            query.setMaxResults(10_000);
            ContactFeed allContactsFeed = contactsService.getFeed(query, ContactFeed.class);
            for (ContactEntry contact : allContactsFeed.getEntries()) {
                if (contact.hasPhoneNumbers()) {
                    FriendsInfoGoogle friendsInfoGoogle = new FriendsInfoGoogle();
                    friendsInfoGoogle.setUserId(contact.getId());
                    friendsInfoGoogle.setPhoneNumber(contact.getPhoneNumbers().toString()); // неверно, лист телефонов возвращается и неверно к стрингу приводится
                    friendsInfoGoogle.setFullName(contact.getName().getFullName().getValue());
                    friendsInfoGoogle.setPhotoUrl(contact.getContactPhotoLink().getHref());
                    friendsInfoGoogles.add(friendsInfoGoogle);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return friendsInfoGoogles;
    }


    private byte[] getBytePhoto() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            URL toDownload = new URL("https://pp.vk.me//c604826//v604826439//10848//6qPcMv4SLBg.jpg");
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }


    private void updatePhoto(ContactsService myService, byte[] photoData, ContactEntry contact) throws ServiceException, IOException {
        Link photoLink = contact.getContactPhotoLink();
        URL photoUrl = null;
        photoUrl = new URL(photoLink.getHref());
        com.google.gdata.client.Service.GDataRequest request = null;
        request = myService.createRequest(com.google.gdata.client.Service.GDataRequest.RequestType.UPDATE,
                photoUrl, new ContentType("image/jpeg"));

        request.setEtag(photoLink.getEtag());
        OutputStream requestStream = request.getRequestStream();
        requestStream.write(photoData);

        try {
            request.execute();
        } catch (PreconditionFailedException e) {
            // Etags mismatch: handle the exception.
        }
    }


    public ContactsService getContactsService() {
        return getContactsService(userService.getCurrentUser());
    }

    public ContactsService getContactsService(long id) {
        return getContactsService(userService.getUserById(id));
    }

    private ContactsService getContactsService(User user) {
        ContactsService contactsService = new ContactsService(APPLICATION_NAME);
        ConnectedPlatform connectedPlatform = new ConnectedPlatform();
        GoogleCredential.Builder builder = new GoogleCredential.Builder();
        try {
            builder.setTransport(GoogleNetHttpTransport.newTrustedTransport());
            builder.setJsonFactory(JacksonFactory.getDefaultInstance());
            builder.setClientSecrets(CLIENT_ID, CLIENT_SECRET);
            GoogleCredential googleCredential1 = builder.build();
            List<ConnectedPlatform> connectedPlatforms = user.getSocialPlatforms();
            for (ConnectedPlatform platform : connectedPlatforms) {
                if (platform.getSocialPlatform().toString().equals("GOOGLE")) connectedPlatform = platform;
            }
            googleCredential1.setAccessToken(connectedPlatform.getAccessToken()).setExpiresInSeconds(connectedPlatform.getExpiresIn());
            contactsService.setOAuth2Credentials(googleCredential1);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return contactsService;
    }


}
