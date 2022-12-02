package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.ClientHandler;
import shared.request.ListsPageRequest;
import server.model.User;
import server.logic.model_logic.UserLogic;
import shared.response.ListsPageResponse;
import shared.response.Response;
import shared.util.Command;
import shared.util.PageType;

import java.util.Arrays;
import java.util.LinkedList;

public class ListsPage extends Page {
    private static final Logger logger = LogManager.getLogger(ListsPage.class);
    private int listType; // 0: following / 1: followers / 2: black list / 3: mute list
    private long thirdUserID;

    public ListsPage(Page previousPage, ClientHandler clientHandler) {
        super(PageType.lists, previousPage, clientHandler);
        setFieldsToDefaultValues();
        logger.info("entered the page");
    }

    public LinkedList<Long> getCurrentList() {
        LinkedList<Long> filteredList = new LinkedList<>(), list;
        synchronized(DataBase.getSyncObj()){
            switch(listType){
                case 0-> list = getCurrentUser().getFollowing();
                case 1-> list = getCurrentUser().getFollowers();
                case 2-> list = getCurrentUser().getBlackList();
                case 3-> list = getCurrentUser().getMuted();
                default -> throw new IllegalStateException("Unexpected value: " + listType);
            }
            for(long id: list){
                User user = getUser(id);
                if(user.isActive()) filteredList.add(id);
            }
        }
        return filteredList;
    }
    public long getThirdUserID() {
        return thirdUserID;
    }

    private void setFieldsToDefaultValues(){
        listType=-1;
        thirdUserID=-1;
    }
    private void showFollowings(){
        listType=0;
        logger.info("list type set to: 0 = followings");
    }
    private void showFollowers(){
        listType=1;
        logger.info("list type set to: 1 = followers");
    }
    private void showBlackList(){
        listType=2;
        logger.info("list type set to: 2 = black lists");
    }
    private void showMuteList(){
        listType=3;
        logger.info("list type set to: 3 = mute list");
    }
    private Page showTheirPage(){
        User thirdUser = getUser(thirdUserID);
        if(!thirdUser.isActive()){
            setDialog("AccountNotActive");
        }
        else if(getCurrentUser().getBlockedBy().contains(thirdUserID)){
            setDialog("YouHaveBeenBanned");
        }
        else{
            setFieldsToDefaultValues();
            return new PersonalPage(thirdUser,this, getClientHandler());
        }
        return this;
    }
    private void showNextID(){
        try{
            LinkedList<Long> currentList = getCurrentList();
            thirdUserID = currentList.get(currentList.indexOf(thirdUserID)+1);
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowNext");
        }
    }
    private void showPreviousID(){
        try{
            LinkedList<Long> currentList = getCurrentList();
            thirdUserID = currentList.get(currentList.indexOf(thirdUserID)-1);
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowPrevious");
        }
    }
    private void removeIDFromTargetUsersAndOwnList(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser(), thirdUser = getUser(thirdUserID);
            switch(listType){
                case 0->{
                    thirdUser.getFollowers().remove(currentUser.getId());
                    UserLogic.addNotification(thirdUser,currentUser.getId(),2);
                }
                case 1-> thirdUser.getFollowing().remove(currentUser.getId());
                case 2-> thirdUser.getBlockedBy().remove(currentUser.getId());
                case 3-> thirdUser.getMutedBy().remove(currentUser.getId());
            }
            removeIDFromOwnList(currentUser);
            updateUser(thirdUser);
            updateUser(currentUser);
        }
    }
    private void removeIDFromOwnList(User currentUser){
        LinkedList<Long> currentList = getCurrentList();
        long toRemove = thirdUserID;
        try{
            thirdUserID = currentList.get(currentList.indexOf(thirdUserID)+1);
        } catch(IndexOutOfBoundsException e1){
            try{
                thirdUserID = currentList.get(currentList.indexOf(thirdUserID)-1);
            } catch(IndexOutOfBoundsException e2){
                thirdUserID=-1;
            }
        }
        switch(listType){
            case 0-> currentUser.getFollowing().remove(toRemove);
            case 1-> currentUser.getFollowers().remove(toRemove);
            case 2-> currentUser.getBlackList().remove(toRemove);
            case 3-> currentUser.getMuted().remove(toRemove);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(ListsPageRequest request){
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+ Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        if(request.getCommand()==Command.home) return new MainMenuPage(getClientHandler());
        else if(request.getCommand()==Command.back){
            if(listType>-1) setFieldsToDefaultValues();
            else return getPreviousPage();
        }
        else if(listType==-1){
            switch(request.getCommand()){
                case showFollowings-> showFollowings();
                case showFollowers-> showFollowers();
                case showBlacklist-> showBlackList();
                case showMuteList-> showMuteList();
            }
            if(listType!=-1){
                try{ thirdUserID = getCurrentList().getFirst(); }
                catch(Exception e){ thirdUserID=-1; }
            }
        }
        else if(thirdUserID>-1){
            switch(request.getCommand()){
                case showTheirPage->{ return showTheirPage(); }
                case showNext-> showNextID();
                case showPrevious-> showPreviousID();
                case remove-> removeIDFromTargetUsersAndOwnList();
            }
        }
        else{
            setDialog("CantExecuteNothingToShow");
            logger.info("unable to run the command: thirdUserID field is -1");
        }
        return this;
    }

    @Override
    public Page update() {
        if(listType!=-1){
            LinkedList<Long> currentList = getCurrentList();
            if(!currentList.contains(thirdUserID)){
                try{ thirdUserID = currentList.getFirst(); }
                catch(Exception e){ thirdUserID=-1; }
            }
        }
        return this;
    }

    @Override
    public Response getData() {
        String dialog,nameText=null;
        int listType = this.listType;
        boolean isThereUserInList;
        byte[] profilePicture=null;

        isThereUserInList = getThirdUserID()>-1;
        dialog = getDialog();
        clearDialog();
        if(listType!=-1 && getThirdUserID()!=-1){
            User thirdUser = getUser(getThirdUserID());
            profilePicture = getImage(thirdUser.getPictureID());
            nameText = thirdUser.getUserName();
        }
        return new ListsPageResponse(dialog,nameText,listType,isThereUserInList,profilePicture);
    }
}
