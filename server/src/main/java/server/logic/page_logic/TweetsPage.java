package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.ClientHandler;
import server.model.Message;
import server.model.Tweet;
import server.model.User;
import server.logic.model_logic.TweetLogic;
import server.logic.model_logic.UserLogic;
import shared.request.TweetsPageRequest;
import shared.response.Response;
import shared.response.TweetsPageResponse;
import shared.simple_model.SimpleTweet;
import shared.util.Command;
import shared.util.PageType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TweetsPage extends Page {
    private static final Logger logger = LogManager.getLogger(TweetsPage.class);
    private final long targetUserID;
    private final boolean isThisCurrentUsersPage;
    private long currentTweetID;

    public TweetsPage(User targetUser, Page previousPage, ClientHandler clientHandler) {
        super(PageType.tweets, previousPage, clientHandler);
        logger.info("entered the page");
        this.targetUserID = targetUser.getId();
        this.isThisCurrentUsersPage = getCurrentUser().equals(targetUser);
        initializePage();
    }

    public User getTargetUser() {
        return getUser(targetUserID);
    }
    public boolean isThisCurrentUsersPage() {
        return isThisCurrentUsersPage;
    }
    public Tweet getCurrentTweet() {
        return getTweet(targetUserID,currentTweetID);
    }
    public long getCurrentTweetID() {
        return currentTweetID;
    }
    public void setCurrentTweetID(long currentTweetID) {
        this.currentTweetID = currentTweetID;
    }

    private void initializePage(){
        try{
            synchronized(DataBase.getSyncObj()){
                setCurrentTweetID(getTargetUser().getTweets().getLast());
            }
        } catch(NoSuchElementException e){
            setCurrentTweetID(-1);
        }
    }
    private void newTweet(String text,byte[] imageData){
        if(text==null || text.isBlank()) setDialog("MustProvideText");
        else{
            synchronized(DataBase.getSyncObj()){
                User targetUser = getTargetUser();
                Tweet newTweet = new Tweet(text,getCurrentUser().getId(),targetUser.getId(),-1,generateNewTweetID(targetUser.getId()),null,saveNewImage(imageData));
                UserLogic.addTweet(targetUser,newTweet);
                saveNewTweet(newTweet);
                updateUser(targetUser);
                if(getCurrentTweetID()==-1) setCurrentTweetID(newTweet.getId());
            }
        }
    }
    private void repost(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser();
            Tweet currentTweet = getCurrentTweet();
            Tweet newTweet = new Tweet(currentTweet.getText(), currentUser.getId(), currentUser.getId(), TweetLogic.getRepostedID(currentTweet), generateNewTweetID(currentUser.getId()), null, currentTweet.getPictureID());
            UserLogic.addTweet(currentUser,newTweet);
            saveNewTweet(newTweet);
            updateUser(currentUser);
        }
    }
    private void sendToSavedMessages(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser();
            Tweet currentTweet = getCurrentTweet();
            if(!currentUser.getMessages().containsKey(currentUser.getId())){
                currentUser.getMessages().put(currentUser.getId(),new LinkedList<>());
            }
            Message newMessage = new Message(currentTweet.getText(), currentUser.getId(), currentUser.getId(), generateNewMessageID(currentUser.getId(),currentUser.getId()), TweetLogic.getRepostedID(currentTweet),4, currentTweet.getPictureID());
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
                    Tweet currentTweet = getCurrentTweet();
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
                    Message newMessage = new Message(currentTweet.getText(), currentUser.getId(), targetUser.getId(), generateNewMessageID(currentUser.getId(), targetUser.getId()), TweetLogic.getRepostedID(currentTweet), seen, currentTweet.getPictureID());
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
    private void showNext(){
        try{
            Tweet tweet;
            synchronized(DataBase.getSyncObj()){
                Tweet currentTweet = getCurrentTweet();
                tweet = getTweet(getTargetUser().getId(), TweetLogic.showNextInPersonalPage(currentTweet, getTweet(currentTweet.getMainSender(),currentTweet.getUpperTweetID()), getTargetUser().getTweets()));
            }
            setCurrentTweetID(tweet.getId());
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowNext");
        }
    }
    private void showPrevious(){
        try{
            Tweet tweet;
            synchronized(DataBase.getSyncObj()){
                Tweet currentTweet = getCurrentTweet();
                tweet = getTweet(getTargetUser().getId(), TweetLogic.showPreviousInPersonalPage(currentTweet, getTweet(currentTweet.getMainSender(),currentTweet.getUpperTweetID()), getTargetUser().getTweets()));
            }
            setCurrentTweetID(tweet.getId());
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowPrevious");
        }
    }
    private void showUpperTweet(){
        try{
            synchronized(DataBase.getSyncObj()){
                setCurrentTweetID(TweetLogic.showUpperTweetID(getCurrentTweet()));
            }
        } catch(NullPointerException e){
            setDialog("NoUpperTweet");
        }
    }
    private void showComments(){
        try{
            Tweet tweet;
            synchronized(DataBase.getSyncObj()){
                tweet = getTweet(getCurrentTweet().getMainSender(), TweetLogic.showComments(getCurrentTweet()));
            }
            setCurrentTweetID(tweet.getId());
        } catch(IndexOutOfBoundsException e){
            setDialog("NoComment");
        }
    }
    private void addComment(String text,byte[] imageData){
        if(text==null || text.isBlank()) setDialog("MustProvideText");
        else{
            synchronized(DataBase.getSyncObj()){
                Tweet currentTweet = getCurrentTweet();
                Tweet newTweet = new Tweet(text,getCurrentUser().getId(), currentTweet.getMainSender(),-1,generateNewTweetID(currentTweet.getMainSender()), currentTweet, saveNewImage(imageData));
                TweetLogic.addComment(currentTweet,newTweet);
                saveNewTweet(newTweet);
            }
        }
    }
    private void edit(String text){
        synchronized(DataBase.getSyncObj()){
            Tweet currentTweet = getCurrentTweet();
            if(text==null || text.isBlank()) setDialog("MustProvideText");
            else if(getCurrentUser().getId()!=currentTweet.getSender()) setDialog("NotTheOwner");
            else if(currentTweet.getRepostedFrom()>-1) setDialog("CantEditForwarded");
            else{
                currentTweet.setText(text);
                currentTweet.setEditedTimes(currentTweet.getEditedTimes()+1);
                updateTweet(currentTweet);
            }
        }
    }
    private void delete(){
        if(getCurrentUser().getId()!=getCurrentTweet().getSender()) setDialog("NotTheOwner");
        else{
            synchronized(DataBase.getSyncObj()){
                Tweet toDelete = getCurrentTweet();
                showNext();
                if(getDialog()!=null && !getDialog().isEmpty()){
                    clearDialog();
                    showPrevious();
                    if(getDialog()!=null && !getDialog().isEmpty()){
                        clearDialog();
                        showUpperTweet();
                        if(getDialog()!=null && !getDialog().isEmpty()){
                            clearDialog();
                            setCurrentTweetID(-1);
                        }
                    }
                }
                if(toDelete.isPrimaryTweet()){
                    User targetUser = getTargetUser();
                    targetUser.getTweets().remove(toDelete.getId());
                    updateUser(targetUser);
                }
                else{
                    Tweet upperTweet = getTweet(toDelete.getMainSender(),toDelete.getUpperTweetID());
                    upperTweet.getComments().remove(toDelete.getId());
                    updateTweet(upperTweet);
                }
                toDelete.setOwned(false);
                updateTweet(toDelete);
            }
        }
    }
    private Page showSender(){
        User targetUser;
        synchronized(DataBase.getSyncObj()){
            targetUser = getUser(getCurrentTweet().getSender());
        }
        if(!targetUser.isActive()){
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
    private void report(){
        synchronized(DataBase.getSyncObj()){
            if(getCurrentTweet().getReportedUsers().contains(getCurrentUser().getId()))
                setDialog("AlreadyReported");
            else{
                Tweet currentTweet = getCurrentTweet();
                currentTweet.getReportedUsers().add(getCurrentUser().getId());
                checkToHideTweet(currentTweet);
            }
        }
    }
    private void like(){
        synchronized(DataBase.getSyncObj()){
            if(getCurrentTweet().getReportedUsers().contains(getCurrentUser().getId()))
                setDialog("CantUndoReport");
            else{
                Tweet currentTweet = getCurrentTweet();
                switch(TweetLogic.like(currentTweet,getCurrentUser().getId())){
                    case -1 -> setDialog("RetractedDislike");
                    case 0 -> setDialog("AlreadyLiked");
                    case 1 -> setDialog("Liked");
                }
                updateTweet(currentTweet);
            }
        }
    }
    private void dislike(){
        synchronized(DataBase.getSyncObj()){
            if(getCurrentTweet().getReportedUsers().contains(getCurrentUser().getId()))
                setDialog("AutomaticallyDisliked");
            else{
                Tweet currentTweet = getCurrentTweet();
                switch(TweetLogic.dislike(currentTweet,getCurrentUser().getId())){
                    case 1 -> setDialog("RetractedLike");
                    case 0 -> setDialog("AlreadyDisliked");
                    case -1 -> setDialog("Disliked");
                }
                updateTweet(currentTweet);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(TweetsPageRequest request) {
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+ Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        if(request.getCommand()==Command.home) return new MainMenuPage(getClientHandler());
        else if(request.getCommand()==Command.back) return getPreviousPage();
        else if(request.getCommand()==Command.newTweet) newTweet(request.getStringArgs()[0], request.getImageData());
        else if(getCurrentTweetID()!=-1){
            switch(request.getCommand()){
                case repost-> repost();
                case save-> sendToSavedMessages();
                case forwardToUser-> forwardToUser(request.getStringArgs()[0]);
                case showNext-> showNext();
                case showPrevious-> showPrevious();
                case showUpperTweet-> showUpperTweet();
                case showComments-> showComments();
                case addComment-> addComment(request.getStringArgs()[0], request.getImageData());
                case edit-> edit(request.getStringArgs()[0]);
                case delete-> delete();
                case showSender->{ return showSender(); }
                case report-> report();
                case like-> like();
                case dislike-> dislike();
            }
        }
        else{
            setDialog("CantExecuteNothingToShow");
            //logger.info("unable to run the command: currentTweet field is null");
        }
        return this;
    }

    @Override
    public Page update() {
        if(!isThisCurrentUsersPage()){
            User targetUser = getTargetUser();
            if(!UserLogic.requestsToSeeTweetsInTweetsPage(targetUser,getCurrentID()))
                return getPreviousPage();
        }
        Tweet tweet = getCurrentTweet();
        if(tweet==null || !tweet.isOwned() || !tweet.isViewable()){
            initializePage();
        }
        return this;
    }

    @Override
    public Response getData() {
        String dialog,repostedFrom=null;
        SimpleTweet simpleTweet=null;
        boolean isThisCurrentUsersPage = isThisCurrentUsersPage();

        dialog = getDialog();
        clearDialog();
        Tweet tweet = getCurrentTweet();
        if(tweet!=null){
            if(tweet.getRepostedFrom()>-1)
                repostedFrom = getUsername(tweet.getRepostedFrom());
            simpleTweet = new SimpleTweet(tweet.getText(), getUsername(tweet.getSender()), repostedFrom, getImage(tweet.getPictureID()), getImage(getUser(tweet.getSender()).getPictureID()), tweet.getPostedTime(), tweet.getEditedTimes(), tweet.getLikedTimes(), tweet.getDislikedTimes());
        }
        return new TweetsPageResponse(simpleTweet,dialog,isThisCurrentUsersPage);
    }
}
