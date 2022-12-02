package client.offline.logic;

import client.offline.event.EventType;
import client.offline.event.MessagingPageEvent;
import client.page_data.MessagingPageData;
import client.page_data.PageData;
import shared.response.MessagingPageResponse;
import shared.simple_model.SimpleMessage;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class MessagingPage extends Page {
    private final String targetUser;
    private int section; // 1: saved messages / 2: pv
    private SimpleMessage currentMessage;

    public MessagingPage(Page previousPage, String targetUser) {
        super(previousPage);
        this.targetUser = targetUser;
        if(getCurrentUser().getUserName().equals(targetUser)) showSavedMessages();
        else messageTargetUser();
    }

    public LinkedList<SimpleMessage> getCurrentMessageList() {
        return getCurrentUser().getMessages().get(targetUser);
    }

    private void showSavedMessages(){
        section=1;
        try{
            currentMessage = getCurrentMessageList().getLast();
        } catch(Exception e){
            currentMessage = null;
        }
    }
    private void messageTargetUser(){
        section=2;
        try{
            currentMessage = getCurrentMessageList().getLast();
        } catch(NoSuchElementException e){
            currentMessage = null;
        }
    }
    private void showNext(){
        try{
            LinkedList<SimpleMessage> currentMessageList = getCurrentMessageList();
            currentMessage = currentMessageList.get(currentMessageList.indexOf(currentMessage)+1);
        } catch(Exception e){
            setDialog("CantShowNext");
        }
    }
    private void showPrevious(){
        try{
            LinkedList<SimpleMessage> currentMessageList = getCurrentMessageList();
            currentMessage = currentMessageList.get(currentMessageList.indexOf(currentMessage)-1);
        } catch(Exception e){
            setDialog("CantShowPrevious");
        }
    }

    //////////////////////////////////////////////////////////////////////////

    public Page runEvent(MessagingPageEvent event) {
        if(event.getEventType()==EventType.home) return new MainMenuPage();
        else if(event.getEventType()==EventType.back) return getPreviousPage();
        else if(currentMessage!=null){
            switch(event.getEventType()){
                case showNext-> showNext();
                case showPrevious-> showPrevious();
            }
        }
        return this;
    }

    @Override
    public PageData getData() {
        String dialog;

        dialog = getDialog();
        clearDialog();
        return new MessagingPageData(new MessagingPageResponse(currentMessage,dialog));
    }
}
