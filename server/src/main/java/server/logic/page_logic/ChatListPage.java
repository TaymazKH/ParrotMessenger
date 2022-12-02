package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.ClientHandler;
import shared.request.ChatListPageRequest;
import server.model.Message;
import server.model.User;
import server.logic.model_logic.UserLogic;
import shared.response.ChatListPageResponse;
import shared.response.Response;
import shared.util.Command;
import shared.util.PageType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

public class ChatListPage extends Page {
    private static final Logger logger = LogManager.getLogger(ChatListPage.class);
    private int section; // 0: chat list / 1: group list / 2: group management / 3: selection mode
    private LinkedList<Long> selectionList;
    private long thirdUserID;
    private String groupName;

    public ChatListPage(Page previousPage, ClientHandler clientHandler) {
        super(PageType.chatList, previousPage, clientHandler);
        backToChatList();
        logger.info("entered the page");
    }

    public int getSection() {
        return section;
    }
    public LinkedList<Long> getCurrentList() {
        LinkedList<Long> currentList = null;
        switch(section){
            case 0->{
                User currentUser = getCurrentUser();
                currentList = new LinkedList<>();
                LinkedList<Long> usersWithoutUnread = new LinkedList<>();
                for(Map.Entry<Long,Integer> m: currentUser.getUnread().entrySet()){
                    if(m.getValue()>0) currentList.add(m.getKey());
                    else usersWithoutUnread.add(m.getKey());
                }
                currentList.addAll(usersWithoutUnread);
            }
            case 2-> currentList = getCurrentUser().getPvGroups().get(groupName);
            case 3-> currentList = selectionList;
        }
        return currentList;
    }
    public LinkedList<String> getGroupsList() {
        return new LinkedList<>(getCurrentUser().getPvGroups().keySet());
    }
    public long getThirdUserID() {
        return thirdUserID;
    }
    public String getGroupName() {
        return groupName;
    }

    private void backToChatList(){
        section=0;
        try{ thirdUserID = getCurrentList().getFirst(); }
        catch(NoSuchElementException e){ thirdUserID=-1; }
    }
    private void gotoGroupsList(){
        section=1;
        try{ groupName = getGroupsList().getFirst(); }
        catch(NoSuchElementException e){ groupName = null; }
    }
    private void gotoGroupManagement(){
        if(groupName!=null){
            section=2;
            try{ thirdUserID = getCurrentList().getFirst(); }
            catch(NoSuchElementException e){ thirdUserID=-1; }
        }
    }
    private void gotoSelectionMode(){
        section=3;
        selectionList = new LinkedList<>();
        thirdUserID=-1;
    }
    private void showNext(){
        try{
            switch(section){
                case 0,2,3->{
                    LinkedList<Long> currentList = getCurrentList();
                    thirdUserID = currentList.get(currentList.indexOf(thirdUserID)+1);
                }
                case 1->{
                    LinkedList<String> groupsList = getGroupsList();
                    groupName = groupsList.get(groupsList.indexOf(groupName)+1);
                }
            }
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowNext");
        }
    }
    private void showPrevious(){
        try{
            switch(section){
                case 0,2,3->{
                    LinkedList<Long> currentList = getCurrentList();
                    thirdUserID = currentList.get(currentList.indexOf(thirdUserID)-1);
                }
                case 1->{
                    LinkedList<String> groupsList = getGroupsList();
                    groupName = groupsList.get(groupsList.indexOf(groupName)-1);
                }
            }
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowPrevious");
        }
    }
    private Page showSavedMessages(){
        return new MessagingPage(getCurrentUser(),this, getClientHandler());
    }
    private Page messageUser(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideName");
        else{
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
                return new MessagingPage(targetUser,this, getClientHandler());
            }
        }
        return this;
    }
    private void addUser(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideName");
        else{
            User user = getUser(text);
            if(user==null){
                setDialog("UserNotFound");
            }
            else if(!user.isActive()){
                setDialog("AccountNotActive");
            }
            else if(!user.isOwned()){
                setDialog("AccountNotOwned");
            }
            else if(!UserLogic.canMessage(getCurrentUser(),user.getId())){
                setDialog("FollowingNeededToMessage");
            }
            else{
                synchronized(DataBase.getSyncObj()){
                    User currentUser = getCurrentUser();
                    LinkedList<Long> currentList = getCurrentList();
                    currentList.add(user.getId());
                    currentUser.getPvGroups().put(groupName,currentList);
                    updateUser(currentUser);
                }
                logger.info("updated group \""+groupName+"\": added user: "+user.getId());
            }
        }
    }
    private void removeThisUser(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser();
            LinkedList<Long> currentList = getCurrentList();
            currentList.remove(thirdUserID);
            if(!currentList.isEmpty()){
                currentUser.getPvGroups().put(groupName, currentList);
                gotoGroupManagement();
            }
            else{
                currentUser.getPvGroups().remove(groupName);
                gotoGroupsList();
            }
            updateUser(currentUser);
        }
        logger.info("updated group \""+groupName+"\": removed user: "+thirdUserID);
    }
    private void selectUser(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideName");
        else{
            User user = getUser(text);
            if(user==null){
                setDialog("UserNotFound");
            }
            else if(!user.isActive()){
                setDialog("AccountNotActive");
            }
            else if(!user.isOwned()){
                setDialog("AccountNotOwned");
            }
            else if(!UserLogic.canMessage(getCurrentUser(),user.getId())){
                setDialog("FollowingNeededToMessage");
            }
            else{
                selectionList.add(user.getId());
                if(thirdUserID==-1) thirdUserID = getCurrentList().getFirst();
            }
        }
    }
    private void selectGroup(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideName");
        else{
            User currentUser = getCurrentUser();
            if(currentUser.getPvGroups().containsKey(text)){
                for(Long i: currentUser.getPvGroups().get(text)){
                    if(!selectionList.contains(i)) selectUser(getUsername(i));
                }
                setDialog("SelectedGroup");
            }
            else{
                setDialog("GroupNotFound");
            }
        }
    }
    private void deselectUser(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideName");
        else{
            User user = getUser(text);
            if(user==null){
                setDialog("UserNotFound");
            }
            else if(!selectionList.remove(user.getId())){
                setDialog("RedundantRemove");
            }
            else{
                setDialog("DeselectedUser");
            }
        }
    }
    private void deselectGroup(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideName");
        else{
            User currentUser = getCurrentUser();
            if(currentUser.getPvGroups().containsKey(text)){
                for(Long i: currentUser.getPvGroups().get(text)){
                    selectionList.remove(i);
                }
                setDialog("DeselectedGroup");
            }
            else{
                setDialog("GroupNotFound");
            }
        }
    }
    private void stopSelectionAndCreateGroup(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideName");
        else{
            if(selectionList.isEmpty()){
                setDialog("LeftSelectionWithEmptyList");
                logger.info("group creation failed: empty list");
            }
            else{
                synchronized(DataBase.getSyncObj()){
                    User currentUser = getCurrentUser();
                    if(currentUser.getPvGroups().containsKey(text))
                        setDialog("OverrodeGroup");
                    else setDialog("CreatedGroup");
                    currentUser.getPvGroups().put(text,selectionList);
                    updateUser(currentUser);
                }
                logger.info("created pv group: "+text);
            }
            backToChatList();
            logger.info("section set to: 0 = chat list");
        }
    }
    private void stopSelectionAndSendMessage(String text,byte[] imageData){
        if(text==null || text.isBlank()) setDialog("MustProvideText");
        else{
            if(!selectionList.isEmpty()){
                synchronized(DataBase.getSyncObj()){
                    User currentUser = getCurrentUser(), targetUser;
                    LinkedList<Long> currentMessageList;
                    Message newMessage;
                    for(Long i: selectionList){
                        targetUser = getUser(i);
                        if(targetUser != null && targetUser.isActive() && targetUser.isOwned() && UserLogic.canMessage(getCurrentUser(), targetUser.getId())){
                            if(!currentUser.getMessages().containsKey(targetUser.getId())){
                                currentMessageList = new LinkedList<>();
                                currentUser.getMessages().put(targetUser.getId(), currentMessageList);
                                targetUser.getMessages().put(currentUser.getId(), currentMessageList);
                                currentUser.getUnread().put(targetUser.getId(),0);
                                targetUser.getUnread().put(currentUser.getId(),0);
                                updateUser(currentUser);
                                updateUser(targetUser);
                            }
                            if(targetUser.isOwned() && targetUser.isActive() && UserLogic.canMessage(currentUser,i)){
                                int seen;
                                synchronized(getDataHolder().getOnlineUsers()){
                                    if(currentUser.equals(targetUser)) seen=4;
                                    else if(getDataHolder().getOnlineUsers().containsValue(targetUser.getId())) seen=3;
                                    else seen=2;
                                }
                                newMessage = new Message(text,currentUser.getId(),targetUser.getId(),generateNewMessageID(currentUser.getId(),targetUser.getId()),-1,seen, saveNewImage(imageData));
                                UserLogic.addMessage(currentUser,newMessage);
                                UserLogic.addMessage(targetUser,newMessage);
                                updateUser(currentUser);
                                updateUser(targetUser);
                                saveNewMessage(newMessage);
                            }
                        }
                    }
                    setDialog("MessageSent");
                }
                logger.info("message sent to selected list");
            }
            else{
                setDialog("LeftSelectionWithEmptyList");
                logger.info("failed to send the message: empty list");
            }
            backToChatList();
            logger.info("section set to: 0 = chat list");
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(ChatListPageRequest request){
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        if(request.getCommand()==Command.home) return new MainMenuPage(getClientHandler());
        else if(request.getCommand()==Command.back){
            if(section==0) return getPreviousPage();
            else backToChatList();
        }
        else{
            switch(section){
                case 0->{
                    switch(request.getCommand()){
                        case showNext-> showNext();
                        case showPrevious-> showPrevious();
                        case showSavesMessages->{ return showSavedMessages(); }
                        case messageUser->{ return messageUser(request.getStringArgs()[0]); }
                        case showPVGroups-> gotoGroupsList();
                        case selectionMode-> gotoSelectionMode();
                    }
                }
                case 1->{
                    if(groupName!=null){
                        switch(request.getCommand()){
                            case showNext-> showNext();
                            case showPrevious-> showPrevious();
                            case enterPVGroupManagement-> gotoGroupManagement();
                        }
                    }
                }
                case 2->{
                    if(thirdUserID!=-1){
                        switch(request.getCommand()){
                            case showNext-> showNext();
                            case showPrevious-> showPrevious();
                            case addUser-> addUser(request.getStringArgs()[0]);
                            case removeThisUser-> removeThisUser();
                        }
                    }
                }
                case 3->{
                    if(request.getCommand()==Command.selectUser) selectUser(request.getStringArgs()[0]);
                    else if(request.getCommand()==Command.selectGroup) selectGroup(request.getStringArgs()[0]);
                    else if(thirdUserID!=-1){
                        switch(request.getCommand()){
                            case showNext-> showNext();
                            case showPrevious-> showPrevious();
                            case deselectUser-> deselectUser(request.getStringArgs()[0]);
                            case deselectGroup-> deselectGroup(request.getStringArgs()[0]);
                            case stopSelectionAndCreateGroup-> stopSelectionAndCreateGroup(request.getStringArgs()[0]);
                            case stopSelectionAndSendMessage-> stopSelectionAndSendMessage(request.getStringArgs()[0],request.getImageData());
                        }
                    }
                }
            }
        }
        return this;
    }

    @Override
    public Page update() {
        switch(section){
            case 0->{
                if(!getCurrentList().contains(thirdUserID)) backToChatList();
            }
            case 1->{
                if(!getGroupsList().contains(groupName)) gotoGroupsList();
            }
            case 2->{
                if(!getCurrentList().contains(thirdUserID)) gotoGroupManagement();
            }
        }
        return this;
    }

    @Override
    public Response getData(){
        int section, count=0;
        String dialog, sectionTitle=null, nameText=null;
        byte[] image = null;

        section = getSection();
        dialog = getDialog();
        clearDialog();
        switch(getSection()){
            case 0->{
                if(getThirdUserID()>-1){
                    count = getCurrentUser().getUnread().get(getThirdUserID());
                    nameText = getUsername(getThirdUserID());
                    image = getImage(getUser(getThirdUserID()).getPictureID());
                }
            }
            case 1-> nameText = getGroupName();
            case 2->{
                sectionTitle = getGroupName();
                if(getThirdUserID()>-1){
                    nameText = getUsername(getThirdUserID());
                    image = getImage(getUser(getThirdUserID()).getPictureID());
                }
            }
            case 3->{
                count = getCurrentList().size();
                if(getThirdUserID()>-1){
                    nameText = getUsername(getThirdUserID());
                    image = getImage(getUser(getThirdUserID()).getPictureID());
                }
            }
        }
        return new ChatListPageResponse(dialog,sectionTitle,nameText,section,count,image);
    }
}
