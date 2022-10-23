package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    @FindBy(id = "nav-notes-tab")
    private WebElement navigateToNotesTab;

    @FindBy(id = "addNoteButton")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleInput;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionInput;

    @FindBy(id = "noteSaveButton")
    private WebElement noteSaveButton;

    @FindBy(className = "note-title-content")
    private WebElement firstNoteTitleContent;

    @FindBy(className = "note-description-content")
    private WebElement firstNoteDescriptionContent;

    @FindBy(id = "noteEditButton")
    private WebElement noteEditButton;

    private WebDriverWait webDriverWait;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.titleContains("Home"));
    }

    public void navigateToNotesTab() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
        navigateToNotesTab.click();
    }

    public void addNote(String title, String description) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("addNoteButton")));

        addNoteButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteSaveButton")));

        noteTitleInput.sendKeys(title);
        noteDescriptionInput.sendKeys(description);
        noteSaveButton.click();
    }

    public Note getFirstNote() {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("note-title-content")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("note-description-content")));

        Note note = new Note();
        note.setNoteTitle(firstNoteTitleContent.getText());
        note.setNoteDescription(firstNoteDescriptionContent.getText());
        return note;
    }

    public void editFirstNote(String newTitle, String newDescription) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(noteEditButton));
        noteEditButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteSaveButton")));

        noteTitleInput.click();
        noteTitleInput.clear();
        noteTitleInput.sendKeys(newTitle);

        noteDescriptionInput.click();
        noteDescriptionInput.clear();
        noteDescriptionInput.sendKeys(newDescription);

        noteSaveButton.click();
    }
}
