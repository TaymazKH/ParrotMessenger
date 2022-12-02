package server.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.page_logic.*;
import server.network.SocketResponseSender;
import shared.request.*;
import server.model.*;
import server.logic.model_logic.UserLogic;

import java.util.*;

public class ClientHandler extends Thread implements RequestHandler {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private volatile DataHolder dataHolder;
    private final SocketResponseSender responseSender;
    private int authToken=-1;
    private boolean returnOfflineDB = false;
    private Page currentPage;

    public ClientHandler(SocketResponseSender responseSender, DataHolder dataHolder) {
        this.dataHolder = dataHolder;
        this.responseSender = responseSender;
        currentPage = new LoginPage(this);
    }

    @Override
    public void run() {
        logger.info("Started working");
        try{
            Request request;
            while(true){
                request = responseSender.getRequest();
                if(authToken==-1 || request.getAuthToken()==authToken){
                    request.run(this);
                    if(returnOfflineDB){
                        returnOfflineDB = false;
                        logger.info("returning offline database of user: "+getCurrentID());
                        responseSender.sendResponse(currentPage.getOfflineDataBase());
                    }
                    else{
                        logger.info("updated page of user: "+getCurrentID());
                        responseSender.sendResponse(currentPage.getData());
                    }
                }
                else responseSender.sendResponse(null);
            }
        } catch(NoSuchElementException|IllegalStateException ignored){
        } catch(Exception e){
            e.printStackTrace();
        }
        logger.info("end of process");
        close();
    }
    private void close(){
        responseSender.close();
        if(authToken!=-1){
            synchronized(DataBase.getSyncObj()){
                User currentUser = getCurrentUser();
                UserLogic.logInOut(currentUser,false);
                dataHolder.getDataBase().updateUser(currentUser);
            }
            synchronized(getDataHolder().getOnlineUsers()){
                getDataHolder().getOnlineUsers().remove(authToken);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public DataHolder getDataHolder() {
        return dataHolder;
    }
    public long getCurrentID() {
        if(authToken==-1) return -1;
        synchronized(dataHolder.getOnlineUsers()){
            return dataHolder.getOnlineUsers().get(authToken);
        }
    }
    public User getCurrentUser() {
        return dataHolder.getDataBase().getUser(getCurrentID());
    }
    public int getAuthToken() {
        return authToken;
    }

    public void setAuthToken(int authToken) {
        this.authToken = authToken;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handleChatListPageRequest(ChatListPageRequest request) {
        currentPage = ((ChatListPage) currentPage).runCommand(request);
    }
    @Override
    public void handleExploringPageRequest(ExploringPageRequest request) {
        currentPage = ((ExploringPage) currentPage).runCommand(request);
    }
    @Override
    public void handleGroupsPageRequest(GroupsPageRequest request) {
        currentPage = ((GroupsPage) currentPage).runCommand(request);
    }
    @Override
    public void handleListsPageRequest(ListsPageRequest request) {
        currentPage = ((ListsPage) currentPage).runCommand(request);
    }
    @Override
    public void handleLoginPageRequest(LoginPageRequest request) {
        currentPage = ((LoginPage) currentPage).runCommand(request);
    }
    @Override
    public void handleMainMenuPageRequest(MainMenuPageRequest request) {
        currentPage = ((MainMenuPage) currentPage).runCommand(request);
    }
    @Override
    public void handleMessagingPageRequest(MessagingPageRequest request) {
        currentPage = ((MessagingPage) currentPage).runCommand(request);
    }
    @Override
    public void handleNotificationsPageRequest(NotificationsPageRequest request) {
        currentPage = ((NotificationsPage) currentPage).runCommand(request);
    }
    @Override
    public void handlePersonalPageRequest(PersonalPageRequest request) {
        currentPage = ((PersonalPage) currentPage).runCommand(request);
    }
    @Override
    public void handleRequestsPageRequest(RequestsPageRequest request) {
        currentPage = ((RequestsPage) currentPage).runCommand(request);
    }
    @Override
    public void handleSettingsPageRequest(SettingsPageRequest request) {
        currentPage = ((SettingsPage) currentPage).runCommand(request);
    }
    @Override
    public void handleTimeLinePageRequest(TimeLinePageRequest request) {
        currentPage = ((TimeLinePage) currentPage).runCommand(request);
    }
    @Override
    public void handleTweetsPageRequest(TweetsPageRequest request) {
        currentPage = ((TweetsPage) currentPage).runCommand(request);
    }
    @Override
    public void handleUpdatePageRequest() {
        currentPage = currentPage.update();
    }
    @Override
    public void handleGetOfflineDataBaseRequest() {
        returnOfflineDB = true;
    }
}
