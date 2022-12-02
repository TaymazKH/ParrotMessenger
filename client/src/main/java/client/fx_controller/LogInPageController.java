package client.fx_controller;

import client.page_data.LoginPageData;
import client.page_data.PageData;
import client.util.PageDataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import shared.request.LoginPageRequest;
import client.util.Dialogs;
import shared.util.Command;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInPageController extends PageController {
    @FXML
    private TextField logInUsernameText;
    @FXML
    private TextField logInPasswordText;
    @FXML
    private Button logInButton;
    @FXML
    private TextField firstNameText;
    @FXML
    private TextField lastNameText;
    @FXML
    private TextField signUpUsernameText;
    @FXML
    private TextField signUpPasswordText;
    @FXML
    private TextField emailText;
    @FXML
    private Button signUpButton;

    /////////////////////////////////////////////////////////////////////

    @FXML
    void login(ActionEvent event) {
        sendRequest(new LoginPageRequest(Command.login, new String[]{
                logInUsernameText.getText(),
                logInPasswordText.getText()
        }));
    }

    @FXML
    void signup(ActionEvent event) {
        sendRequest(new LoginPageRequest(Command.signup, new String[]{
                signUpUsernameText.getText(),
                signUpPasswordText.getText(),
                firstNameText.getText(),
                lastNameText.getText(),
                emailText.getText()
        }));
    }

    @Override
    public void update(PageData pageData) {
        LoginPageData data = (LoginPageData) pageData;
        if(data.getDialog()!=null && !data.getDialog().isBlank())
            new Alert(Alert.AlertType.INFORMATION,Dialogs.get(data.getDialog())){{ setHeaderText(Dialogs.get("NoticeTitle")); show(); }};
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPageDataType(PageDataType.login);
        passSelf();
    }
}
