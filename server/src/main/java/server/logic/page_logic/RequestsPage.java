package server.logic.page_logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.ClientHandler;
import server.model.Group;
import server.model.User;
import server.logic.model_logic.UserLogic;
import shared.request.RequestsPageRequest;
import shared.response.RequestsPageResponse;
import shared.response.Response;
import shared.util.Command;
import shared.util.PageType;

import java.util.Arrays;
import java.util.LinkedList;

public class RequestsPage extends Page {
    private static final Logger logger = LogManager.getLogger(RequestsPage.class);
    private int listType; // 0: incoming / 1: outgoing
    private long[] itemInList;

    public RequestsPage(Page previousPage, ClientHandler clientHandler) {
        super(PageType.requests, previousPage, clientHandler);
        setFieldsToDefaultValues();
        logger.info("entered the page");
    }

    public int getListType() {
        return listType;
    }
    public LinkedList<long[]> getCurrentList() {
        if(listType==0) return getCurrentUser().getIncomingRequests();
        else return getCurrentUser().getOutgoingRequests();
    }
    public long[] getItemInList() {
        return itemInList;
    }

    private void setFieldsToDefaultValues(){
        listType=-1;
        itemInList=null;
    }
    private void showIncomingRequests(){
        listType=0;
    }
    private void showOutgoingRequests(){
        listType=1;
    }
    private Page showTheirPage(){
        User thirdUser = getUser(itemInList[0]);
        if(!thirdUser.isActive()){
            setDialog("AccountNotActive");
        }
        else if(getCurrentUser().getBlockedBy().contains(itemInList[0])){
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
            LinkedList<long[]> currentList = getCurrentList();
            for(int i=0;i<currentList.size();i++){
                long[] request = currentList.get(i);
                if(Arrays.equals(request,itemInList)){
                    itemInList = currentList.get(i+1);
                    break;
                }
            }
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowNext");
        }
    }
    private void showPreviousID(){
        try{
            LinkedList<long[]> currentList = getCurrentList();
            for(int i=0;i<currentList.size();i++){
                long[] request = currentList.get(i);
                if(Arrays.equals(request,itemInList)){
                    itemInList = currentList.get(i-1);
                    break;
                }
            }
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowPrevious");
        }
    }
    private void accept(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser(), thirdUser = getUser(itemInList[0]);
            if(itemInList[1]==1){
                removeIDFromThirdUsersOutgoingList(currentUser,thirdUser);
                thirdUser.getFollowing().add(currentUser.getId());
                currentUser.getFollowers().add(itemInList[0]);
            }
            else{
                removeIDFromThirdUsersOutgoingList(currentUser,thirdUser);
                currentUser.getGroups().add(itemInList[2]);
                currentUser.getGroupSeen().put(itemInList[2],(long) -1);
                Group group = getGroup(itemInList[2]);
                group.getMembers().add(currentUser.getId());
                updateGroup(group);
            }
            updateUser(thirdUser);
            removeIDFromOwnList(currentUser);
            updateUser(currentUser);
        }
    }
    private void decline(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser(), thirdUser = getUser(itemInList[0]);
            removeIDFromThirdUsersOutgoingList(currentUser,thirdUser);
            if(itemInList[1]==1){
                UserLogic.addNotification(thirdUser,currentUser.getId(),3);
            }
            else{
                UserLogic.addNotification(thirdUser,currentUser.getId(),4);
            }
            updateUser(thirdUser);
            removeIDFromOwnList(currentUser);
            updateUser(currentUser);
        }
    }
    private void silentDecline(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser(), thirdUser = getUser(itemInList[0]);
            removeIDFromThirdUsersOutgoingList(currentUser,thirdUser);
            updateUser(thirdUser);
            removeIDFromOwnList(currentUser);
            updateUser(currentUser);
        }
    }
    private void cancelRequest(){
        synchronized(DataBase.getSyncObj()){
            User currentUser = getCurrentUser(), thirdUser = getUser(itemInList[0]);
            removeIDFromThirdUsersIncomingList(currentUser,thirdUser);
            updateUser(thirdUser);
            removeIDFromOwnList(currentUser);
            updateUser(currentUser);
        }
    }
    private void removeIDFromThirdUsersIncomingList(User currentUser, User thirdUser){
        if(itemInList[1]==1){
            for(int i=0;i<thirdUser.getIncomingRequests().size();i++){
                long[] request = thirdUser.getIncomingRequests().get(i);
                if(Arrays.equals(request,new long[]{currentUser.getId(),1})){
                    thirdUser.getIncomingRequests().remove(i);
                    break;
                }
            }
        }
        else{
            for(int i=0;i<thirdUser.getIncomingRequests().size();i++){
                long[] request = thirdUser.getIncomingRequests().get(i);
                if(Arrays.equals(request,new long[]{currentUser.getId(),2,itemInList[2]})){
                    thirdUser.getIncomingRequests().remove(i);
                    break;
                }
            }
        }
    }
    private void removeIDFromThirdUsersOutgoingList(User currentUser, User thirdUser){
        if(itemInList[1]==1){
            for(int i=0;i<thirdUser.getOutgoingRequests().size();i++){
                long[] request = thirdUser.getOutgoingRequests().get(i);
                if(Arrays.equals(request,new long[]{currentUser.getId(),1})){
                    thirdUser.getOutgoingRequests().remove(i);
                    break;
                }
            }
        }
        else{
            for(int i=0;i<thirdUser.getOutgoingRequests().size();i++){
                long[] request = thirdUser.getOutgoingRequests().get(i);
                if(Arrays.equals(request,new long[]{currentUser.getId(),2,itemInList[2]})){
                    thirdUser.getOutgoingRequests().remove(i);
                    break;
                }
            }
        }
    }
    private void removeIDFromOwnList(User currentUser){
        long[] toRemove = itemInList;
        showNextID();
        if(getDialog()!=null && !getDialog().isEmpty()){
            clearDialog();
            showPreviousID();
            if(getDialog()!=null && !getDialog().isEmpty()){
                clearDialog();
                itemInList = null;
            }
        }
        switch(listType){
            case 0->{
                for(int i=0;i<currentUser.getIncomingRequests().size();i++){
                    long[] request = currentUser.getIncomingRequests().get(i);
                    if(Arrays.equals(request,toRemove)){
                        currentUser.getIncomingRequests().remove(i);
                        break;
                    }
                }
            }
            case 1->{
                for(int i=0;i<currentUser.getOutgoingRequests().size();i++){
                    long[] request = currentUser.getOutgoingRequests().get(i);
                    if(Arrays.equals(request,new long[]{currentUser.getId(),1})){
                        currentUser.getOutgoingRequests().remove(i);
                        break;
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Page runCommand(RequestsPageRequest request) {
        logger.info("user "+getCurrentID()+" issued command "+request.getCommand()+" with args "+ Arrays.toString(request.getStringArgs())+" / has image data: "+(request.getImageData()!=null));
        if(request.getCommand()==Command.home) return new MainMenuPage(getClientHandler());
        else if(request.getCommand()==Command.back){
            if(listType>-1) setFieldsToDefaultValues();
            else return getPreviousPage();
        }
        else if(listType==-1){
            switch(request.getCommand()){
                case showIncomingRequests-> showIncomingRequests();
                case showOutgoingRequests-> showOutgoingRequests();
            }
            if(listType!=-1){
                try{ itemInList = getCurrentList().getFirst(); }
                catch(Exception e){ itemInList = null; }
            }
        }
        else if(itemInList!=null){
            switch(request.getCommand()){
                case showTheirPage->{ return showTheirPage(); }
                case showNext-> showNextID();
                case showPrevious-> showPreviousID();
                case accept-> accept();
                case decline-> decline();
                case silentDecline-> silentDecline();
                case cancelRequest-> cancelRequest();
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
            LinkedList<long[]> currentList = getCurrentList();
            for(int i=0;i<currentList.size();i++){
                if(Arrays.equals(currentList.get(i),itemInList)) return this;
            }
            try{ itemInList = currentList.getFirst(); }
            catch(Exception e){ itemInList = null; }
        }
        return this;
    }

    @Override
    public Response getData() {
        String dialog,username=null,groupName=null;
        int listType;
        boolean isThereItemInList;
        long[] itemInList;
        byte[] profileImage=null;

        listType = getListType();
        itemInList = getItemInList();
        isThereItemInList = itemInList!=null;
        dialog = getDialog();
        clearDialog();
        if(listType!=-1){
            if(itemInList!=null){
                User thirdUser = getUser(itemInList[0]);
                username = thirdUser.getUserName();
                profileImage = getImage(thirdUser.getPictureID());
                switch(listType){
                    case 0->{
                        if(itemInList[1]==2) groupName = getGroup(getItemInList()[2]).getName();
                    }
                    case 1->{
                        if(getItemInList()[1]==2) groupName = getGroup(getItemInList()[2]).getName();
                    }
                }
            }
        }
        return new RequestsPageResponse(dialog,username,groupName,listType,isThereItemInList,itemInList,profileImage);
    }
}
