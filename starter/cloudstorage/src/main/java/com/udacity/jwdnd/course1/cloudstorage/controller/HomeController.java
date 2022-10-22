package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
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
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private EncryptionService encryptionService;

    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        String userName = authentication.getName();

        model.addAttribute("files", fileService.getAllFilesForUser(userName));
        model.addAttribute("notes", noteService.getAllNotesForUser(userName));
        model.addAttribute("noteObject", new Note());
        model.addAttribute("credentials", credentialService.getAllCredentialsForUser(userName));
        model.addAttribute("credential", new Credential());
        model.addAttribute("encryptionService", encryptionService);

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

        if (! fileService.canFindFile(fileId)) {
            // can't view/download a file that doesn't exist
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if (! fileService.isFileAccessibleToUser(fileId, userName)) {
            // can't view/download a file that isn't yours.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFileData());
    }

    // if we do post mapping here it seems like the frontend can't connect to it via <href>.
    @GetMapping("/deleteFile")
    public String deleteFile(Authentication authentication, @RequestParam("fileId") int fileId, Model model) {
        String userName = authentication.getName();

        if (! fileService.canFindFile(fileId)) {
            model.addAttribute("error", "you can't delete a file that doesn't exist");
        } else if (! fileService.isFileAccessibleToUser(fileId, userName)) {
            model.addAttribute("error", "An error happened. You can't access other people's files!!!!");
        } else {
            if (fileService.deleteFileById(fileId) <= 0) {
                // delete not successful
                model.addAttribute("saveFailure", true);
            } else {
                model.addAttribute("saveSuccess", true);
            }
        }

        return "result";
    }

    @PostMapping("/saveOrEditNote")
    public String saveOrEditNote(Authentication authentication, @ModelAttribute("noteObject") Note noteObject, Model model) {
        String userName = authentication.getName();
        Integer noteId = noteObject.getNoteId();

        if (noteId != null && ! noteService.hasAccessToNoteByUser(noteId, userName)) {
            model.addAttribute("error", "You cannot edit a note that is not yours!!!");
        } else {
            int rowsAffected = noteService.saveOrEditNoteForUser(noteObject, userName);
            if (rowsAffected <= 0) {
                model.addAttribute("saveFailure", true);
            } else {
                model.addAttribute("saveSuccess", true);
            }
        }
        return "result";
    }

    @GetMapping("/deleteNote")
    public String deleteNote(Authentication authentication, @RequestParam("noteId") int noteId, Model model) {
        String userName = authentication.getName();

        // can't delete a note that doesn't exist
        if (! noteService.canFindNote(noteId)) {
            model.addAttribute("error", "You cannot delete a note that doesn't exist!!!");
        } else if (! noteService.hasAccessToNoteByUser(noteId, userName)) {
            // can't delete a note that is not yours!
            model.addAttribute("error", "You cannot delete a note that is not yours!!!");
        } else {
            int rowsAffected = noteService.deleteNoteWithId(noteId);
            if (rowsAffected <= 0) {
                model.addAttribute("saveFailure", true);
            } else {
                model.addAttribute("saveSuccess", true);
            }
        }
        return "result";
    }

    @PostMapping("/saveOrEditCredential")
    public String saveOrEditCredential(Authentication authentication, @ModelAttribute("credential") Credential credential, Model model) {
        String userName = authentication.getName();

        int rowsAffected = credentialService.saveOrEditCredentialForUser(credential, userName);
        if (rowsAffected <= 0) {
            model.addAttribute("saveFailure", true);
        } else {
            model.addAttribute("saveSuccess", true);
        }
        return "result";
    }
}
