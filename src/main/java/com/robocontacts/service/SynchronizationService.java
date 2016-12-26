package com.robocontacts.service;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.Link;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.sites.SitesLink;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.PreconditionFailedException;
import com.google.gdata.util.ServiceException;
import com.robocontacts.domain.FriendsInfoGoogle;
import com.robocontacts.domain.FriendsInfoVk;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by ekaterina on 09.12.2016.
 */


@Service
public class SynchronizationService {
    private static final Logger log = LoggerFactory.getLogger(GoogleService.class);

    private final GoogleService googleService;
    private final VkService vkService;

    public SynchronizationService(GoogleService googleService, VkService vkService) {
        this.googleService = googleService;
        this.vkService = vkService;
    }

    public void updatePhoto(String vkPhoto, String googlePhoto, Long id){
        byte[] updPhoto = getBytePhoto(vkPhoto);
        ContactsService contactsService = googleService.getContactsService(id);
        try {
            updatePhoto(contactsService, updPhoto, googlePhoto);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private byte[] getBytePhoto(String url) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            URL toDownload = new URL(url);
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


    private void updatePhoto(ContactsService myService, byte[] photoData, String link) throws ServiceException, IOException {
        Link photoLink = new Link(SitesLink.Rel.SOURCE, Link.Type.ATOM, link);
        URL photoUrl = null;
        photoUrl = new URL(link);
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

}
