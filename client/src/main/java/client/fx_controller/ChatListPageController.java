package client.fx_controller;

import client.page_data.ChatListPageData;
import client.page_data.PageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import shared.request.ChatListPageRequest;
import client.util.Dialogs;
import shared.util.Command;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatListPageController extends PageController {
    @FXML
    private Button deselectUserButton;
    @FXML
    private Button removeThisUserButton;
    @FXML
    private Button selectGroupButton;
    @FXML
    private Button manageGroupButton;
    @FXML
    private Button addUserButton;
    @FXML
    private Button selectUserButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Button messageUserButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private ImageView profileImage;
    @FXML
    private Label nameLabel;
    @FXML
    private Button savedMessagesButton;
    @FXML
    private Button pvGroupsButton;
    @FXML
    private Button selectionModeButton;
    @FXML
    private Button deselectGroupButton;
    @FXML
    private Button sendMessageButton;
    @FXML
    private Button createGroupButton;
    @FXML
    private Label sectionTitleLabel;

    ////////////////////////////////////////////////////////////
    @FXML
    void addUser(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.addUser,new String[]{showInputDialog("EnterUsername")}));
//        sendRequest(new ChatListPageRequest(Command.addUser,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))}));
    }

    @FXML
    void back(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.back));
    }

    @FXML
    void createGroup(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.stopSelectionAndCreateGroup,new String[]{showInputDialog("EnterGroupName")}));
//        sendRequest(new ChatListPageRequest(Command.stopSelectionAndCreateGroup,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterGroupName"))}));
    }

    @FXML
    void deselectGroup(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.deselectGroup,new String[]{showInputDialog("EnterGroupName")}));
//        sendRequest(new ChatListPageRequest(Command.deselectGroup,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterGroupName"))}));
    }

    @FXML
    void deselectUser(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.deselectUser,new String[]{showInputDialog("EnterUsername")}));
//        sendRequest(new ChatListPageRequest(Command.deselectUser,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))}));
    }

    @FXML
    void home(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.home));
    }

    @FXML
    void manageGroup(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.enterPVGroupManagement));
    }

    @FXML
    void messageUser(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.messageUser,new String[]{showInputDialog("EnterUsername")}));
//        sendRequest(new ChatListPageRequest(Command.messageUser,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))}));
    }

    @FXML
    void removeThisUser(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.removeThisUser));
    }

    @FXML
    void selectGroup(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.selectGroup,new String[]{showInputDialog("EnterGroupName")}));
//        sendRequest(new ChatListPageRequest(Command.selectGroup,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterGroupName"))}));
    }

    @FXML
    void selectUser(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.selectUser,new String[]{showInputDialog("EnterUsername")}));
//        sendRequest(new ChatListPageRequest(Command.selectUser,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))}));
    }

    @FXML
    void selectionMode(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.selectionMode));
    }

    @FXML
    void sendMessage(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.stopSelectionAndSendMessage,new String[]{showInputDialog("EnterMessageText")},selectImage()));
//        sendRequest(new ChatListPageRequest(Command.stopSelectionAndSendMessage,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterMessageText"))},selectImage()));
    }

    @FXML
    void showNext(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.showNext));
    }

    @FXML
    void showPVGroups(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.showPVGroups));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.showPrevious));
    }

    @FXML
    void showSavedMessages(ActionEvent event) {
        sendRequest(new ChatListPageRequest(Command.showSavesMessages));
    }

    @Override
    public void update(PageData pageData) {
        ChatListPageData data = (ChatListPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        setComponentsVisibility(data.getSection());
        sectionTitleLabel.setText(data.getSectionTitle());
        nameLabel.setText(data.getNameText());
        profileImage.setImage(convertImage(data.getImage()));
    }

    private void setComponentsVisibility(int section){
        messageUserButton.setVisible(section==0);
        savedMessagesButton.setVisible(section==0);
        pvGroupsButton.setVisible(section==0);
        selectionModeButton.setVisible(section==0);

        manageGroupButton.setVisible(section==1);
        profileImage.setVisible(section!=1);

        addUserButton.setVisible(section==2);
        removeThisUserButton.setVisible(section==2);

        selectUserButton.setVisible(section==3);
        selectGroupButton.setVisible(section==3);
        deselectUserButton.setVisible(section==3);
        deselectGroupButton.setVisible(section==3);
        sendMessageButton.setVisible(section==3);
        createGroupButton.setVisible(section==3);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.chatList);
        passSelf();
    }
}
