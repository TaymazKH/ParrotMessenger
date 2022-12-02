package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.ClientHandler;
import server.model.User;
import server.logic.model_logic.UserLogic;
import shared.request.SettingsPageRequest;
import shared.response.Response;
import shared.response.SettingsPageResponse;
import shared.util.PageType;

import java.util.Arrays;

public class SettingsPage extends Page{
    private static final Logger logger = LogManager.getLogger(SettingsPage.class);

    public SettingsPage(Page previousPage, ClientHandler clientHandler) {
        super(PageType.settings, previousPage, clientHandler);
        logger.info("entered the page");
    }

    private void togglePrivacy(){
        User currentUser;
        synchronized(DataBase.getSyncObj()){
            currentUser = getCurrentUser();
            currentUser.setPublic(!currentUser.isPublic());
            updateUser(currentUser);
        }
        logger.info("toggled account privacy. is public: "+currentUser.isPublic());
    }
    private void toggleInfoVisibility(){
        User currentUser;
        synchronized(DataBase.getSyncObj()){
            currentUser = getCurrentUser();
            if(currentUser.getInfoVisibility()==2) currentUser.setInfoVisibility(0);
            else currentUser.setInfoVisibility(currentUser.getInfoVisibility()+1);
            updateUser(currentUser);
        }
        logger.info("toggled info visibility. new access level: "+currentUser.getInfoVisibility());
    }
    private void changeUserName(String text){
        if(text==null) setDialog("MustProvideName");
        else{
            if(!usernameValidation(text)) setDialog("InvalidUsername");
            else{
                if(getUsernames().contains(text)) setDialog("UsernameTaken");
                else{
                    User currentUser;
                    synchronized(DataBase.getSyncObj()){
                        currentUser = getCurrentUser();
                        currentUser.setUserName(text);
                        updateUser(currentUser);
                        updateIDUsernameMapFiles(currentUser);
                    }
                    logger.info("changed username of user "+currentUser.getId()+" to: "+text);
                }
            }
        }
    }
    private void changePassword(String[] text){
        if(text[0]==null || !getCurrentUser().getPassword().equals(text[0]))
            setDialog("WrongPassword");
        else if(text[1]==null || text[1].isBlank())
            setDialog("MustProvideText");
        else{
            User currentUser;
            synchronized(DataBase.getSyncObj()){
                currentUser = getCurrentUser();
                currentUser.setPassword(text[1]);
                updateUser(currentUser);
            }
            logger.info("changed password of user "+currentUser.getId()+" to: "+text[1]);
        }
    }
    private Page logout(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser();
            UserLogic.logInOut(currentUser,false);
            updateUser(currentUser);
        }
        synchronized(getDataHolder().getOnlineUsers()){
            getDataHolder().getOnlineUsers().remove(getClientHandler().getAuthToken());
            getClientHandler().setAuthToken(-1);
        }
        return new LoginPage(getClientHandler());
    }
    private Page deactivateAccount(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser();
            currentUser.setActive(false);
            UserLogic.logInOut(currentUser,false);
            updateUser(currentUser);
        }
        logger.info("deactivated the account");
        return null;
    }
    private Page delete(String text){
        if(getCurrentUser().getPassword().equals(text)){
            deleteAccount();
            return new LoginPage(getClientHandler());
        }
        else{
            setDialog("WrongPassword");
            logger.info("didn't start deleting process: wrong password");
            return this;
        }
    }

    private boolean usernameValidation(String username){
        for(int i=0;i<username.length();i++){
            if(!Character.isDigit(username.charAt(i))) return true;
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(SettingsPageRequest request){
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+ Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        switch(request.getCommand()){
            case home->{ return new MainMenuPage(getClientHandler()); }
            case back->{ return getPreviousPage(); }
            case togglePrivacy-> togglePrivacy();
            case toggleInfoVisibility-> toggleInfoVisibility();
            case changeUsername-> changeUserName(request.getStringArgs()[0]);
            case changePassword-> changePassword(request.getStringArgs());
            case logOut->{ return logout(); }
            case deactivateAccount->{ return deactivateAccount(); }
            case deleteAccount->{ return delete(request.getStringArgs()[0]); }
        }
        return this;
    }

    @Override
    public Page update() {
        return this;
    }

    @Override
    public Response getData() {
        String dialog,usernameText,passwordText;
        boolean privacy;
        int infoVisibility;

        dialog = getDialog();
        clearDialog();
        User currentUser = getCurrentUser();
        privacy = currentUser.isPublic();
        infoVisibility = currentUser.getInfoVisibility();
        usernameText = currentUser.getUserName();
        passwordText = "*".repeat(currentUser.getPassword().length());
        return new SettingsPageResponse(dialog,usernameText,passwordText,privacy,infoVisibility);
    }
}
