package client.offline.logic;

import client.offline.event.EventType;
import client.offline.event.GroupsPageEvent;
import client.page_data.GroupsPageData;
import client.page_data.PageData;
import shared.response.GroupsPageResponse;
import shared.simple_model.SimpleGroupMessage;

import java.util.LinkedList;

public class GroupsPage extends Page {
    private int section; // 0: groups list / 1: messages / 2: members
    private SimpleGroupMessage currentMessage;
    private String groupName;

    public GroupsPage(Page previousPage) {
        super(previousPage);
        backToGroupsList();
    }

    private LinkedList<String> getGroups(){
        return new LinkedList<>(getCurrentUser().getGroupMessages().keySet());
    }

    private void backToGroupsList(){
        section=0;
        currentMessage = null;
        try{ groupName = getGroups().getFirst(); }
        catch(Exception e){ groupName = null; }
    }
    private void backToMessages(){
        section=1;
    }
    private void showNext(){
        try{
            switch(section){
                case 0-> groupName = getGroups().get(getGroups().indexOf(groupName)+1);
                case 1-> currentMessage = getCurrentUser().getGroupMessages().get(groupName).get(getCurrentUser().getGroupMessages().get(groupName).indexOf(currentMessage)+1);
            }
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowNext");
        }
    }
    private void showPrevious(){
        try{
            switch(section){
                case 0-> groupName = getGroups().get(getGroups().indexOf(groupName)-1);
                case 1-> currentMessage = getCurrentUser().getGroupMessages().get(groupName).get(getCurrentUser().getGroupMessages().get(groupName).indexOf(currentMessage)-1);
            }
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowPrevious");
        }
    }
    private void enter(){
        section=1;
        try{ currentMessage = getCurrentUser().getGroupMessages().get(groupName).getLast(); }
        catch(Exception e){ currentMessage = null; }
    }

    //////////////////////////////////////////////////////////////////////////

    public Page runEvent(GroupsPageEvent event) {
        if(event.getEventType()==EventType.home) return new MainMenuPage();
        else if(event.getEventType()==EventType.back){
            if(section==1) backToGroupsList();
            else return getPreviousPage();
        }
        else if(section==0){
            if(groupName!=null){
                switch(event.getEventType()){
                    case showNext-> showNext();
                    case showPrevious-> showPrevious();
                    case enterGroup-> enter();
                }
            }
            else setDialog("NotInAnyGroup");
        }
        else{
            if(currentMessage!=null){
                switch(event.getEventType()){
                    case showNext-> showNext();
                    case showPrevious-> showPrevious();
                }
            }
            else setDialog("CantExecuteNothingToShow");
        }
        return this;
    }

    @Override
    public PageData getData() {
        String dialog;

        dialog = getDialog();
        clearDialog();
        return new GroupsPageData(new GroupsPageResponse(currentMessage,dialog,groupName,null,null,section,false,null));
    }
}
