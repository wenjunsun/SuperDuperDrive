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

    // a user can only delete a file that is owned by him. -- He can't
    // delete a file owned by other people.
    public boolean isFileAccessibleToUser(int fileId, String userName) {
        File file = fileMapper.getFileById(fileId);
        return file.getUserId() == userService.getUserIdFromName(userName);
    }
}
