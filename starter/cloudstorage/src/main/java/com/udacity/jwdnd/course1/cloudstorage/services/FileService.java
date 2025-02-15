package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private UserService userService;
    @Autowired
    private FileMapper fileMapper;

    public int saveFileForUser(MultipartFile multipartFile, String userName) throws IOException {
        // serves as a check in the backend that we don't have duplicated filename
        // for this user. Shouldn't happen anyways because the controller already makes sure
        // that user doesn't have a duplicated file before calling this method.
        assert isFileAvailableToUser(multipartFile, userName);

        int userId = userService.getUserIdFromName(userName);

        File fileToSave = new File();
        fileToSave.setFileData(multipartFile.getBytes());
        fileToSave.setUserId(userId);
        fileToSave.setFileSize(String.valueOf(multipartFile.getSize()));
        fileToSave.setFileName(multipartFile.getOriginalFilename());
        fileToSave.setContentType(multipartFile.getContentType());

        return fileMapper.insertFile(fileToSave);
    }

    public List<File> getAllFilesForUser(String userName) {
        int userId = userService.getUserIdFromName(userName);
        return fileMapper.getFilesForUser(userId);
    }

    public File getFileById(int fileId) {
        return fileMapper.getFileById(fileId);
    }

    public int deleteFileById(int id) {
        return fileMapper.deleteFileById(id);
    }

    // a user can only access a file that is owned by him. -- He can't
    // access a file owned by other people.
    public boolean isFileAccessibleToUser(int fileId, String userName) {
        File file = fileMapper.getFileById(fileId);
        return file.getUserId() == userService.getUserIdFromName(userName);
    }

    // a file is available for user to upload if there isn't another
    // file with the same name already in the database for this user.
    public boolean isFileAvailableToUser(MultipartFile file, String userName) {
        String fileName = file.getOriginalFilename();
        int userId = userService.getUserIdFromName(userName);
        File fileWithThisNameForUser = fileMapper.getFileForUserWithName(userId, fileName);
        return fileWithThisNameForUser == null;
    }

    public boolean canFindFile(int fileId) {
        return getFileById(fileId) != null;
    }
}
