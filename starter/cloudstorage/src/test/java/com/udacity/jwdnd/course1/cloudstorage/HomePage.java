package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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

    @FindBy(className = "note-edit-button")
    private WebElement noteEditButton;

    @FindBy(className = "note-delete-button")
    private WebElement noteDeleteButton;

    @FindBy(id = "nav-credentials-tab")
    private WebElement navigateToCredentialsTab;

    @FindBy(id = "addCredentialButton")
    private WebElement addCredentialButton;

    @FindBy(id = "saveCredentialButton")
    private WebElement saveCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement credentialURLInput;

    @FindBy(id = "credential-username")
    private WebElement credentialUserNameInput;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordInput;

    @FindBy(className = "credential-url-content")
    private WebElement firstCredentialURLContent;

    @FindBy(className = "credential-username-content")
    private WebElement firstCredentialUserNameContent;

    @FindBy(className = "credential-password-content")
    private WebElement firstCredentialPasswordContent;

    @FindBy(className = "credential-edit-button")
    private WebElement firstCredentialEditButton;

    @FindBy(className = "credential-delete-button")
    private WebElement firstCredentialDeleteButton;

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

    // if there is first note, get first note. if there isn't any note on the page, return null.
    public Note getFirstNote() {
        try {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("note-title-content")));
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("note-description-content")));
        } catch (TimeoutException timeoutException) {
            return null;
        }

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

    public void deleteFirstNote() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(noteDeleteButton));
        noteDeleteButton.click();
    }

    public void navigateToCredentialsTab() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(navigateToCredentialsTab));
        navigateToCredentialsTab.click();
    }

    public void addCredential(String url, String userName, String password) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(addCredentialButton));
        addCredentialButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(saveCredentialButton));

        credentialURLInput.sendKeys(url);
        credentialUserNameInput.sendKeys(userName);
        credentialPasswordInput.sendKeys(password);
        saveCredentialButton.click();
    }

    // this will grab the first credential in the credentials tab,
    // thus will return an encrypted version of the first credential.
    public Credential getFirstCredentialEncrypted() {
        try {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("credential-url-content")));
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("credential-username-content")));
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("credential-password-content")));
        } catch (TimeoutException timeoutException) {
            return null;
        }

        Credential credential = new Credential();
        credential.setUrl(firstCredentialURLContent.getText());
        credential.setUserName(firstCredentialUserNameContent.getText());
        credential.setPassword(firstCredentialPasswordContent.getText());
        return credential;
    }

    // this will go to the edit/viewing pane of first credential
    // and get the unencrypted version of the first credential.
    public Credential getFirstCredentialDecrypted() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(firstCredentialEditButton));
        firstCredentialEditButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));

        Credential credential = new Credential();
        // has to use this .getAttribute("value") here instead of .getText() because
        // the variables here are HTML inputs instead of text HTML tags. To get the value inputted by
        // user into an HTML input we need to use input.value.
        credential.setUrl(credentialURLInput.getAttribute("value"));
        credential.setUserName(credentialUserNameInput.getAttribute("value"));
        credential.setPassword(credentialPasswordInput.getAttribute("value"));
        return credential;
    }

    public void editFirstCredential(String newUrl, String newUserName, String newPassword) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(firstCredentialEditButton));
        firstCredentialEditButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(saveCredentialButton));

        credentialURLInput.click();
        credentialURLInput.clear();
        credentialURLInput.sendKeys(newUrl);

        credentialUserNameInput.click();
        credentialUserNameInput.clear();
        credentialUserNameInput.sendKeys(newUserName);

        credentialPasswordInput.click();
        credentialPasswordInput.clear();
        credentialPasswordInput.sendKeys(newPassword);

        saveCredentialButton.click();
    }

    public void deleteFirstCredential() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(firstCredentialDeleteButton));
        firstCredentialDeleteButton.click();
    }
}
