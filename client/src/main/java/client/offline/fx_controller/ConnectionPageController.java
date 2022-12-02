package client.offline.fx_controller;

import client.fx_controller.PageController;
import client.offline.event.InitializeConnectionEvent;
import client.offline.event.RunOfflineEvent;
import client.page_data.ConnectionPageData;
import client.page_data.PageData;
import client.util.Dialogs;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionPageController extends PageController {
    @FXML
    void runOffline(ActionEvent event) {
        runEvent(new RunOfflineEvent());
    }

    @FXML
    void retry(ActionEvent event) {
        runEvent(new InitializeConnectionEvent());
    }

    @Override
    public void update(PageData pageData) {
        ConnectionPageData data = (ConnectionPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION, Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.connection);
        passSelf();
    }
}
