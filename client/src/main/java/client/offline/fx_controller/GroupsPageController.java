package client.offline.fx_controller;

import client.fx_controller.PageController;
import client.offline.event.EventType;
import client.offline.event.GroupsPageEvent;
import client.page_data.GroupsPageData;
import client.page_data.PageData;
import client.util.Dialogs;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GroupsPageController extends PageController {
    @FXML
    private AnchorPane groupChatSectionPane;
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
    private ImageView messageImage;
    @FXML
    private AnchorPane groupSelectionSectionPane;
    @FXML
    private Label groupNameLabel;

    /////////////////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        runEvent(new GroupsPageEvent(EventType.back));
    }

    @FXML
    void enter(ActionEvent event) {
        runEvent(new GroupsPageEvent(EventType.enterGroup));
    }

    @FXML
    void home(ActionEvent event) {
        runEvent(new GroupsPageEvent(EventType.home));
    }

    @FXML
    void showNext(ActionEvent event) {
        runEvent(new GroupsPageEvent(EventType.showNext));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        runEvent(new GroupsPageEvent(EventType.showPrevious));
    }

    @Override
    public void update(PageData pageData) {
        GroupsPageData data = (GroupsPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION, Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        setComponentsVisibility(data.getSection());
        switch(data.getSection()){
            case 0->{
                groupNameLabel.setText(data.getGroupName());
            }
            case 1->{
                messageLabel.setText(data.getMessageText());
                nameLabel.setText(data.getNameText());
                postedTimeLabel.setText(data.getPostedTime());
                repostedOrEditedLabel.setText(data.getRepostedOrEdited());
                messageImage.setImage(convertImage(data.getPostImage()));
                sendersImage.setImage(convertImage(data.getProfilePicture()));
            }
        }
    }

    private void setComponentsVisibility(int section){
        groupSelectionSectionPane.setVisible(section==0);
        groupChatSectionPane.setVisible(section==1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.groups);
        passSelf();
    }
}
