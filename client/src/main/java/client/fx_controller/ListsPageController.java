package client.fx_controller;

import client.page_data.ListsPageData;
import client.page_data.PageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import shared.request.ListsPageRequest;
import client.util.Dialogs;
import shared.util.Command;

import java.net.URL;
import java.util.ResourceBundle;

public class ListsPageController extends PageController {
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView profilePicture;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button showTheirPageButton;
    @FXML
    private Button followingsButton;
    @FXML
    private Button muteListButton;
    @FXML
    private Button followersButton;
    @FXML
    private Button blackListButton;
    @FXML
    private Label listTypeLabel;
    @FXML
    private Label listTypeTitleLabel;
    @FXML
    private Label noListSelectedTitleLabel;
    @FXML
    private Rectangle rectangle;

    ///////////////////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        sendRequest(new ListsPageRequest(Command.back));
    }

    @FXML
    void home(ActionEvent event) {
        sendRequest(new ListsPageRequest(Command.home));
    }

    @FXML
    void remove(ActionEvent event) {
        sendRequest(new ListsPageRequest(Command.remove));
    }

    @FXML
    void showBlackList(ActionEvent event) {
        sendRequest(new ListsPageRequest(Command.showBlacklist));
    }

    @FXML
    void showFollowers(ActionEvent event) {
        sendRequest(new ListsPageRequest(Command.showFollowers));
    }

    @FXML
    void showFollowings(ActionEvent event) {
        sendRequest(new ListsPageRequest(Command.showFollowings));
    }

    @FXML
    void showMuteList(ActionEvent event) {
        sendRequest(new ListsPageRequest(Command.showMuteList));
    }

    @FXML
    void showNext(ActionEvent event) {
        sendRequest(new ListsPageRequest(Command.showNext));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        sendRequest(new ListsPageRequest(Command.showPrevious));
    }

    @FXML
    void showTheirPage(ActionEvent event) {
        sendRequest(new ListsPageRequest(Command.showTheirPage));
    }

    @Override
    public void update(PageData pageData) {
        ListsPageData data = (ListsPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        setComponentsVisibility(data.isListSelected(), data.isThereUserInList());
        listTypeLabel.setText(data.getListTypeText());
        usernameLabel.setText(data.getNameText());
        profilePicture.setImage(convertImage(data.getProfilePicture()));
    }

    private void setComponentsVisibility(boolean isListSelected, boolean isThereUserInList){
        followingsButton.setVisible(!isListSelected);
        followersButton.setVisible(!isListSelected);
        blackListButton.setVisible(!isListSelected);
        muteListButton.setVisible(!isListSelected);
        noListSelectedTitleLabel.setVisible(!isListSelected);
        listTypeTitleLabel.setVisible(isListSelected);
        listTypeLabel.setVisible(isListSelected);
        profilePicture.setVisible(isListSelected && isThereUserInList);
        usernameLabel.setVisible(isListSelected);
        rectangle.setVisible(isListSelected);
//        nextButton.setVisible(isListSelected && isThereUserInList);
//        previousButton.setVisible(isListSelected && isThereUserInList);
//        removeButton.setVisible(isListSelected && isThereUserInList);
//        showTheirPageButton.setVisible(isListSelected && isThereUserInList);
        nextButton.setVisible(isListSelected);
        previousButton.setVisible(isListSelected);
        removeButton.setVisible(isListSelected);
        showTheirPageButton.setVisible(isListSelected);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.lists);
        passSelf();
    }
}
