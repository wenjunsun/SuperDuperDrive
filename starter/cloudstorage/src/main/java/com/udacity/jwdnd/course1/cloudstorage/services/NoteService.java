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

    // if note.noteId == null, then this is a fresh note, and we
    // will save it. if noteId != null, we edit the note instead.
    public int saveOrEditNoteForUser(Note note, String userName) {
        note.setUserId(userService.getUserIdFromName(userName));
        if (note.getNoteId() == null) {
            return noteMapper.insertNote(note);
        } else {
            return noteMapper.updateNote(note);
        }
    }

    public int deleteNoteWithId(int noteId) {
        return noteMapper.deleteNote(noteId);
    }
}
