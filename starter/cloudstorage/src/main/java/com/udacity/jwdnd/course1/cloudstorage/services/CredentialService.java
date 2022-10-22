package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    @Autowired
    private CredentialMapper credentialMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private EncryptionService encryptionService;

    public int saveOrEditCredentialForUser(Credential credential, String userName) {
        int userId = userService.getUserIdFromName(userName);
        String encryptionKey = encryptionService.getAEncryptionKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encryptionKey);

        // set correct fields on credential object that we want to save.
        credential.setUserId(userId);
        credential.setKey(encryptionKey);
        credential.setPassword(encryptedPassword);

        if (credential.getCredentialId() == null) {
            // save
            return credentialMapper.insertCredential(credential);
        } else {
            // edit
            return credentialMapper.updateCredential(credential);
        }
    }

    // get list of unencrypted credentials for a user.
    public List<Credential> getAllCredentialsForUser(String userName) {
        int userId = userService.getUserIdFromName(userName);
        return credentialMapper.getCredentialsForUser(userId);
    }
}
