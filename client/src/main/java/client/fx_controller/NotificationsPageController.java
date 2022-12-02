package client.fx_controller;

import client.page_data.NotificationsPageData;
import client.page_data.PageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import shared.request.NotificationsPageRequest;
import client.util.Dialogs;
import shared.util.Command;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificationsPageController extends PageController {
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Label notificationLabel;
    @FXML
    private ImageView profilePicture;
    @FXML
    private Label dateLabel;
    @FXML
    private Button nextButton;

    ///////////////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        sendRequest(new NotificationsPageRequest(Command.back));
    }

    @FXML
    void home(ActionEvent event) {
        sendRequest(new NotificationsPageRequest(Command.home));
    }

    @FXML
    void showNext(ActionEvent event) {
        sendRequest(new NotificationsPageRequest(Command.showNextNotification));
    }

    @Override
    public void update(PageData pageData) {
        NotificationsPageData data = (NotificationsPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        dateLabel.setText(data.getDateText());
        notificationLabel.setText(data.getNotificationText());
        profilePicture.setImage(convertImage(data.getProfileImage()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.notifications);
        passSelf();
    }
}
