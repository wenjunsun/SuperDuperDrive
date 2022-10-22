package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;
    @Autowired
    private UserService userService;

    public List<Note> getAllNotesForUser(String userName) {
        int userId = userService.getUserIdFromName(userName);
        return noteMapper.getNotesForUser(userId);
    }

    public int saveNoteForUser(Note note, String userName) {
        note.setUserId(userService.getUserIdFromName(userName));
        return noteMapper.insertNote(note);
    }

    public int deleteNoteWithId(int noteId) {
        return noteMapper.deleteNote(noteId);
    }
}
