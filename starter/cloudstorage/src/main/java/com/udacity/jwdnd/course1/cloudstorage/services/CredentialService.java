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
        Integer credentialId = credential.getCredentialId();
        String encryptionKey;
        if (credentialId == null) {
            encryptionKey = encryptionService.getAEncryptionKey();
        } else {
            // means we already have this credential in database. In that case,
            // instead of generating a new encryption key, we can just use the old one
            // in the database. this has the added benefit of when user click on save and if
            // he has the same password as before, the encrypted password won't change for him.
            encryptionKey = credentialMapper.getCredentialById(credentialId).getKey();
        }
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encryptionKey);

        // set correct fields on credential object that we want to save.
        credential.setUserId(userId);
        credential.setKey(encryptionKey);
        credential.setPassword(encryptedPassword);

        if (credentialId == null) {
            // save
            return credentialMapper.insertCredential(credential);
        } else {
            // edit
            return credentialMapper.updateCredential(credential);
        }
    }

    public List<Credential> getAllCredentialsForUser(String userName) {
        int userId = userService.getUserIdFromName(userName);
        return credentialMapper.getCredentialsForUser(userId);
    }

    public int deleteCredentialWithId(int credentialId) {
        return credentialMapper.deleteCredentialById(credentialId);
    }
}
