package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.ClientHandler;
import server.model.User;
import shared.request.NotificationsPageRequest;
import shared.response.NotificationsPageResponse;
import shared.response.Response;
import shared.util.PageType;

import java.util.Arrays;

public class NotificationsPage extends Page {
    private static final Logger logger = LogManager.getLogger(NotificationsPage.class);

    public NotificationsPage(Page previousPage, ClientHandler clientHandler) {
        super(PageType.notifications, previousPage, clientHandler);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(NotificationsPageRequest request){
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+ Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        switch(request.getCommand()){
            case home->{ return new MainMenuPage(getClientHandler()); }
            case back->{ return getPreviousPage(); }
            case showNextNotification->{
                synchronized(DataBase.getSyncObj()){
                    User currentUser = getCurrentUser();
                    if(!currentUser.getNotifications().isEmpty()){
                        currentUser.getNotifications().removeFirst();
                        updateUser(currentUser);
                    }
                }
            }
        }
        return this;
    }

    @Override
    public Page update() {
        return this;
    }

    @Override
    public Response getData() {
        String dialog,username=null;
        long[] notification=null;
        byte[] profileImage=null;

        dialog = getDialog();
        clearDialog();
        User currentUser = getCurrentUser();
        if(!currentUser.getNotifications().isEmpty()){
            notification = currentUser.getNotifications().getFirst();
            username = getUsername(notification[0]);
            profileImage = getImage(getUser(notification[0]).getPictureID());
        }
        return new NotificationsPageResponse(dialog,username,notification,profileImage);
    }
}
