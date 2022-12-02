package client.fx_controller;

import client.page_data.MessagingPageData;
import client.page_data.PageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import shared.request.MessagingPageRequest;
import client.util.Dialogs;
import shared.util.Command;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MessagingPageController extends PageController {
    @FXML
    private ImageView messageImage;
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private ImageView sendersImage;
    @FXML
    private TextField messageText;
    @FXML
    private Label messageLabel;
    @FXML
    private Button newMessageButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button forwardToUserButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Label postedTimeLabel;
    @FXML
    private Label repostedOrEditedLabel;
    @FXML
    private Label seenLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    ///////////////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        sendRequest(new MessagingPageRequest(Command.back));
    }

    @FXML
    void delete(ActionEvent event) {
        sendRequest(new MessagingPageRequest(Command.delete));
    }

    @FXML
    void edit(ActionEvent event) {
        sendRequest(new MessagingPageRequest(Command.edit,new String[]{messageText.getText()}));
    }

    @FXML
    void forwardToUser(ActionEvent event) {
        sendRequest(new MessagingPageRequest(Command.forwardToUser,new String[]{showInputDialog("EnterUsername")}));
//        sendRequest(new MessagingPageRequest(Command.forwardToUser,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))}));
    }

    @FXML
    void home(ActionEvent event) {
        sendRequest(new MessagingPageRequest(Command.home));
    }

    @FXML
    void newMessage(ActionEvent event) {
        sendRequest(new MessagingPageRequest(Command.newMessage,new String[]{messageText.getText()},selectImage()));
    }

    @FXML
    void save(ActionEvent event) {
        sendRequest(new MessagingPageRequest(Command.save));
    }

    @FXML
    void showNext(ActionEvent event) {
        sendRequest(new MessagingPageRequest(Command.showNext));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        sendRequest(new MessagingPageRequest(Command.showPrevious));
    }

    @Override
    public void update(PageData pageData) {
        MessagingPageData data = (MessagingPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        messageLabel.setText(data.getMessageText());
        nameLabel.setText(data.getNameText());
        postedTimeLabel.setText(data.getPostedTime());
        repostedOrEditedLabel.setText(data.getRepostedOrEdited());
        seenLabel.setText(data.getSeen());
        messageImage.setImage(convertImage(data.getPostImage()));
        sendersImage.setImage(convertImage(data.getProfilePicture()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.messaging);
        passSelf();
    }
}
