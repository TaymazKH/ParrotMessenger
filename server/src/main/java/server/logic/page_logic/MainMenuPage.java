package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.logic.ClientHandler;
import shared.request.MainMenuPageRequest;
import shared.response.MainMenuPageResponse;
import shared.response.Response;
import shared.util.PageType;

import java.util.Arrays;

public class MainMenuPage extends Page{
    private static final Logger logger = LogManager.getLogger(MainMenuPage.class);
    private int authToken;

    public MainMenuPage(ClientHandler clientHandler){
        super(PageType.mainMenu, null, clientHandler);
        authToken=-1;
        logger.info("entered main menu page");
    }
    public MainMenuPage(int authToken, ClientHandler clientHandler) {
        this(clientHandler);
        this.authToken = authToken;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(MainMenuPageRequest request){
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+ Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        switch(request.getCommand()){
            case gotoPersonalPage->{ return new PersonalPage(getCurrentUser(),this, getClientHandler()); }
            case gotoTimeLinePage->{ return new TimeLinePage(this, getClientHandler()); }
            case gotoExploringPage->{ return new ExploringPage(this, getClientHandler()); }
            case gotoChatListPage->{ return new ChatListPage(this, getClientHandler()); }
            case gotoGroupsPage->{ return new GroupsPage(this, getClientHandler()); }
            case gotoSettingsPage->{ return new SettingsPage(this, getClientHandler()); }
            case exit->{ return null; }
        }
        return this;
    }

    @Override
    public Page update() {
        return this;
    }

    @Override
    public Response getData(){
        if(authToken!=-1){
            int token = authToken;
            authToken=-1;
            return new MainMenuPageResponse(token);
        }
        return new MainMenuPageResponse(-1);
    }
}
