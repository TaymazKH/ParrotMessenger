package client.fx_controller;

import client.offline.event.ExitEvent;
import client.page_data.PageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import shared.request.MainMenuPageRequest;
import shared.util.Command;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuPageController extends PageController {
    @FXML
    private Button personalPageButton;
    @FXML
    private Button timeLinePageButton;
    @FXML
    private Button exploringPageButton;
    @FXML
    private Button messagingPageButton;
    @FXML
    private Button groupsPageButton;
    @FXML
    private Button settingsPageButton;
    @FXML
    private Button exitButton;

    ///////////////////////////////////////////////////////////////////////////////////
    @FXML
    void gotoExploringPage(ActionEvent event) {
        sendRequest(new MainMenuPageRequest(Command.gotoExploringPage));
    }

    @FXML
    void gotoGroupsPage(ActionEvent event) {
        sendRequest(new MainMenuPageRequest(Command.gotoGroupsPage));
    }

    @FXML
    void gotoChatListPage(ActionEvent event) {
        sendRequest(new MainMenuPageRequest(Command.gotoChatListPage));
    }

    @FXML
    void gotoPersonalPage(ActionEvent event) {
        sendRequest(new MainMenuPageRequest(Command.gotoPersonalPage));
    }

    @FXML
    void gotoSettingsPage(ActionEvent event) {
        sendRequest(new MainMenuPageRequest(Command.gotoSettingsPage));
    }

    @FXML
    void gotoTimeLinePage(ActionEvent event) {
        sendRequest(new MainMenuPageRequest(Command.gotoTimeLinePage));
    }

    @FXML
    void exit(ActionEvent event) {
        runEvent(new ExitEvent());
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
