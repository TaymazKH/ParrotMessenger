package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.ClientHandler;
import server.model.Message;
import shared.request.LoginPageRequest;
import server.model.User;
import server.logic.model_logic.UserLogic;
import shared.response.LoginPageResponse;
import shared.response.Response;
import shared.util.PageType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public class LoginPage extends Page{
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private String firstName = "", lastName = "", signupUsername = "", signupPassword = "", email = "", loginUsername = "", loginPassword = "";

    public LoginPage(ClientHandler clientHandler){
        super(PageType.login, null, clientHandler);
    }

    private Page login(String[] args){
        loginUsername = args[0];
        loginPassword = args[1];
        synchronized(DataBase.getSyncObj()){
            if(loginUsername.isBlank() || loginPassword.isBlank()) setDialog("FillTheFields");
            else if(getUser(loginUsername)==null) setDialog("NoAccountWithThisUsername");
            else if(!getUser(loginUsername).getPassword().equals(loginPassword)) setDialog("WrongPassword");
            else{
                User user; Page page;
                synchronized(DataBase.getSyncObj()){
                    user = getUser(loginUsername);
                }
                boolean alreadyLoggedIn;
                synchronized(getDataHolder().getOnlineUsers()){
                    alreadyLoggedIn = getDataHolder().getOnlineUsers().containsValue(user.getId());
                }
                if(alreadyLoggedIn) setDialog("AlreadyLoggedIn");
                else{
                    synchronized(DataBase.getSyncObj()){
                        page = successfulLogin(user);
                        updateUser(user);
                    }
                    logger.info("login complete with user: "+user.getId());
                    return page;
                }
            }
            return this;
        }
    }
    private Page signup(String[] args){
        signupUsername = args[0];
        signupPassword = args[1];
        firstName = args[2];
        lastName = args[3];
        email = args[4];
        synchronized(DataBase.getSyncObj()){
            if(signupUsername.isBlank() || signupPassword.isBlank() || firstName.isBlank() || lastName.isBlank() || email.isBlank()) setDialog("FillTheFields");
            else if(!usernameValidation(signupUsername)) setDialog("InvalidUsername");
            else if(getUsernames().contains(signupUsername)) setDialog("UsernameTaken");
            else if(!email.contains("@")) setDialog("InvalidEmail");
            else if(getEmailMap().containsValue(email)) setDialog("EmailTaken");
            else{
                User newUser; Page page;
                synchronized(DataBase.getSyncObj()){
                    newUser = new User(firstName,lastName,signupUsername,signupPassword,generateNewUserID(),email);
                    updateIDUsernameMapFiles(newUser);
                    updateEmailMapFile(newUser);
                    updatePhoneNumberMapFile(newUser);
                    page = successfulLogin(newUser);
                    saveNewUser(newUser);
                }
                logger.info("signup complete with user: "+newUser.getId());
                return page;
            }
            return this;
        }
    }
    private Page successfulLogin(User user){
        UserLogic.logInOut(user,true);
        for(Map.Entry<Long,LinkedList<Long>> m: user.getMessages().entrySet()){
            for(Long messageID: m.getValue()){
                Message message = getMessage(user.getId(),m.getKey(),messageID);
                if(message.getSeen()!=4){
                    message.setSeen(3);
                    updateMessage(message);
                }
            }
        }
        int authToken;
        synchronized(getDataHolder().getRandom()){
            authToken = getDataHolder().getRandom().nextInt(Integer.MAX_VALUE);
        }
        synchronized(getDataHolder().getOnlineUsers()){
            getDataHolder().getOnlineUsers().put(authToken,user.getId());
        }
        getClientHandler().setAuthToken(authToken);
        return new MainMenuPage(authToken, getClientHandler());
    }
    private boolean usernameValidation(String username){
        for(int i=0;i<username.length();i++){
            if(!Character.isDigit(username.charAt(i))) return true;
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(LoginPageRequest request){
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+ Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        switch(request.getCommand()){
            case login->{ return login(request.getStringArgs()); }
            case signup->{ return signup(request.getStringArgs()); }
        }
        return this;
    }

    @Override
    public Page update() {
        return this;
    }

    @Override
    public Response getData(){
        String dialog;

        dialog = getDialog();
        clearDialog();
        return new LoginPageResponse(dialog);
    }
}
