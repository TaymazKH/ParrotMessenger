package client.offline.fx_controller;

import client.fx_controller.PageController;
import client.offline.event.EventType;
import client.offline.event.MessagingPageEvent;
import client.page_data.MessagingPageData;
import client.page_data.PageData;
import client.util.Dialogs;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MessagingPageController extends PageController {
    @FXML
    private ImageView messageImage;
    @FXML
    private ImageView sendersImage;
    @FXML
    private Label messageLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label postedTimeLabel;
    @FXML
    private Label repostedOrEditedLabel;
    @FXML
    private Label seenLabel;

    ///////////////////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        runEvent(new MessagingPageEvent(EventType.back));
    }

    @FXML
    void home(ActionEvent event) {
        runEvent(new MessagingPageEvent(EventType.home));
    }

    @FXML
    void showNext(ActionEvent event) {
        runEvent(new MessagingPageEvent(EventType.showNext));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        runEvent(new MessagingPageEvent(EventType.showPrevious));
    }

    @Override
    public void update(PageData pageData) {
        MessagingPageData data = (MessagingPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION, Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
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
