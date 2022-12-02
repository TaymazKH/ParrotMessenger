package client.offline.fx_controller;

import client.fx_controller.PageController;
import client.offline.event.EventType;
import client.offline.event.InitializeConnectionEvent;
import client.offline.event.MainMenuPageEvent;
import client.page_data.PageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuPageController extends PageController {
    @FXML
    void retry(ActionEvent event) {
        runEvent(new InitializeConnectionEvent());
    }

    @FXML
    void gotoChatListPage(ActionEvent event) {
        runEvent(new MainMenuPageEvent(EventType.gotoChatListPage));
    }

    @FXML
    void gotoGroupsPage(ActionEvent event) {
        runEvent(new MainMenuPageEvent(EventType.gotoGroupsPage));
    }

    @FXML
    void gotoPersonalPage(ActionEvent event) {
        runEvent(new MainMenuPageEvent(EventType.gotoPersonalPage));
    }

    @Override
    public void update(PageData pageData) {
        // nothing to show
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.mainMenu);
        passSelf();
    }
}
