package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private NoteService noteService;

    @GetMapping
    public String getHomePage(Authentication authentication, @ModelAttribute("noteObject") Note noteObject, Model model) {
        String userName = authentication.getName();

        model.addAttribute("files", fileService.getAllFilesForUser(userName));
        model.addAttribute("notes", noteService.getAllNotesForUser(userName));
        model.addAttribute("noteObject", new Note());

        return "home";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile file, Model model) {
        String userName = authentication.getName();
        if (file.isEmpty()) {
            model.addAttribute("error", "You haven't selected a file for upload. Please choose a file before uploading.");
        } else if (! fileService.isFileAvailableToUser(file, userName)) {
            model.addAttribute("error", "Upload failed. You can't upload two files with the same name!");
        } else {
            try {
                fileService.saveFileForUser(file, userName);
                model.addAttribute("saveSuccess", true);
            } catch (IOException e) {
                model.addAttribute("saveFailure", true);
                e.printStackTrace();
            }
        }
        return "result";
    }

    // actually this is for downloading the file.
    @GetMapping("/viewFile")
    public ResponseEntity<byte[]> viewFile(Authentication authentication, @RequestParam("fileId") int fileId, Model model) {
        String userName = authentication.getName();
        File file = fileService.getFileById(fileId);

        if (! fileService.isFileAccessibleToUser(fileId, userName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFileData());
    }

    // if we do post mapping here it seems like the frontend can't connect to it via <href>.
    @GetMapping("/deleteFile")
    public String deleteFile(Authentication authentication, @RequestParam("fileId") int fileId, Model model) {
        String userName = authentication.getName();

        // 1. I can delete a thing that doesn't exist. How should that pan out?
        // 2. Can I delete somebody else's file with this scheme?
        if (fileService.isFileAccessibleToUser(fileId, userName)) {
            if (fileService.deleteFileById(fileId) <= 0) {
                // delete not successful
                model.addAttribute("saveFailure", true);
            } else {
                model.addAttribute("saveSuccess", true);
            }
        } else {
            model.addAttribute("error", "An error happened. You can't access other people's files!!!!");
        }

        return "result";
    }

    @PostMapping("/uploadNote")
    public String uploadNote(Authentication authentication, @ModelAttribute("noteObject") Note noteObject, Model model) {
        String userName = authentication.getName();

        noteService.saveNoteForUser(noteObject, userName);
        model.addAttribute("saveSuccess", true);
        return "result";
    }

    @GetMapping("/deleteNote")
    public String deleteNote(Authentication authentication, @RequestParam("noteId") int noteId, Model model) {
        String userName = authentication.getName();

        noteService.deleteNoteWithId(noteId);
        model.addAttribute("saveSuccess", true);
        return "result";
    }
}
