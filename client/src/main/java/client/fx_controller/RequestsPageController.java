package client.fx_controller;

import client.page_data.PageData;
import client.page_data.RequestsPageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import client.util.Dialogs;
import javafx.scene.shape.Rectangle;
import shared.request.RequestsPageRequest;
import shared.util.Command;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestsPageController extends PageController {
    @FXML
    private Button showTheirPageButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Label requestLabel;
    @FXML
    private ImageView profilePicture;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button acceptButton;
    @FXML
    private Button incomingButton;
    @FXML
    private Button outgoingButton;
    @FXML
    private Label listTypeLabel;
    @FXML
    private Label listTypeTitleLabel;
    @FXML
    private Label noListSelectedTitleLabel;
    @FXML
    private Button declineButton;
    @FXML
    private Button silentDeclineButton;
    @FXML
    private Button cancelRequestButton;
    @FXML
    private Rectangle rectangle;

    ////////////////////////////////////////////////////////////
    @FXML
    void accept(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.accept));
    }

    @FXML
    void back(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.back));
    }

    @FXML
    void cancelRequest(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.cancelRequest));
    }

    @FXML
    void decline(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.decline));
    }

    @FXML
    void home(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.home));
    }

    @FXML
    void showIncomingRequests(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.showIncomingRequests));
    }

    @FXML
    void showNext(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.showNext));
    }

    @FXML
    void showOutgoingRequests(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.showOutgoingRequests));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.showPrevious));
    }

    @FXML
    void showTheirPage(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.showTheirPage));
    }

    @FXML
    void silentDecline(ActionEvent event) {
        sendRequest(new RequestsPageRequest(Command.silentDecline));
    }

    @Override
    public void update(PageData pageData) {
        RequestsPageData data = (RequestsPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        setComponentsVisibility(data.getListType(), data.isThereItemInList());
        listTypeLabel.setText(data.getListTypeText());
        requestLabel.setText(data.getRequestText());
        profilePicture.setImage(convertImage(data.getProfileImage()));
    }

    private void setComponentsVisibility(int listType, boolean isThereItemInList){
        incomingButton.setVisible(listType==-1);
        outgoingButton.setVisible(listType==-1);
        noListSelectedTitleLabel.setVisible(listType==-1);

        listTypeTitleLabel.setVisible(listType>-1);
        listTypeLabel.setVisible(listType>-1);
        profilePicture.setVisible(listType>-1 && isThereItemInList);
        requestLabel.setVisible(listType>-1);
        rectangle.setVisible(listType>-1);
        nextButton.setVisible(listType>-1);
        previousButton.setVisible(listType>-1);
        showTheirPageButton.setVisible(listType>-1);

        acceptButton.setVisible(listType==0 && isThereItemInList);
        declineButton.setVisible(listType==0 && isThereItemInList);
        silentDeclineButton.setVisible(listType==0 && isThereItemInList);
        cancelRequestButton.setVisible(listType==1 && isThereItemInList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.requests);
        passSelf();
    }
}
