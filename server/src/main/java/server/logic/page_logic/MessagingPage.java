package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.ClientHandler;
import shared.request.MessagingPageRequest;
import server.model.Message;
import server.model.User;
import server.logic.model_logic.MessageLogic;
import server.logic.model_logic.UserLogic;
import shared.response.MessagingPageResponse;
import shared.response.Response;
import shared.simple_model.SimpleMessage;
import shared.util.Command;
import shared.util.PageType;

import java.util.Arrays;
import java.util.LinkedList;

public class MessagingPage extends Page {
    private static final Logger logger = LogManager.getLogger(MessagingPage.class);
    private int section; // 1: saved messages / 2: pv
    private final long targetUserID;
    private long currentMessageID;

    public MessagingPage(User targetUser, Page previousPage, ClientHandler clientHandler) {
        super(PageType.messaging, previousPage, clientHandler);
        targetUserID = targetUser.getId();
        if(getCurrentUser().equals(targetUser)) showSavedMessages();
        else messageTargetUser();
    }

    public User getTargetUser() {
        return getUser(targetUserID);
    }
    public LinkedList<Long> getCurrentMessageList() {
        return getCurrentUser().getMessages().get(targetUserID);
    }
    public Message getCurrentMessage() {
        return getMessage(getCurrentUser().getId(), targetUserID, currentMessageID);
    }

    private void showSavedMessages(){
        section=1;
        try{
            synchronized(DataBase.getSyncObj()){
                User currentUser = getCurrentUser();
                if(!currentUser.getMessages().containsKey(currentUser.getId())){
                    currentUser.getMessages().put(currentUser.getId(),new LinkedList<>());
                    updateUser(currentUser);
                }
            }
            currentMessageID = getCurrentMessageList().getLast();
            logger.info("section set to: 1 = saved messages");
        } catch(Exception e){
            currentMessageID=-1;
            logger.info("section set to: 1 = saved messages | nothing to show");
        }
    }
    private void messageTargetUser(){
        section=2;
        try{
            synchronized(DataBase.getSyncObj()){
                User currentUser = getCurrentUser(), targetUser = getTargetUser();
                LinkedList<Long> currentMessageList;
                if(currentUser.getMessages().containsKey(targetUser.getId())){
                    currentMessageList = getCurrentMessageList();
                    if(!currentMessageList.isEmpty()){
                        if(getCurrentUser().getUnread().get(targetUser.getId())>0){
                            currentMessageID = currentMessageList.get(currentMessageList.size()-currentUser.getUnread().get(targetUser.getId()));
                            Message currentMessage = getCurrentMessage();
                            currentMessage.setSeen(4);
                            updateMessage(currentMessage);
                            currentUser.getUnread().put(targetUser.getId(), currentUser.getUnread().get(targetUser.getId())-1);
                            updateUser(currentUser);
                            logger.info("section set to: 2 = pv | targetUser set to: "+targetUser.getId()+" | loaded unread message(s)");
                        }
                        else{
                            currentMessageID = currentMessageList.getLast();
                            logger.info("section set to: 2 = pv | targetUser set to: "+targetUser.getId()+" | loaded last message");
                        }
                    }
                    else{
                        currentMessageID=-1;
                        logger.info("section set to: 2 = pv | targetUser set to: "+targetUser.getId()+" | nothing to show");
                    }
                }
                else{
                    currentMessageList = new LinkedList<>();
                    currentUser.getMessages().put(targetUser.getId(), currentMessageList);
                    targetUser.getMessages().put(currentUser.getId(), currentMessageList);
                    currentUser.getUnread().put(targetUser.getId(),0);
                    targetUser.getUnread().put(currentUser.getId(),0);
                    updateUser(currentUser);
                    updateUser(targetUser);
                    currentMessageID=-1;
                    logger.info("section set to: 2 = pv | targetUser set to: "+targetUserID+" | created new message lists");
                }
            }
        } catch(Exception e){
            currentMessageID=-1;
            logger.info("section set to: 2 = pv | targetUser set to: "+targetUserID+" | nothing to show");
        }
    }
    private void newMessage(String text, byte[] imageData){
        if(text==null || text.isBlank()) setDialog("MustProvideText");
        else{
            synchronized(DataBase.getSyncObj()){
                User currentUser = getCurrentUser(), targetUser = getTargetUser();
                int seen;
                synchronized(getDataHolder().getOnlineUsers()){
                    if(currentUser.equals(targetUser)) seen=4;
                    else if(getDataHolder().getOnlineUsers().containsValue(targetUser.getId())) seen=3;
                    else seen=2;
                }
                Message newMessage = new Message(text, currentUser.getId(), targetUser.getId(), generateNewMessageID(currentUser.getId(),targetUser.getId()),-1, seen, saveNewImage(imageData));
                UserLogic.addMessage(currentUser,newMessage);
//                currentUser.getUnread().replace(targetUserID,0);
//                for(Map.Entry<Long,LinkedList<Long>> m: currentUser.getMessages().entrySet()){
//                    for(Long messageID: m.getValue()){
//                        Message message = getMessage(currentUser.getId(),m.getKey(),messageID);
//                        message.setSeen(4);
//                        updateMessage(message);
//                    }
//                }
                updateUser(currentUser);
                if(section==2){
                    UserLogic.addMessage(targetUser,newMessage);
//                    targetUser.getUnread().replace(currentUser.getId(),0);
//                    for(Map.Entry<Long,LinkedList<Long>> m: targetUser.getMessages().entrySet()){
//                        for(Long messageID: m.getValue()){
//                            Message message = getMessage(targetUser.getId(),m.getKey(),messageID);
//                            message.setSeen(4);
//                            updateMessage(message);
//                        }
//                    }
                    updateUser(targetUser);
                }
                saveNewMessage(newMessage);
                if(currentMessageID==-1) currentMessageID = newMessage.getId();
            }
        }
    }
    private void showNext(){
        try{
            synchronized(DataBase.getSyncObj()){
                LinkedList<Long> currentMessageList = getCurrentMessageList();
                currentMessageID = currentMessageList.get(currentMessageList.indexOf(currentMessageID)+1);
                User currentUser = getCurrentUser(), targetUser = getTargetUser();
                Message currentMessage = getCurrentMessage();
                if(currentMessage.getReceiver()==currentUser.getId() && currentMessage.getSeen()!=4){
                    currentMessage.setSeen(4);
                    updateMessage(currentMessage);
                    currentUser.getUnread().put(targetUser.getId(), currentUser.getUnread().get(targetUser.getId())-1);
                    updateUser(currentUser);
                }   
            }
        } catch(Exception e){
            setDialog("CantShowNext");
        }
    }
    private void showPrevious(){
        try{
            LinkedList<Long> currentMessageList = getCurrentMessageList();
            currentMessageID = currentMessageList.get(currentMessageList.indexOf(currentMessageID)-1);
        } catch(Exception e){
            setDialog("CantShowPrevious");
        }
    }
    private void save(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser();
            if(!currentUser.getMessages().containsKey(currentUser.getId())){
                currentUser.getMessages().put(currentUser.getId(),new LinkedList<>());
            }
            Message currentMessage = getCurrentMessage(),
                    newMessage = new Message(currentMessage.getText(), currentUser.getId(), currentUser.getId(), generateNewMessageID(currentUser.getId(),currentUser.getId()), MessageLogic.getRepostedID(currentMessage),4, currentMessage.getPictureID());
            UserLogic.addMessage(currentUser,newMessage);
            saveNewMessage(newMessage);
            updateUser(currentUser);
        }
    }
    private void forwardToUser(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideName");
        else{
            synchronized(DataBase.getSyncObj()){
                User targetUser = getUser(text);
                if(targetUser==null){
                    setDialog("UserNotFound");
                }
                else if(!targetUser.isActive()){
                    setDialog("AccountNotActive");
                }
                else if(!targetUser.isOwned()){
                    setDialog("AccountNotOwned");
                }
                else if(!UserLogic.canMessage(getCurrentUser(),targetUser.getId())){
                    setDialog("FollowingNeededToMessage");
                }
                else{
                    User currentUser = getCurrentUser();
                    Message currentMessage = getCurrentMessage();
                    if(!currentUser.getMessages().containsKey(targetUser.getId())){
                        currentUser.getMessages().put(targetUser.getId(),new LinkedList<>());
                        targetUser.getMessages().put(currentUser.getId(),new LinkedList<>());
                        currentUser.getUnread().put(targetUser.getId(),0);
                        targetUser.getUnread().put(currentUser.getId(),0);
                    }
                    int seen;
                    synchronized(getDataHolder().getOnlineUsers()){
                        if(currentUser.equals(targetUser)) seen=4;
                        else if(getDataHolder().getOnlineUsers().containsValue(targetUser.getId())) seen=3;
                        else seen=2;
                    }
                    Message newMessage = new Message(currentMessage.getText(), currentUser.getId(), targetUser.getId(), generateNewMessageID(currentUser.getId(), targetUser.getId()), MessageLogic.getRepostedID(currentMessage), seen, currentMessage.getPictureID());
                    UserLogic.addMessage(currentUser,newMessage);
                    updateUser(currentUser);
                    UserLogic.addMessage(targetUser,newMessage);
                    updateUser(targetUser);
                    saveNewMessage(newMessage);
                    logger.info("message sent to: "+targetUser.getId());
                }   
            }
        }
    }
    private void edit(String text){
        synchronized(DataBase.getSyncObj()){
            Message currentMessage = getCurrentMessage();
            if(text==null || text.isBlank()) setDialog("MustProvideText");
            else if(currentMessage.getSender()!=getCurrentUser().getId()) setDialog("NotTheOwner");
            else if(currentMessage.getRepostedFrom()>-1) setDialog("CantEditForwarded");
            else{
                currentMessage.setText(text);
                currentMessage.setEditedTimes(currentMessage.getEditedTimes()+1);
                updateMessage(currentMessage);
            }   
        }
    }
    private void delete(){
        if(getCurrentUser().getId()!=getCurrentMessage().getSender()) setDialog("NotTheOwner");
        else{
            synchronized(DataBase.getSyncObj()){
                Message toDelete = getCurrentMessage();
                showNext();
                if(getDialog()!=null && !getDialog().isEmpty()){
                    clearDialog();
                    showPrevious();
                    if(getDialog()!=null && !getDialog().isEmpty()){
                        clearDialog();
                        currentMessageID=-1;
                    }
                }
                User currentUser = getCurrentUser(), targetUser = getTargetUser();
                currentUser.getMessages().get(targetUser.getId()).remove(toDelete.getId());
                updateUser(currentUser);
                if(section==2){
                    if(toDelete.getSeen()!=4) targetUser.getUnread().put(currentUser.getId(),targetUser.getUnread().get(currentUser.getId())-1);
                    targetUser.getMessages().get(currentUser.getId()).remove(toDelete.getId());
                    updateUser(targetUser);
                }
                toDelete.setOwned(false);
                updateMessage(toDelete);   
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(MessagingPageRequest request){
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+ Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        if(request.getCommand()==Command.home) return new MainMenuPage(getClientHandler());
        else if(request.getCommand()==Command.back) return getPreviousPage();
        else if(request.getCommand()==Command.newMessage) newMessage(request.getStringArgs()[0], request.getImageData());
        else if(currentMessageID!=-1){
            switch(request.getCommand()){
                case showNext-> showNext();
                case showPrevious-> showPrevious();
                case save-> save();
                case forwardToUser-> forwardToUser(request.getStringArgs()[0]);
                case edit-> edit(request.getStringArgs()[0]);
                case delete-> delete();
            }
        }
        return this;
    }

    @Override
    public Page update() {
        if(section==2){
            User targetUser = getTargetUser();
            if(!UserLogic.canMessage(targetUser,getCurrentID()))
                return getPreviousPage();
        }
        Message message = getCurrentMessage();
        if(message==null || !message.isOwned()){
            if(getCurrentID()==targetUserID) showSavedMessages();
            else messageTargetUser();
            clearDialog();
        }
        return this;
    }

    @Override
    public Response getData() {
        String dialog,repostedFrom=null;
        SimpleMessage simpleMessage=null;

        dialog = getDialog();
        clearDialog();
        Message message = getCurrentMessage();
        if(message!=null){
            if(message.getRepostedFrom()>-1)
                repostedFrom = getUsername(message.getRepostedFrom());
            simpleMessage = new SimpleMessage(message.getText(), getUsername(message.getSender()), repostedFrom, getImage(message.getPictureID()), getImage(getUser(message.getSender()).getPictureID()), message.getPostedTime(), message.getEditedTimes(), message.getSeen());
        }
        return new MessagingPageResponse(simpleMessage,dialog);
    }
}
