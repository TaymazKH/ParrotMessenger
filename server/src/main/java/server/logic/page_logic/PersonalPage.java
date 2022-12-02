package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.ClientHandler;
import shared.request.PersonalPageRequest;
import server.model.User;
import server.logic.model_logic.UserLogic;
import shared.response.PersonalPageResponse;
import shared.response.Response;
import shared.util.Command;
import shared.util.PageType;

import java.time.LocalDate;
import java.util.Arrays;

public class PersonalPage extends Page {
    private static final Logger logger = LogManager.getLogger(PersonalPage.class);
    private final long targetUserID;
    private final boolean isThisCurrentUsersPage;

    public PersonalPage(User targetUser, Page previousPage, ClientHandler clientHandler) {
        super(PageType.personal, previousPage, clientHandler);
        logger.info("entered the page | targetUser: "+targetUser.getId());
        this.targetUserID = targetUser.getId();
        this.isThisCurrentUsersPage = getCurrentUser().equals(targetUser);
    }

    public User getTargetUser() {
        return getUser(targetUserID);
    }
    public boolean isThisCurrentUsersPage() {
        return isThisCurrentUsersPage;
    }

    private void changeProfilePicture(byte[] imageData){
        long pictureID = saveNewImage(imageData);
        if(pictureID>-1){
            synchronized(DataBase.getSyncObj()){
                User currentUser = getCurrentUser();
                currentUser.setPictureID(pictureID);
                updateUser(currentUser);
            }
            logger.info("changed profile picture");
        }
    }
    private void editBio(String text){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser();
            if(text==null || text.isBlank()) currentUser.setBio("");
            else currentUser.setBio(text);
            updateUser(currentUser);
        }
        logger.info("edited bio");
    }
    private void editPhoneNumber(String text){
        if(text==null || text.isBlank()){
            synchronized(DataBase.getSyncObj()){
                User currentUser = getCurrentUser();
                currentUser.setPhoneNumber("");
                updatePhoneNumberMapFile(currentUser);
                updateUser(currentUser);
            }
            logger.info("edited phone number");
        }
        else if(!getPhoneNumberMap().containsValue(text)){
            synchronized(DataBase.getSyncObj()){
                User currentUser = getCurrentUser();
                currentUser.setPhoneNumber(text);
                updatePhoneNumberMapFile(currentUser);
                updateUser(currentUser);
            }
            logger.info("edited phone number");
        }
        else setDialog("PhoneNumberTaken");
    }
    private void editEmail(String text){
        if(!text.contains("@")) setDialog("InvalidEmail");
        else if(getEmailMap().containsValue(text)) setDialog("EmailTaken");
        else{
            synchronized(DataBase.getSyncObj()){
                User currentUser = getCurrentUser();
                currentUser.setEmail(text);
                updateEmailMapFile(currentUser);
                updateUser(currentUser);
            }
            logger.info("edited email");
        }
    }
    private void editBirthdate(String text){
        try{
            int[] date = { Integer.parseInt(text.substring(0,4)), Integer.parseInt(text.substring(5,7)), Integer.parseInt(text.substring(8,10)) };
            LocalDate birthDate = LocalDate.of(date[0],date[1],date[2]);
            if(!birthDate.isEqual(LocalDate.of(0,1,1)) && (birthDate.isAfter(LocalDate.now()) || birthDate.isBefore(LocalDate.now().minusYears(200))))
                setDialog("InvalidDate");
            else{
                synchronized(DataBase.getSyncObj()){
                    User currentUser = getCurrentUser();
                    UserLogic.setBirthDateWithLocalDate(currentUser,birthDate);
                    updateUser(currentUser);
                }
                logger.info("edited birth date");
            }
        } catch(Exception e){
            setDialog("InvalidDate");
        }
    }
    private void hideBirthdate(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser();
            UserLogic.setBirthDateWithLocalDate(currentUser,LocalDate.of(0,1,1));
            updateUser(currentUser);
        }
        logger.info("edited birth date");
    }
    private Page gotoTweetsPage(User targetUser){
        return new TweetsPage(targetUser,this, getClientHandler());
    }
    private Page gotoListsPage(){
        return new ListsPage(this, getClientHandler());
    }
    private Page gotoRequestsPage(){
        return new RequestsPage(this, getClientHandler());
    }
    private Page gotoNotificationsPage(){
        return new NotificationsPage(this, getClientHandler());
    }
    private void follow(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser(), targetUser = getTargetUser();
            boolean hasAlreadySentFollowRequest = false;
            for(long[] request: targetUser.getIncomingRequests())
                if(request[0]==currentUser.getId() && request[1]==1){
                    hasAlreadySentFollowRequest = true;
                    break;
                }
            if(hasAlreadySentFollowRequest)
                setDialog("AlreadyRequested");
            else if(!UserLogic.canFollow(currentUser,targetUser.getId()))
                setDialog("OneOfYouHasBlockedTheOther");
            else if(currentUser.getFollowing().contains(targetUser.getId()))
                setDialog("AlreadyFollowing");
            else{
                if(!targetUser.isPublic()){
                    UserLogic.sendFollowRequest(currentUser,targetUser);
                    setDialog("RequestSent");
                }
                else{
                    UserLogic.follow(currentUser,targetUser);
                    setDialog("NowFollowing");
                }
                updateUser(targetUser);
                updateUser(currentUser);
            }
        }
    }
    private void toggleBan(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser(), targetUser = getTargetUser();
            if(currentUser.getBlackList().contains(targetUser.getId())){
                UserLogic.unblock(currentUser,targetUser);
                setDialog("Unblocked");
            }
            else{
                UserLogic.block(currentUser,targetUser);
                setDialog("Blocked");
            }
            updateUser(targetUser);
            updateUser(currentUser);
        }
    }
    private void toggleMute(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser(), targetUser = getTargetUser();
            if(currentUser.getMuted().contains(targetUser.getId())){
                UserLogic.unmute(currentUser,targetUser);
                setDialog("Unmuted");
            }
            else{
                UserLogic.mute(currentUser,targetUser);
                setDialog("Muted");
            }
            updateUser(targetUser);
            updateUser(currentUser);
        }
    }
    private void report(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser(), targetUser = getTargetUser();
            if(!currentUser.getReported().contains(targetUser.getId())){
                UserLogic.report(currentUser,targetUser);
                updateUser(targetUser);
                updateUser(currentUser);
            }
            else{
                setDialog("AlreadyReported");
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(PersonalPageRequest request) {
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+ Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        if(request.getCommand()==Command.home) return new MainMenuPage(getClientHandler());
        else if(request.getCommand()==Command.back) return getPreviousPage();
        if(isThisCurrentUsersPage()){
            switch(request.getCommand()){
                case changeProfilePicture-> changeProfilePicture(request.getImageData());
                case editBio-> editBio(request.getStringArgs()[0]);
                case editPhoneNumber-> editPhoneNumber(request.getStringArgs()[0]);
                case editEmail-> editEmail(request.getStringArgs()[0]);
                case editBirthDate-> editBirthdate(request.getStringArgs()[0]);
                case hideBirthDate-> hideBirthdate();
                case gotoTweetsPage ->{ return gotoTweetsPage(getTargetUser()); }
                case gotoListsPage ->{ return gotoListsPage(); }
                case gotoRequestsPage ->{ return gotoRequestsPage(); }
                case gotoNotificationsPage ->{ return gotoNotificationsPage(); }
            }
        }
        else{
            switch(request.getCommand()){
                case gotoTweetsPage ->{ return gotoTweetsPage(getTargetUser()); }
                case follow-> follow();
                case toggleBan-> toggleBan();
                case toggleMute-> toggleMute();
                case report-> report();
            }
        }
        return this;
    }

    @Override
    public Page update() {
        if(!isThisCurrentUsersPage()){
            User targetUser = getTargetUser();
            if(!targetUser.isOwned() || !targetUser.isActive() || getCurrentUser().getBlockedBy().contains(targetUser.getId()))
                return getPreviousPage();
        }
        return this;
    }

    @Override
    public Response getData() {
        String dialog,username,bio,firstName,lastName,birthdate=null,email=null,phoneNumber=null;
        boolean isThisCurrentUsersPage,canViewDetails,online=true;
        int[] lastSeen=null;
        byte[] profileImage;

        isThisCurrentUsersPage = isThisCurrentUsersPage();
        canViewDetails = true;
        dialog = getDialog();
        clearDialog();
        if(isThisCurrentUsersPage()){
            User user = getCurrentUser();
            profileImage = getImage(user.getPictureID());
            username = user.getUserName();
            bio = user.getBio();
            firstName = user.getFirstName();
            lastName = user.getLastName();
            birthdate = user.getBirthDate();
            email = user.getEmail();
            phoneNumber = user.getPhoneNumber();
        }
        else{
            User user =  getTargetUser();
            profileImage =  getImage(user.getPictureID());
            username = user.getUserName();
            bio = user.getBio();
            firstName = user.getFirstName();
            lastName = user.getLastName();
            canViewDetails =  user.getInfoVisibility() == 2 || (user.getInfoVisibility() == 1 && user.getFollowing().contains(getCurrentUser().getId()));
            if(canViewDetails){
                synchronized(getDataHolder().getOnlineUsers()){
                    if(!getDataHolder().getOnlineUsers().containsValue(user.getId())){
                        online = false;
                        lastSeen = user.getLastSeen();
                    }
                }
                birthdate = user.getBirthDate();
                email = user.getEmail();
                phoneNumber = user.getPhoneNumber();
            }
        }
        return new PersonalPageResponse(dialog,username,bio,firstName,lastName,birthdate,email,phoneNumber,isThisCurrentUsersPage,canViewDetails,online,lastSeen,profileImage);
    }
}
