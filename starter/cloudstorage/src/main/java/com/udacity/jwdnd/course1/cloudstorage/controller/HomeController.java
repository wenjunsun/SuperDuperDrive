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
    public String viewFile(Authentication authentication, @RequestParam("filename") String fileName, Model model) {
        System.out.println("I am in viewFile!");
        // TODO here
        String userName = authentication.getName();
        model.addAttribute("files", fileService.getAllFilesForUser(userName));
        return "home";
    }

    @PostMapping("/deleteFile")
    public String deleteFile(Authentication authentication, @RequestParam("filename") String fileName, Model model) {
        System.out.println("I am in deleteFile!");
        // TODO here
        String userName = authentication.getName();
        model.addAttribute("files", fileService.getAllFilesForUser(userName));
        return "home";
    }
}
