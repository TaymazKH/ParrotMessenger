package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.ClientHandler;
import server.logic.model_logic.GroupLogic;
import server.model.TimedMessage;
import server.util.Config;
import shared.request.GroupsPageRequest;
import server.model.Group;
import server.model.GroupMessage;
import server.model.Message;
import server.model.User;
import server.logic.model_logic.GroupMessageLogic;
import server.logic.model_logic.UserLogic;
import shared.response.GroupsPageResponse;
import shared.response.Response;
import shared.simple_model.SimpleGroupMessage;
import shared.util.Command;
import shared.util.PageType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class GroupsPage extends Page {
    private static final Logger logger = LogManager.getLogger(GroupsPage.class);
    private int section; // 0: groups list / 1: messages / 2: members
    private long thirdUserID, groupID, messageID;

    public GroupsPage(Page previousPage, ClientHandler clientHandler) {
        super(PageType.groups, previousPage, clientHandler);
        backToGroupsList();
        logger.info("entered groups page");
    }

    public int getSection() {
        return section;
    }
    public long getThirdUserID() {
        return thirdUserID;
    }
    public long getGroupID() {
        return groupID;
    }
    public Group getCurrentGroup() {
        return getGroup(groupID);
    }
    public GroupMessage getCurrentMessage() {
        return getGroupMessage(groupID,messageID);
    }

    private void backToGroupsList(){
        section=0;
        thirdUserID=-1;
        messageID=-1;
        try{ groupID = getCurrentUser().getGroups().getFirst(); }
        catch(Exception e){ groupID=-1; }
    }
    private void backToMessages(){
        section=1;
        thirdUserID=-1;
    }
    private void showNext(){
        try{
            switch(section){
                case 0->{
                    User currentUser = getCurrentUser();
                    groupID = currentUser.getGroups().get(currentUser.getGroups().indexOf(groupID)+1);
                }
                case 1-> messageID = GroupMessageLogic.showNext(messageID, getCurrentGroup().getMessages());
                case 2-> thirdUserID = GroupLogic.showNext(thirdUserID, getCurrentGroup().getMembers());
            }
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowNext");
        }
    }
    private void showPrevious(){
        try{
            switch(section){
                case 0->{
                    User currentUser = getCurrentUser();
                    groupID = currentUser.getGroups().get(currentUser.getGroups().indexOf(groupID)-1);
                }
                case 1-> messageID = GroupMessageLogic.showPrevious(messageID, getCurrentGroup().getMessages());
                case 2-> thirdUserID = GroupLogic.showPrevious(thirdUserID, getCurrentGroup().getMembers());
            }
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowPrevious");
        }
    }
    private Page showTheirPage(){
        User targetUser;
        if(section==1) targetUser = getUser(getCurrentMessage().getSender());
        else targetUser = getUser(thirdUserID);
        if(targetUser==null){
            setDialog("UserNotFound");
        }
        else if(!targetUser.isActive()){
            setDialog("AccountNotActive");
        }
        else if(!targetUser.isOwned()){
            setDialog("AccountNotOwned");
        }
        else if(getCurrentUser().getBlockedBy().contains(targetUser.getId())){
            setDialog("YouHaveBeenBanned");
        }
        else{
            return new PersonalPage(targetUser,this, getClientHandler());
        }
        return this;
    }
    private void enter(){
        section=1;
        try{ messageID = getCurrentGroup().getMessages().getLast(); }
        catch(Exception e){ messageID=-1; }
    }
    private void newGroup(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideName");
        else{
            synchronized(DataBase.getSyncObj()){
                User currentUser = getCurrentUser();
                groupID = generateNewGroupID();
                Group newGroup = new Group(text,groupID,currentUser.getId());
                currentUser.getGroups().add(groupID);
                currentUser.getGroupSeen().put(groupID,(long) -1);
                saveNewGroup(newGroup);
                updateUser(currentUser);
                enter();
            }
        }
    }
    private void showMembers(){
        section=2;
        try{ thirdUserID = getCurrentGroup().getMembers().getFirst(); }
        catch(NoSuchElementException e){ thirdUserID=-1; }
    }
    private void newMessage(String text, byte[] imageData){
        if(text==null || text.isBlank()) setDialog("MustProvideText");
        else{
            synchronized(DataBase.getSyncObj()){
                GroupMessage newMessage = new GroupMessage(text,generateNewGroupMessageID(groupID),groupID,getCurrentUser().getId(),-1,saveNewImage(imageData));
                saveNewGroupMessage(newMessage);
                Group currentGroup = getCurrentGroup();
                currentGroup.getMessages().add(newMessage.getId());
                updateGroup(currentGroup);
                if(messageID==-1) messageID = newMessage.getId();
            }
        }
    }
    private void newTimedMessage(String text, String dateText, byte[] imageData){
        if(text==null || text.isBlank()) setDialog("MustProvideText");
        else{
            try{
                int[] date = {
                        Integer.parseInt(dateText.substring(0,4)),
                        Integer.parseInt(dateText.substring(5,7)),
                        Integer.parseInt(dateText.substring(8,10)),
                        Integer.parseInt(dateText.substring(11,13)),
                        Integer.parseInt(dateText.substring(14,16)),
                        Integer.parseInt(dateText.substring(17,19))
                };
                LocalDateTime sendDate = LocalDateTime.of(date[0],date[1],date[2],date[3],date[4],date[5]),
                        validDate = LocalDateTime.now().plusMinutes(Config.getConfig("importantNumbers").getProperty(Integer.class, "timedMessageLeastMinuteDifference"));
                if(sendDate.isBefore(validDate))
                    setDialog("InvalidDate");
                else{
                    synchronized(DataBase.getSyncObj()){
                        getClientHandler().getDataHolder().getMessageTimer().add(
                                new TimedMessage(text,groupID,getCurrentUser().getId(),saveNewImage(imageData),date)
                        );
                    }
                }
            } catch(Exception e){
                setDialog("InvalidDate");
            }
        }
    }
    private void editMessage(String text){
        synchronized(DataBase.getSyncObj()){
            GroupMessage currentMessage = getCurrentMessage();
            if(text==null || text.isBlank()) setDialog("MustProvideText");
            else if(currentMessage.getSender()!=getCurrentUser().getId()) setDialog("NotTheOwner");
            else if(currentMessage.getRepostedFrom()>-1) setDialog("CantEditForwarded");
            else{
                currentMessage.setText(text);
                currentMessage.setEditedTimes(currentMessage.getEditedTimes()+1);
                updateGroupMessage(currentMessage);
            }
        }
    }
    private void delete(){
        synchronized(DataBase.getSyncObj()){
            GroupMessage toDelete = getCurrentMessage();
            Group currentGroup = getCurrentGroup();
            if(getCurrentUser().getId()!=toDelete.getSender()) setDialog("NotTheOwner");
            else{
                showNext();
                if(getDialog()!=null && !getDialog().isEmpty()){
                    clearDialog();
                    showPrevious();
                    if(getDialog()!=null && !getDialog().isEmpty()){
                        clearDialog();
                        messageID=-1;
                    }
                }
                currentGroup.getMessages().remove(toDelete.getId());
                updateGroup(currentGroup);
                toDelete.setOwned(false);
                updateGroupMessage(toDelete);
            }
        }
    }
    private void save(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser();
            if(!currentUser.getMessages().containsKey(currentUser.getId())){
                currentUser.getMessages().put(currentUser.getId(),new LinkedList<>());
            }
            GroupMessage currentMessage = getCurrentMessage();
            Message newMessage = new Message(currentMessage.getText(), currentUser.getId(), currentUser.getId(), generateNewMessageID(currentUser.getId(),currentUser.getId()), GroupMessageLogic.getRepostedID(currentMessage), 4, currentMessage.getPictureID());
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
                    GroupMessage currentMessage = getCurrentMessage();
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
                    Message newMessage = new Message(currentMessage.getText(), currentUser.getId(), targetUser.getId(), generateNewMessageID(currentUser.getId(), targetUser.getId()), GroupMessageLogic.getRepostedID(currentMessage), seen, currentMessage.getPictureID());
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
    private void transferOwnership(){
        User targetUser = getUser(thirdUserID);
        if(targetUser==null){
            setDialog("UserNotFound");
        }
        else if(!targetUser.isOwned()){
            setDialog("AccountNotOwned");
        }
        else{
            synchronized(DataBase.getSyncObj()){
                Group currentGroup = getCurrentGroup();
                currentGroup.setOwner(targetUser.getId());
                updateGroup(currentGroup);
            }
        }
    }
    private void invite(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideText");
        else{
            synchronized(DataBase.getSyncObj()){
                User currentUser = getCurrentUser(), targetUser = getUser(text);
                if(targetUser==null){
                    setDialog("UserNotFound");
                }
                else if(!targetUser.isActive()){
                    setDialog("AccountNotActive");
                }
                else if(!targetUser.isOwned()){
                    setDialog("AccountNotOwned");
                }
                else if(!UserLogic.canMessage(currentUser,targetUser.getId())){
                    setDialog("FollowingNeededToMessage");
                }
                else{
                    boolean hasAlreadySentInvite = false;
                    for(long[] request: targetUser.getIncomingRequests())
                        if(Arrays.equals(request,new long[]{currentUser.getId(),2,groupID})){
                            hasAlreadySentInvite = true;
                            break;
                        }
                    if(hasAlreadySentInvite){
                        setDialog("AlreadyInvited");
                    }
                    else{
                        UserLogic.sendGroupInvitation(currentUser,targetUser,groupID);
                        updateUser(currentUser);
                        updateUser(targetUser);
                        setDialog("InvitationSent");
                    }
                }
            }
        }
    }
    private void kick(){
        if(getCurrentUser().getId()!=thirdUserID){
            synchronized(DataBase.getSyncObj()){
                User targetUser = getUser(thirdUserID);
                Group currentGroup = getCurrentGroup();
                showNext();
                if(!getDialog().isEmpty()){
                    clearDialog();
                    showPrevious();
                    if(!getDialog().isEmpty()){
                        clearDialog();
                        thirdUserID=-1;
                    }
                }
                targetUser.getGroups().remove(groupID);
                targetUser.getGroupSeen().remove(groupID);
                currentGroup.getMembers().remove(targetUser.getId());
                updateUser(targetUser);
                updateGroup(currentGroup);
            }
        }
    }
    private void leave(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser();
            Group currentGroup = getCurrentGroup();
            if(currentGroup.getOwner()==currentUser.getId() && currentGroup.getMembers().size()>1)
                setDialog("TransferOwnershipToLeave");
            else{
                if(currentGroup.getOwner()==currentUser.getId()){
                    LinkedList<long[]> toDelete = new LinkedList<>();
                    for(long[] request: currentUser.getOutgoingRequests())
                        if(request[1]==2 && request[2]==groupID) toDelete.add(request);
                    for(long[] request: toDelete){
                        User targetUser = getUser(request[0]);
                        for(int i=0;i<targetUser.getIncomingRequests().size();i++){
                            long[] incomingRequest = targetUser.getIncomingRequests().get(i);
                            if(incomingRequest[1]==2 && incomingRequest[2]==groupID){
                                targetUser.getIncomingRequests().remove(i);
                                i--;
                            }
                        }
                        updateUser(targetUser);
                        currentUser.getOutgoingRequests().remove(request);
                    }
                    currentGroup.setActive(false);
                }
                currentGroup.getMembers().remove(currentUser.getId());
                currentUser.getGroups().remove(groupID);
                currentUser.getGroupSeen().remove(groupID);
                updateGroup(currentGroup);
                updateUser(currentUser);
                backToGroupsList();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(GroupsPageRequest request){
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+ Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        if(request.getCommand()==Command.home) return new MainMenuPage(getClientHandler());
        else if(request.getCommand()==Command.back){
            if(section==2) backToMessages();
            else if(section==1) backToGroupsList();
            else return getPreviousPage();
        }
        else if(section==0){
            if(request.getCommand()==Command.newGroup) newGroup(request.getStringArgs()[0]);
            else if(groupID!=-1){
                switch(request.getCommand()){
                    case showNext-> showNext();
                    case showPrevious-> showPrevious();
                    case enterGroup-> enter();
                }
            }
            else setDialog("NotInAnyGroup");
        }
        else if(section==1){
            if(request.getCommand()==Command.showMembers) showMembers();
            else if(request.getCommand()==Command.newMessage) newMessage(request.getStringArgs()[0], request.getImageData());
            else if(request.getCommand()==Command.newTimedMessage) newTimedMessage(request.getStringArgs()[0], request.getStringArgs()[1], request.getImageData());
            else if(request.getCommand()==Command.leave) leave();
            else if(messageID!=-1){
                switch(request.getCommand()){
                    case showNext-> showNext();
                    case showPrevious-> showPrevious();
                    case showTheirPage->{ return showTheirPage(); }
                    case edit-> editMessage(request.getStringArgs()[0]);
                    case delete-> delete();
                    case save-> save();
                    case forwardToUser-> forwardToUser(request.getStringArgs()[0]);
                }
            }
            else setDialog("CantExecuteNothingToShow");
        }
        else{
            switch(request.getCommand()){
                case showNext-> showNext();
                case showPrevious-> showPrevious();
                case showTheirPage->{ return showTheirPage(); }
                case transferOwnership-> transferOwnership();
                case invite-> invite(request.getStringArgs()[0]);
                case kick-> kick();
            }
        }
        return this;
    }

    @Override
    public Page update() {
        switch(section){
            case 0->{
                if(!getCurrentUser().getGroups().contains(groupID)) backToGroupsList();
            }
            case 1->{
                GroupMessage message = getCurrentMessage();
                if(message==null || !message.isOwned()) enter();
            }
            case 2->{
                if(!getCurrentGroup().getMembers().contains(thirdUserID)) showMembers();
            }
        }
        return this;
    }

    @Override
    public Response getData(){
        SimpleGroupMessage simpleGroupMessage=null;
        String dialog,groupName=null,memberCount=null,repostedFrom=null,memberNameText=null;
        int section;
        boolean isOwner=false;
        byte[] memberImage=null;

        section = getSection();
        dialog = getDialog();
        clearDialog();
        switch(getSection()){
            case 0->{
                if(getGroupID()>-1){
                    Group group = getGroup(getGroupID());
                    groupName = group.getName();
                    memberCount = String.valueOf(group.getMembers().size());
                }
            }
            case 1->{
                GroupMessage message = getCurrentMessage();
                if(message!=null){
                    if(message.getRepostedFrom()>-1)
                        repostedFrom = getUsername(message.getRepostedFrom());
                    simpleGroupMessage = new SimpleGroupMessage(message.getText(), getUsername(message.getSender()), repostedFrom, getImage(message.getPictureID()), getImage(getUser(message.getSender()).getPictureID()), message.getPostedTime(), message.getEditedTimes());
                }
            }
            case 2->{
                User user = getUser(getThirdUserID());
                memberNameText = user.getUserName();
                memberImage = getImage(user.getPictureID());
                isOwner = getCurrentGroup().getOwner()==getCurrentUser().getId();
            }
        }
        return new GroupsPageResponse(simpleGroupMessage,dialog,groupName,memberCount,memberNameText,section,isOwner,memberImage);
    }
}
