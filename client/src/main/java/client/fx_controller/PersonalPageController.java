package client.fx_controller;

import client.page_data.PageData;
import client.page_data.PersonalPageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import shared.request.PersonalPageRequest;
import client.util.Dialogs;
import shared.util.Command;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class PersonalPageController extends PageController {
    @FXML
    private Button toggleMuteButton;
    @FXML
    private Button toggleBanButton;
    @FXML
    private Button followButton;
    @FXML
    private Button listsButton;
    @FXML
    private ImageView profilePicture;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label realNameLabel;
    @FXML
    private Label activityLabel;
    @FXML
    private Label birthdateLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label birthdateTitleLabel;
    @FXML
    private Label emailTitleLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label phoneNumberTitleLabel;
    @FXML
    private Label bioLabel;
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Button changePhoneNumberButton;
    @FXML
    private Button changeEmailButton;
    @FXML
    private Button changeBirthdateButton;
    @FXML
    private Button changeBioButton;
    @FXML
    private Button changeProfilePictureButton;
    @FXML
    private Button hideBirthdateButton;
    @FXML
    private Button tweetsButton;
    @FXML
    private Button requestsButton;
    @FXML
    private Button notificationsButton;
    @FXML
    private Button reportButton;

    ////////////////////////////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.back));
    }

    @FXML
    void changeProfilePicture(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.changeProfilePicture,selectImage()));
    }

    @FXML
    void editBio(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.editBio, new String[]{showInputDialog("EnterBio")}));
//        sendRequest(new PersonalPageRequest(Command.editBio, new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterBio"))}));
    }

    @FXML
    void editBirthdate(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.editBirthDate, new String[]{showInputDialog("EnterBirthDate")}));
//        sendRequest(new PersonalPageRequest(Command.editBirthDate, new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterBirthDate"))}));
    }

    @FXML
    void editEmail(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.editEmail, new String[]{showInputDialog("EnterEmail")}));
//        sendRequest(new PersonalPageRequest(Command.editEmail, new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterEmail"))}));
    }

    @FXML
    void editPhoneNumber(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.editPhoneNumber, new String[]{showInputDialog("EnterPhoneNumber")}));
//        sendRequest(new PersonalPageRequest(Command.editPhoneNumber, new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterPhoneNumber"))}));
    }

    @FXML
    void follow(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.follow));
    }

    @FXML
    void gotoListsPage(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.gotoListsPage));
    }

    @FXML
    void gotoNotificationsPage(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.gotoNotificationsPage));
    }

    @FXML
    void gotoRequestsPage(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.gotoRequestsPage));
    }

    @FXML
    void gotoTweetsPage(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.gotoTweetsPage));
    }

    @FXML
    void hideBirthDate(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.hideBirthDate));
    }

    @FXML
    void home(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.home));
    }

    @FXML
    void report(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.report));
    }

    @FXML
    void toggleBan(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.toggleBan));
    }

    @FXML
    void toggleMute(ActionEvent event) {
        sendRequest(new PersonalPageRequest(Command.toggleMute));
    }

    @Override
    public void update(PageData pageData) {
        PersonalPageData data = (PersonalPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        setComponentsVisibility(data.isThisCurrentUsersPage(), data.isCanViewDetails());
        usernameLabel.setText(data.getUsername());
        bioLabel.setText(data.getBio());
        realNameLabel.setText(data.getRealName());
        activityLabel.setText(data.getActivity());
        birthdateLabel.setText(data.getBirthdate());
        emailLabel.setText(data.getEmail());
        phoneNumberLabel.setText(data.getPhoneNumber());
        profilePicture.setImage(convertImage(data.getProfileImage()));
    }

    private void setComponentsVisibility(boolean isThisCurrentUsersPage, boolean canViewDetails){
        if(isThisCurrentUsersPage){
            followButton.setVisible(false);
            toggleBanButton.setVisible(false);
            toggleMuteButton.setVisible(false);
            reportButton.setVisible(false);
        }
        else{
            listsButton.setVisible(false);
            requestsButton.setVisible(false);
            notificationsButton.setVisible(false);
            changeProfilePictureButton.setVisible(false);
            changeBioButton.setVisible(false);
            changeBirthdateButton.setVisible(false);
            hideBirthdateButton.setVisible(false);
            changeEmailButton.setVisible(false);
            changePhoneNumberButton.setVisible(false);
            if(!canViewDetails){
                birthdateTitleLabel.setVisible(false);
                birthdateLabel.setVisible(false);
                emailTitleLabel.setVisible(false);
                emailLabel.setVisible(false);
                phoneNumberTitleLabel.setVisible(false);
                phoneNumberLabel.setVisible(false);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.personal);
        passSelf();
    }
}
