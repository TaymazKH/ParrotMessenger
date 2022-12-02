package server.logic.page_logic;

import server.database.DataBase;
import server.logic.ClientHandler;
import server.logic.DataHolder;
import server.logic.model_logic.UserLogic;
import server.util.Config;
import server.logic.model_logic.TweetLogic;
import server.model.*;
import shared.response.OfflineDataBaseResponse;
import shared.response.Response;
import shared.simple_model.SimpleGroupMessage;
import shared.simple_model.SimpleMessage;
import shared.simple_model.SimpleTweet;
import shared.simple_model.SimpleUser;
import shared.util.PageType;

import java.util.*;

public abstract class Page {
    private final PageType pageType;
    private Page previousPage;
    private String dialog;
    private ClientHandler clientHandler;

    public Page(PageType pageType, Page previousPage, ClientHandler clientHandler){
        this.pageType = pageType;
        this.previousPage = previousPage;
        this.clientHandler = clientHandler;
        this.dialog="";
    }

//    public abstract Page runCommand(Request request);
    public abstract Page update();
    public abstract Response getData();

    public long getCurrentID() {
        return clientHandler.getCurrentID();
    }
    public User getCurrentUser() {
        return clientHandler.getCurrentUser();
    }

    public PageType getPageType() {
        return pageType;
    }
    public Page getPreviousPage() {
        return previousPage;
    }
    public String getDialog() {
        return dialog;
    }
    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public void setPreviousPage(Page previousPage) {
        this.previousPage = previousPage;
    }
    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void clearDialog(){
        setDialog(null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public DataHolder getDataHolder() {
        return clientHandler.getDataHolder();
    }

    public HashMap<Long,String> getPhoneNumberMap(){
        return getDataHolder().getDataBase().getPhoneNumberMap();
    }
    public HashMap<Long,String> getEmailMap(){
        return getDataHolder().getDataBase().getEmailMap();
    }

    public void updateIDUsernameMapFiles(User user){
        getDataHolder().getDataBase().updateIDUsernameMapFiles(user);
    }
    public void updatePhoneNumberMapFile(User user){
        getDataHolder().getDataBase().updatePhoneNumberMapFile(user);
    }
    public void updateEmailMapFile(User user){
        getDataHolder().getDataBase().updateEmailMapFile(user);
    }

    public User getUser(String username){
        return getDataHolder().getDataBase().getUser(username);
    }
    public User getUser(long id){
        return getDataHolder().getDataBase().getUser(id);
    }
    public LinkedList<String> getUsernames(){
        return getDataHolder().getDataBase().getUsernames();
    }
    public String getUsername(long id){
        return getDataHolder().getDataBase().getUsername(id);
    }
    public void saveNewUser(User user){
        getDataHolder().getDataBase().saveNewUser(user);
    }
    public void updateUser(User user){
        getDataHolder().getDataBase().updateUser(user);
    }

    public Tweet getTweet(long mainSender,long id){
        return getDataHolder().getDataBase().getTweet(mainSender,id);
    }
    public void saveNewTweet(Tweet tweet){
        getDataHolder().getDataBase().saveNewTweet(tweet);
    }
    public void updateTweet(Tweet tweet){
        getDataHolder().getDataBase().updateTweet(tweet);
    }

    public Message getMessage(long sender,long receiver,long id){
        return getDataHolder().getDataBase().getMessage(sender, receiver, id);
    }
    public void saveNewMessage(Message message){
        getDataHolder().getDataBase().saveNewMessage(message);
    }
    public void updateMessage(Message message){
        getDataHolder().getDataBase().updateMessage(message);
    }

    public Group getGroup(long id){
        return getDataHolder().getDataBase().getGroup(id);
    }
    public void saveNewGroup(Group group){
        getDataHolder().getDataBase().saveNewGroup(group);
    }
    public void updateGroup(Group group){
        getDataHolder().getDataBase().updateGroup(group);
    }

    public GroupMessage getGroupMessage(long groupID, long id){
        return getDataHolder().getDataBase().getGroupMessage(groupID, id);
    }
    public void saveNewGroupMessage(GroupMessage message){
        getDataHolder().getDataBase().saveNewGroupMessage(message);
    }
    public void updateGroupMessage(GroupMessage message){
        getDataHolder().getDataBase().updateGroupMessage(message);
    }

    public byte[] getImage(long id){
        return getDataHolder().getDataBase().getImage(id);
    }
    public long saveNewImage(byte[] imageData){
        return getDataHolder().getDataBase().saveNewImage(imageData);
    }

    public void deleteAccount(){
        long currentUserID = clientHandler.getCurrentID();
        synchronized(DataBase.getSyncObj()){
            User targetUser, currentUser = getDataHolder().getDataBase().getUser(currentUserID);
            for(long targetID: currentUser.getFollowing()){
                targetUser = getUser(targetID);
                targetUser.getFollowers().remove(currentUserID);
                LinkedList<String> toDelete = new LinkedList<>();
                for(Map.Entry<String,LinkedList<Long>> m: targetUser.getPvGroups().entrySet()){
                    m.getValue().remove(currentUserID);
                    if(m.getValue().isEmpty())
                        toDelete.add(m.getKey());
                }
                for(String groupName: toDelete)
                    targetUser.getPvGroups().remove(groupName);
                targetUser.getMessages().remove(currentUserID);
                getDataHolder().getDataBase().deleteMessageFiles(currentUser,targetUser);
                updateUser(targetUser);
            }
            for(long targetID: currentUser.getFollowers()){
                targetUser = getUser(targetID);
                targetUser.getFollowing().remove(currentUserID);
                LinkedList<String> toDelete = new LinkedList<>();
                for(Map.Entry<String,LinkedList<Long>> m: targetUser.getPvGroups().entrySet()){
                    m.getValue().remove(currentUserID);
                    if(m.getValue().isEmpty())
                        toDelete.add(m.getKey());
                }
                for(String groupName: toDelete)
                    targetUser.getPvGroups().remove(groupName);
                targetUser.getMessages().remove(currentUserID);
                getDataHolder().getDataBase().deleteMessageFiles(currentUser,targetUser);
                updateUser(targetUser);
            }
            for(long targetID: currentUser.getBlackList()){
                targetUser = getUser(targetID);
                targetUser.getBlockedBy().remove(currentUserID);
                updateUser(targetUser);
            }
            for(long targetID: currentUser.getBlockedBy()){
                targetUser = getUser(targetID);
                targetUser.getBlackList().remove(currentUserID);
                updateUser(targetUser);
            }
            for(long targetID: currentUser.getMuted()){
                targetUser = getUser(targetID);
                targetUser.getMutedBy().remove(currentUserID);
                updateUser(targetUser);
            }
            for(long targetID: currentUser.getMutedBy()){
                targetUser = getUser(targetID);
                targetUser.getMuted().remove(currentUserID);
                updateUser(targetUser);
            }
//            for(long targetID: currentUser.getReported()){
//                targetUser = getUser(targetID);
//                targetUser.getReportedBy().remove(currentUserID);
//                updateUser(targetUser);
//            }
//            for(long targetID: currentUser.getReportedBy()){
//                targetUser = getUser(targetID);
//                targetUser.getReported().remove(currentUserID);
//                updateUser(targetUser);
//            }
            for(long[] targetID: currentUser.getIncomingRequests()){
                targetUser = getUser(targetID[0]);
                LinkedList<long[]> toDelete = new LinkedList<>();
                for(long[] request: targetUser.getOutgoingRequests())
                    if(request[0]==currentUserID) toDelete.add(request);
                for(long[] request: toDelete)
                    targetUser.getOutgoingRequests().remove(request);
                updateUser(targetUser);
            }
            for(long[] targetID: currentUser.getOutgoingRequests()){
                targetUser = getUser(targetID[0]);
                LinkedList<long[]> toDelete = new LinkedList<>();
                for(long[] request: targetUser.getIncomingRequests())
                    if(request[0]==currentUserID) toDelete.add(request);
                for(long[] request: toDelete)
                    targetUser.getIncomingRequests().remove(request);
                updateUser(targetUser);
            }
            for(Long groupID: currentUser.getGroups()){
                Group group = getGroup(groupID);
                group.getMembers().remove(currentUserID);
                if(group.getMembers().isEmpty()) group.setActive(false);
                else group.setOwner(group.getMembers().getFirst());
                updateGroup(group);
            }
            currentUser.setUserName(String.valueOf(currentUser.getId()));
            currentUser.setPhoneNumber("");
            currentUser.setEmail("");
            currentUser.setOwned(false);
            UserLogic.logInOut(currentUser,false);
            updateIDUsernameMapFiles(currentUser);
            updatePhoneNumberMapFile(currentUser);
            updateEmailMapFile(currentUser);
            updateUser(currentUser);
        }
    }
    public void checkToHideTweet(Tweet tweet){
        if(tweet.getReportedUsers().size() > Config.getConfig("importantNumbers").getProperty(Integer.class,"reportLimit")) tweet.setViewable(false);
        TweetLogic.dislike(tweet,getCurrentUser().getId());
        TweetLogic.dislike(tweet,getCurrentUser().getId());
        updateTweet(tweet);
    }
    public OfflineDataBaseResponse getOfflineDataBase(){
        User currentUser = getCurrentUser();
        if(currentUser==null) return new OfflineDataBaseResponse(null);
        SimpleUser simpleUser;
        synchronized(DataBase.getSyncObj()){
            int maxPostCount = Config.getConfig("importantNumbers").getProperty(Integer.class, "maxOfflinePostCount");
            // tweets
            LinkedList<SimpleTweet> tweets = new LinkedList<>();
            ArrayList<Long> tweetIDs = new ArrayList<>(currentUser.getTweets());
            while(tweetIDs.size()>maxPostCount) tweetIDs.remove(0);
            for(long tweetID: tweetIDs){
                Tweet tweet = getTweet(currentUser.getId(),tweetID);
                SimpleTweet simpleTweet = new SimpleTweet(tweet.getText(),getUsername(tweet.getSender()),getUsername(tweet.getRepostedFrom()),getImage(tweet.getPictureID()),getImage(getUser(tweet.getSender()).getPictureID()),tweet.getPostedTime(),tweet.getEditedTimes(),tweet.getLikedTimes(),tweet.getDislikedTimes());
                tweets.add(simpleTweet);
            }
            // messages
            LinkedHashMap<String,LinkedList<SimpleMessage>> messages = new LinkedHashMap<>();
            for(Map.Entry<Long,LinkedList<Long>> m: currentUser.getMessages().entrySet()){
                User user = getUser(m.getKey());
                ArrayList<Long> messageIDs = new ArrayList<>(m.getValue());
                LinkedList<SimpleMessage> oneUsersMessages = new LinkedList<>();
                if(currentUser.getId()==user.getId()) continue;
                for(int i=0;i<currentUser.getUnread().get(user.getId());i++) messageIDs.remove(messageIDs.size()-1);
                while(messageIDs.size()>maxPostCount) messageIDs.remove(0);
                for(long messageID: messageIDs){
                    Message message = getMessage(currentUser.getId(),user.getId(),messageID);
                    SimpleMessage simpleMessage = new SimpleMessage(message.getText(),getUsername(message.getSender()),getUsername(message.getRepostedFrom()),getImage(message.getPictureID()),getImage(getUser(message.getSender()).getPictureID()),message.getPostedTime(),message.getEditedTimes(),message.getSeen());
                    oneUsersMessages.add(simpleMessage);
                }
                messages.put(user.getUserName(),oneUsersMessages);
            }
            // group messages
            LinkedHashMap<String,LinkedList<SimpleGroupMessage>> groupMessages = new LinkedHashMap<>();
            for(long groupID: currentUser.getGroups()){
                Group group = getGroup(groupID);
                ArrayList<Long> messageIDs = new ArrayList<>(group.getMessages());
                LinkedList<SimpleGroupMessage> oneGroupsMessages = new LinkedList<>();
                while(messageIDs.size()>maxPostCount) messageIDs.remove(0);
                for(Long messageID: messageIDs){
                    GroupMessage message = getGroupMessage(group.getId(),messageID);
                    SimpleGroupMessage simpleGroupMessage = new SimpleGroupMessage(message.getText(),getUsername(message.getSender()),getUsername(message.getRepostedFrom()),getImage(message.getPictureID()),getImage(getUser(message.getSender()).getPictureID()),message.getPostedTime(),message.getEditedTimes());
                    oneGroupsMessages.add(simpleGroupMessage);
                }
                groupMessages.put(group.getName(),oneGroupsMessages);
            }
            simpleUser = new SimpleUser(currentUser.getFirstName(), currentUser.getLastName(), currentUser.getUserName(), currentUser.getId(), getImage(currentUser.getPictureID()), currentUser.getBirthDate(), currentUser.getEmail(), currentUser.getPhoneNumber(), currentUser.getBio(), tweets, groupMessages, messages);
        }
        return new OfflineDataBaseResponse(simpleUser);
    }

    public long generateNewUserID(){
        return getDataHolder().getDataBase().generateNewUserID();
    }
    public long generateNewTweetID(long mainSenderID){
        return getDataHolder().getDataBase().generateNewTweetID(mainSenderID);
    }
    public long generateNewMessageID(long sender, long receiver){
        return getDataHolder().getDataBase().generateNewMessageID(sender, receiver);
    }
    public long generateNewGroupID(){
        return getDataHolder().getDataBase().generateNewGroupID();
    }
    public long generateNewGroupMessageID(long groupID){
        return getDataHolder().getDataBase().generateNewGroupMessageID(groupID);
    }

    public long getLatestUserID(){
        return getDataHolder().getDataBase().getLatestUserID();
    }
}
