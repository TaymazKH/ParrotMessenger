package client.offline.logic;

import client.offline.event.ChatListPageEvent;
import client.offline.event.EventType;
import client.page_data.ChatListPageData;
import client.page_data.PageData;
import shared.response.ChatListPageResponse;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ChatListPage extends Page {
    private String thirdUsername;

    public ChatListPage(Page previousPage) {
        super(previousPage);
        backToChatList();
    }

    public LinkedList<String> getCurrentList() {
        return new LinkedList<>(getCurrentUser().getMessages().keySet());
    }

    private void backToChatList(){
        try{ thirdUsername = getCurrentList().getFirst(); }
        catch(NoSuchElementException e){ thirdUsername = null; }
    }
    private void showNext(){
        try{
            LinkedList<String> currentList = getCurrentList();
            thirdUsername = currentList.get(currentList.indexOf(thirdUsername)+1);
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowNext");
        }
    }
    private void showPrevious(){
        try{
            LinkedList<String> currentList = getCurrentList();
            thirdUsername = currentList.get(currentList.indexOf(thirdUsername)-1);
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowPrevious");
        }
    }
    private Page showSavedMessages(){
        return new MessagingPage(this,getCurrentUser().getUserName());
    }
    private Page messageUser(String text){
        if(text==null || text.isBlank()) setDialog("MustProvideName");
        else if(!getCurrentUser().getMessages().containsKey(text)) setDialog("UserNotFound");
        else return new MessagingPage(this,text);
        return this;
    }

    //////////////////////////////////////////////////////////////////////////

    public Page runEvent(ChatListPageEvent event) {
        if(event.getEventType()==EventType.home) return new MainMenuPage();
        else if(event.getEventType()==EventType.back) return getPreviousPage();
        else{
            if(thirdUsername!=null){
                switch(event.getEventType()){
                    case showNext-> showNext();
                    case showPrevious-> showPrevious();
                    case showSavesMessages->{ return showSavedMessages(); }
                    case messageUser->{ return messageUser(event.getTargetUsername()); }
                }
            }
        }
        return this;
    }

    @Override
    public PageData getData() {
        String dialog;

        dialog = getDialog();
        clearDialog();
        return new ChatListPageData(new ChatListPageResponse(dialog,null,thirdUsername,0,0,null));
    }
}
