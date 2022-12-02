package client.fx_controller;

import client.page_data.PageData;
import client.page_data.TimeLinePageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import client.util.Dialogs;
import shared.request.TimeLinePageRequest;
import shared.util.Command;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class TimeLinePageController extends PageController {
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Button refreshButton;
    @FXML
    private ImageView tweetImage;
    @FXML
    private ImageView sendersImage;
    @FXML
    private TextField tweetText;
    @FXML
    private Label tweetLabel;
    @FXML
    private Button addCommentButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button commentsButton;
    @FXML
    private Button upperTweetButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button forwardToUserButton;
    @FXML
    private Button likeButton;
    @FXML
    private Button dislikeButton;
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
    @FXML
    private Button repostButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button reportButton;

    ////////////////////////////////////////////////////////////
    @FXML
    void addComment(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.addComment,new String[]{tweetText.getText()},selectImage()));
    }

    @FXML
    void back(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.back));
    }

    @FXML
    void delete(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.delete));
    }

    @FXML
    void dislike(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.dislike));
    }

    @FXML
    void edit(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.edit,new String[]{tweetText.getText()}));
    }

    @FXML
    void forwardToUser(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.forwardToUser,new String[]{showInputDialog("EnterUsername")}));
//        sendRequest(new TimeLinePageRequest(Command.forwardToUser,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))}));
    }

    @FXML
    void home(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.home));
    }

    @FXML
    void like(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.like));
    }

    @FXML
    void refresh(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.refresh));
    }

    @FXML
    void report(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.report));
    }

    @FXML
    void repost(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.repost));
    }

    @FXML
    void save(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.save));
    }

    @FXML
    void showComments(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.showComments));
    }

    @FXML
    void showNext(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.showNext));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.showPrevious));
    }

    @FXML
    void showSender(MouseEvent event) {
        sendRequest(new TimeLinePageRequest(Command.showSender));
    }

    @FXML
    void showUpperTweet(ActionEvent event) {
        sendRequest(new TimeLinePageRequest(Command.showUpperTweet));
    }

    @Override
    public void update(PageData pageData) {
        TimeLinePageData data = (TimeLinePageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
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
        setPageDataType(PageDataType.timeLine);
        passSelf();
    }
}
