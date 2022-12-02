package client.offline.fx_controller;

import client.fx_controller.PageController;
import client.offline.event.EventType;
import client.offline.event.TweetsPageEvent;
import client.page_data.PageData;
import client.page_data.TweetsPageData;
import client.util.Dialogs;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TweetsPageController extends PageController {
    @FXML
    private ImageView tweetImage;
    @FXML
    private ImageView sendersImage;
    @FXML
    private Label tweetLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label postedTimeLabel;
    @FXML
    private Label repostedOrEditedLabel;
    @FXML
    private Label likeLabel;
    @FXML
    private Label dislikeLabel;

    //////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        runEvent(new TweetsPageEvent(EventType.back));
    }

    @FXML
    void home(ActionEvent event) {
        runEvent(new TweetsPageEvent(EventType.home));
    }

    @FXML
    void showNext(ActionEvent event) {
        runEvent(new TweetsPageEvent(EventType.showNext));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        runEvent(new TweetsPageEvent(EventType.showPrevious));
    }

    @Override
    public void update(PageData pageData) {
        TweetsPageData data = (TweetsPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION, Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        tweetLabel.setText(data.getTweetText());
        nameLabel.setText(data.getNameText());
        postedTimeLabel.setText(data.getPostedTime());
        repostedOrEditedLabel.setText(data.getRepostedOrEdited());
        likeLabel.setText(data.getLikeCount());
        dislikeLabel.setText(data.getDislikeCount());
        tweetImage.setImage(convertImage(data.getPostImage()));
        sendersImage.setImage(convertImage(data.getProfilePicture()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.tweets);
        passSelf();
    }
}
