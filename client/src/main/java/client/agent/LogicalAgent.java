package client.agent;

import client.network.SocketRequestSender;
import client.offline.database.DataBase;
import client.offline.event.*;
import client.offline.logic.*;
import client.page_data.*;
import client.util.Loop;
import javafx.stage.Stage;
import shared.request.Request;
import shared.response.*;
import shared.simple_model.SimpleUser;

import java.io.IOException;

public class LogicalAgent implements ResponseHandler, EventHandler {
    private final GraphicalAgent graphicalAgent;
    private SocketRequestSender socketRequestSender;
    private final Loop loop;
    private int authToken=-1;
    private final Object syncObj1 = new Object(), syncObj2 = new Object();
    private volatile boolean userHasRequest = false, loopHasRequest = false, online = false;
    private final DataBase dataBase = new DataBase();
    private Page currentOfflinePage;
    private PageData pageData;

    public LogicalAgent(Stage stage){
        stage.setOnCloseRequest(event -> handleExitEvent());
        graphicalAgent = new GraphicalAgent(this,stage);
        loop = new Loop(this);
        loop.start();
        handleInitializeConnectionEvent();
        graphicalAgent.initialize();
        updateGraphics();
    }

    public void sendRequest(Request request){
        synchronized(getSyncObj1()){
            userHasRequest = true;
        }
        synchronized(getSyncObj2()){
            if(loopHasRequest){
                try {
                    getSyncObj2().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        request.setAuthToken(authToken);
        try {
            Response response;
            response = socketRequestSender.sendRequest(request);
            response.run(this);
        } catch (Exception e) {
            socketRequestSender = null;
            online = false;
            pageData = new ConnectionPageData();
        }
        updateGraphics();
        synchronized(getSyncObj1()){
            userHasRequest = false;
            getSyncObj1().notifyAll();
        }
    }
    public void loopSendRequest(Request request){
        synchronized(getSyncObj1()){
            if(userHasRequest){
                try {
                    getSyncObj1().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        synchronized(getSyncObj2()){
            loopHasRequest = true;
        }
        request.setAuthToken(authToken);
        try {
            Response response;
            response = socketRequestSender.sendRequest(request);
            if(loop.isActive()) response.run(this);
        } catch (Exception e) {
            loop.deactivate();
            socketRequestSender = null;
            online = false;
            pageData = new ConnectionPageData();
        }
        updateGraphics();
        synchronized(getSyncObj2()){
            loopHasRequest = false;
            getSyncObj2().notifyAll();
        }
    }
    public void runEvent(Event event){
        event.run(this);
        updateGraphics();
    }
    private void updateGraphics(){
        synchronized(graphicalAgent){
            graphicalAgent.updateGraphics(pageData,online);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    public Object getSyncObj1() {
        return syncObj1;
    }
    public Object getSyncObj2() {
        return syncObj2;
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handleChatListPageResponse(ChatListPageResponse response) {
        pageData = new ChatListPageData(response);
    }
    @Override
    public void handleExploringPageResponse(ExploringPageResponse response) {
        pageData = new ExploringPageData(response);
    }
    @Override
    public void handleGroupsPageResponse(GroupsPageResponse response) {
        pageData = new GroupsPageData(response);
    }
    @Override
    public void handleListsPageResponse(ListsPageResponse response) {
        pageData = new ListsPageData(response);
    }
    @Override
    public void handleLoginPageResponse(LoginPageResponse response) {
        pageData = new LoginPageData(response);
    }
    @Override
    public void handleMainMenuPageResponse(MainMenuPageResponse response) {
        if(response.getAuthToken()!=-1) authToken = response.getAuthToken();
        pageData = new MainMenuPageData();
    }
    @Override
    public void handleMessagingPageResponse(MessagingPageResponse response) {
        pageData = new MessagingPageData(response);
    }
    @Override
    public void handleNotificationsPageResponse(NotificationsPageResponse response) {
        pageData = new NotificationsPageData(response);
    }
    @Override
    public void handlePersonalPageResponse(PersonalPageResponse response) {
        pageData = new PersonalPageData(response);
    }
    @Override
    public void handleRequestsPageResponse(RequestsPageResponse response) {
        pageData = new RequestsPageData(response);
    }
    @Override
    public void handleSettingsPageResponse(SettingsPageResponse response) {
        pageData = new SettingsPageData(response);
    }
    @Override
    public void handleTimeLinePageResponse(TimeLinePageResponse response) {
        pageData = new TimeLinePageData(response);
    }
    @Override
    public void handleTweetsPageResponse(TweetsPageResponse response) {
        pageData = new TweetsPageData(response);
    }
    @Override
    public void handleOfflineDataBaseResponse(OfflineDataBaseResponse response) {
        if(response.getSimpleUser()!=null) dataBase.saveUser(response.getSimpleUser());
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handleChatListPageEvent(ChatListPageEvent event) {
        currentOfflinePage = ((ChatListPage) currentOfflinePage).runEvent(event);
        pageData = currentOfflinePage.getData();
    }
    @Override
    public void handleGroupsPageEvent(GroupsPageEvent event) {
        currentOfflinePage = ((GroupsPage) currentOfflinePage).runEvent(event);
        pageData = currentOfflinePage.getData();
    }
    @Override
    public void handleMainMenuPageEvent(MainMenuPageEvent event) {
        currentOfflinePage = ((MainMenuPage) currentOfflinePage).runEvent(event);
        pageData = currentOfflinePage.getData();
    }
    @Override
    public void handleMessagingPageEvent(MessagingPageEvent event) {
        currentOfflinePage = ((MessagingPage) currentOfflinePage).runEvent(event);
        pageData = currentOfflinePage.getData();
    }
    @Override
    public void handlePersonalPageEvent(PersonalPageEvent event) {
        currentOfflinePage = ((PersonalPage) currentOfflinePage).runEvent(event);
        pageData = currentOfflinePage.getData();
    }
    @Override
    public void handleTweetsPageEvent(TweetsPageEvent event) {
        currentOfflinePage = ((TweetsPage) currentOfflinePage).runEvent(event);
        pageData = currentOfflinePage.getData();
    }
    @Override
    public void handleInitializeConnectionEvent() {
        try{
            socketRequestSender = new SocketRequestSender();
            online = true;
            pageData = new LoginPageData();
            loop.activate();
        } catch(IOException e){
            socketRequestSender = null;
            online = false;
            pageData = new ConnectionPageData();
        }
    }
    @Override
    public void handleRunOfflineEvent() {
        SimpleUser user = dataBase.loadUser();
        if(user==null) pageData = new ConnectionPageData("OfflineDBNotFound");
        else{
            Page.setCurrentUser(user);
            currentOfflinePage = new MainMenuPage();
            pageData = currentOfflinePage.getData();
        }
    }
    @Override
    public void handleExitEvent() {
        loop.stopLater();
        if(socketRequestSender!=null) socketRequestSender.close();
        pageData = null;
    }
}
