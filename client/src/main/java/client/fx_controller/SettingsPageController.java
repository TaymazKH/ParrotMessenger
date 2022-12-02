package client.fx_controller;

import client.page_data.PageData;
import client.page_data.SettingsPageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import client.util.Dialogs;
import shared.request.SettingsPageRequest;
import shared.util.Command;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsPageController extends PageController {
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Label privacyLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button togglePrivacyButton;
    @FXML
    private Button toggleInfoVisibilityButton;
    @FXML
    private Button changeUsernameButton;
    @FXML
    private Button changePasswordButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Button deactivateButton;
    @FXML
    private Button deleteButton;

    ////////////////////////////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        sendRequest(new SettingsPageRequest(Command.back));
    }

    @FXML
    void changePassword(ActionEvent event) {
        sendRequest(new SettingsPageRequest(Command.changePassword,
                new String[]{showInputDialog("EnterCurrentPassword"),
                        showInputDialog("EnterNewPassword")}));
//        sendRequest(new SettingsPageRequest(Command.changePassword,
//                new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterCurrentPassword")),
//                        JOptionPane.showInputDialog(Dialogs.get("EnterNewPassword"))}));
    }

    @FXML
    void changeUsername(ActionEvent event) {
        sendRequest(new SettingsPageRequest(Command.changeUsername, new String[]{showInputDialog("EnterUsername")}));
//        sendRequest(new SettingsPageRequest(Command.changeUsername, new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))}));
    }

    @FXML
    void deactivateAccount(ActionEvent event) {
        sendRequest(new SettingsPageRequest(Command.deactivateAccount));
    }

    @FXML
    void deleteAccount(ActionEvent event) {
        sendRequest(new SettingsPageRequest(Command.deleteAccount, new String[]{showInputDialog("EnterCurrentPassword")}));
//        sendRequest(new SettingsPageRequest(Command.deleteAccount, new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterCurrentPassword"))}));
    }

    @FXML
    void home(ActionEvent event) {
        sendRequest(new SettingsPageRequest(Command.home));
    }

    @FXML
    void logout(ActionEvent event) {
        sendRequest(new SettingsPageRequest(Command.logOut));
    }

    @FXML
    void toggleInfoVisibility(ActionEvent event) {
        sendRequest(new SettingsPageRequest(Command.toggleInfoVisibility));
    }

    @FXML
    void togglePrivacy(ActionEvent event) {
        sendRequest(new SettingsPageRequest(Command.togglePrivacy));
    }

    @Override
    public void update(PageData pageData) {
        SettingsPageData data = (SettingsPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        privacyLabel.setText(data.getPrivacyText());
        infoLabel.setText(data.getInfoVisibilityText());
        usernameLabel.setText(data.getUsernameText());
        passwordLabel.setText(data.getPasswordText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.settings);
        passSelf();
    }
}
