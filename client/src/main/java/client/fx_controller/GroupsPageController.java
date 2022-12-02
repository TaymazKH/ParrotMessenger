package client.fx_controller;

import client.page_data.GroupsPageData;
import client.page_data.PageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import shared.request.GroupsPageRequest;
import client.util.Dialogs;
import shared.util.Command;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GroupsPageController extends PageController {
    @FXML
    private AnchorPane groupChatSectionPane;
    @FXML
    private AnchorPane membersSectionPane;
    @FXML
    private AnchorPane groupSelectionSectionPane;
    @FXML
    private ImageView messageImage;
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Button showMembersButton;
    @FXML
    private Button newGroupButton;
    @FXML
    private ImageView sendersImage;
    @FXML
    private TextField messageText;
    @FXML
    private Label messageLabel;
    @FXML
    private Button newMessageButton;
    @FXML
    private Button newTimedMessageButton;
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
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button enterButton;
    @FXML
    private Label groupNameLabel;
    @FXML
    private Label groupNameTitleLabel;
    @FXML
    private Label memberCountTitleLabel;
    @FXML
    private Label memberCountLabel;
    @FXML
    private Button leaveButton;
    @FXML
    private ImageView profileImage;
    @FXML
    private Label currentMemberLabel;
    @FXML
    private Button inviteButton;
    @FXML
    private Button kickButton;
    @FXML
    private Button transferOwnershipButton;

    ///////////////////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.back));
    }

    @FXML
    void delete(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.delete));
    }

    @FXML
    void edit(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.edit,new String[]{messageText.getText()}));
    }

    @FXML
    void enter(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.enterGroup));
    }

    @FXML
    void forwardToUser(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.forwardToUser,new String[]{showInputDialog("EnterUsername")}));
//        sendRequest(new GroupsPageRequest(Command.forwardToUser,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))}));
    }

    @FXML
    void home(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.home));
    }

    @FXML
    void invite(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.invite,new String[]{showInputDialog("EnterUsername")}));
//        sendRequest(new GroupsPageRequest(Command.invite,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))}));
    }

    @FXML
    void kick(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.kick));
    }

    @FXML
    void leave(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.leave));
    }

    @FXML
    void newGroup(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.newGroup,new String[]{showInputDialog("EnterGroupName")}));
//        sendRequest(new GroupsPageRequest(Command.newGroup,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterGroupName"))}));
    }

    @FXML
    void newMessage(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.newMessage,new String[]{messageText.getText()},selectImage()));
    }

    @FXML
    void newTimedMessage(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.newTimedMessage,new String[]{messageText.getText(),showInputDialog("EnterSendDate")},selectImage()));
//        sendRequest(new GroupsPageRequest(Command.newTimedMessage,new String[]{messageText.getText(),JOptionPane.showInputDialog(Dialogs.get("EnterSendDate"))},selectImage()));
    }

    @FXML
    void save(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.save));
    }

    @FXML
    void showMembers(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.showMembers));
    }

    @FXML
    void showNext(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.showNext));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.showPrevious));
    }

    @FXML
    void showSender(MouseEvent event) {
        sendRequest(new GroupsPageRequest(Command.showTheirPage));
    }

    @FXML
    void transferOwnership(ActionEvent event) {
        sendRequest(new GroupsPageRequest(Command.transferOwnership));
    }

    @Override
    public void update(PageData pageData) {
        GroupsPageData data = (GroupsPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        setComponentsVisibility(data.getSection());
        switch(data.getSection()){
            case 0->{
                groupNameLabel.setText(data.getGroupName());
                memberCountLabel.setText(data.getMemberCount());
            }
            case 1->{
                messageLabel.setText(data.getMessageText());
                nameLabel.setText(data.getNameText());
                postedTimeLabel.setText(data.getPostedTime());
                repostedOrEditedLabel.setText(data.getRepostedOrEdited());
                messageImage.setImage(convertImage(data.getPostImage()));
                sendersImage.setImage(convertImage(data.getProfilePicture()));
            }
            case 2->{
                currentMemberLabel.setText(data.getMemberNameText());
                profileImage.setImage(convertImage(data.getMemberImage()));
                boolean isOwner = data.isOwner();
                inviteButton.setVisible(isOwner);
                kickButton.setVisible(isOwner);
                transferOwnershipButton.setVisible(isOwner);
            }
        }
    }

    private void setComponentsVisibility(int section){
        groupSelectionSectionPane.setVisible(section==0);
        groupChatSectionPane.setVisible(section==1);
        membersSectionPane.setVisible(section==2);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.groups);
        passSelf();
    }
}
