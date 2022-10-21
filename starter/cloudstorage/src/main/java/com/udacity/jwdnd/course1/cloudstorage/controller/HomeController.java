package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private FileService fileService;

    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        String userName = authentication.getName();
        model.addAttribute("files", fileService.getAllFilesForUser(userName));
        return "home";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile file, Model model) {
        String userName = authentication.getName();
        // TODO here?
        try {
            fileService.saveFileForUser(file, userName);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        model.addAttribute("files", fileService.getAllFilesForUser(userName));
        return "home";
    }

    // actually this is for downloading the file.
    @GetMapping("/viewFile")
    public String viewFile(Authentication authentication, @RequestParam("fileId") int fileId, Model model) {
        System.out.println("I am in viewFile!");
        // TODO here
        String userName = authentication.getName();
        model.addAttribute("files", fileService.getAllFilesForUser(userName));
        return "home";
    }

    // if we do post mapping here it seems like the frontend can't connect to it via <href>.
    @GetMapping("/deleteFile")
    public String deleteFile(Authentication authentication, @RequestParam("fileId") int fileId, Model model) {
        String userName = authentication.getName();

        // 1. I can delete a thing that doesn't exist. How should that pan out?
        // 2. Can I delete somebody else's file with this scheme?
        if (fileService.canFileBeDeletedByUser(fileId, userName)) {
            fileService.deleteFileById(fileId);
        } else {
            // ERROR! how to handle this? TODO
        }

        model.addAttribute("files", fileService.getAllFilesForUser(userName));
        return "home";
    }
}
