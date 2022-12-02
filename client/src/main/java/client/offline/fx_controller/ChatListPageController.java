package client.offline.fx_controller;

import client.fx_controller.PageController;
import client.offline.event.ChatListPageEvent;
import client.offline.event.EventType;
import client.page_data.ChatListPageData;
import client.page_data.PageData;
import client.util.Dialogs;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatListPageController extends PageController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label sectionTitleLabel;

    ///////////////////////////////////////////////////////////////////////
    @FXML
    void back(ActionEvent event) {
        runEvent(new ChatListPageEvent(EventType.back));
    }

    @FXML
    void home(ActionEvent event) {
        runEvent(new ChatListPageEvent(EventType.home));
    }

    @FXML
    void messageUser(ActionEvent event) {
        runEvent(new ChatListPageEvent(EventType.messageUser,showInputDialog("EnterUsername")));
//        runEvent(new ChatListPageEvent(EventType.messageUser,JOptionPane.showInputDialog(Dialogs.get("EnterUsername"))));
    }

    @FXML
    void showNext(ActionEvent event) {
        runEvent(new ChatListPageEvent(EventType.showNext));
    }

    @FXML
    void showPrevious(ActionEvent event) {
        runEvent(new ChatListPageEvent(EventType.showPrevious));
    }

    @FXML
    void showSavedMessages(ActionEvent event) {
        runEvent(new ChatListPageEvent(EventType.showSavesMessages));
    }

    @Override
    public void update(PageData pageData) {
        ChatListPageData data = (ChatListPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
        sectionTitleLabel.setText(data.getSectionTitle());
        nameLabel.setText(data.getNameText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.chatList);
        passSelf();
    }
}
