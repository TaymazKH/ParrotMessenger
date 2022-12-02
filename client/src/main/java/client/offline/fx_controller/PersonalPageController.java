package client.offline.fx_controller;

import client.fx_controller.PageController;
import client.offline.event.EventType;
import client.offline.event.PersonalPageEvent;
import client.page_data.PageData;
import client.page_data.PersonalPageData;
import client.util.Dialogs;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonalPageController extends PageController {
    @FXML
    private ImageView profilePicture;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label realNameLabel;
    @FXML
    private Label birthdateLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label bioLabel;

    /////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        runEvent(new PersonalPageEvent(EventType.back));
    }

    @FXML
    void gotoTweetsPage(ActionEvent event) {
        runEvent(new PersonalPageEvent(EventType.gotoTweetsPage));
    }

    @FXML
    void home(ActionEvent event) {
        runEvent(new PersonalPageEvent(EventType.home));
    }

    @Override
    public void update(PageData pageData) {
        PersonalPageData data = (PersonalPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION, Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        usernameLabel.setText(data.getUsername());
        bioLabel.setText(data.getBio());
        realNameLabel.setText(data.getRealName());
        birthdateLabel.setText(data.getBirthdate());
        emailLabel.setText(data.getEmail());
        phoneNumberLabel.setText(data.getPhoneNumber());
        profilePicture.setImage(convertImage(data.getProfileImage()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.personal);
        passSelf();
    }
}
