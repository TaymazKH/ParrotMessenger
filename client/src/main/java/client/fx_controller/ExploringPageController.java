package client.fx_controller;

import client.page_data.ExploringPageData;
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
import shared.request.ExploringPageRequest;
import client.util.Dialogs;
import shared.util.Command;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ExploringPageController extends PageController {
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
    private Button searchButton;
    @FXML
    private TextField searchText;
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

    //////////////////////////////////////////////////////////////
    @FXML
    void addComment(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.addComment,new String[]{tweetText.getText()},selectImage()));
    }

    @FXML
    void back(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.back));
    }

    @FXML
    void delete(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.delete));
    }

    @FXML
    void dislike(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.dislike));
    }

    @FXML
    void edit(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.edit,new String[]{tweetText.getText()}));
    }

    @FXML
    void forwardToUser(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.forwardToUser,new String[]{showInputDialog("EnterUsername")}));
//        sendRequest(new ExploringPageRequest(Command.forwardToUser,new String[]{JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))}));
    }

    @FXML
    void home(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.home));
    }

    @FXML
    void like(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.like));
    }

    @FXML
    void refresh(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.refresh));
    }

    @FXML
    void report(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.report));
    }

    @FXML
    void repost(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.repost));
    }

    @FXML
    void save(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.save));
    }

    @FXML
    void search(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.search,new String[]{searchText.getText()}));
    }

    @FXML
    void showComments(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.showComments));
    }

    @FXML
    void showNext(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.showNext));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.showPrevious));
    }

    @FXML
    void showSender(MouseEvent event) {
        sendRequest(new ExploringPageRequest(Command.showSender));
    }

    @FXML
    void showUpperTweet(ActionEvent event) {
        sendRequest(new ExploringPageRequest(Command.showUpperTweet));
    }

    @Override
    public void update(PageData pageData) {
        ExploringPageData data = (ExploringPageData) pageData;
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
        setPageDataType(PageDataType.exploring);
        passSelf();
    }
}
