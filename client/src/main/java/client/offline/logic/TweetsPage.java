package client.offline.logic;

import client.offline.event.EventType;
import client.offline.event.TweetsPageEvent;
import client.page_data.PageData;
import client.page_data.TweetsPageData;
import shared.response.TweetsPageResponse;
import shared.simple_model.SimpleTweet;

import java.util.NoSuchElementException;

public class TweetsPage extends Page {
    private SimpleTweet currentTweet;

    public TweetsPage(Page previousPage) {
        super(previousPage);
        initializePage();
    }

    private void initializePage(){
        try{
            currentTweet = getCurrentUser().getTweets().getLast();
        } catch(NoSuchElementException e){
            currentTweet = null;
        }
    }
    private void showNext(){
        try{
            currentTweet = getCurrentUser().getTweets().get(getCurrentUser().getTweets().indexOf(currentTweet)+1);
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowNext");
        }
    }
    private void showPrevious(){
        try{
            currentTweet = getCurrentUser().getTweets().get(getCurrentUser().getTweets().indexOf(currentTweet)-1);
        } catch(IndexOutOfBoundsException e){
            setDialog("CantShowPrevious");
        }
    }

    //////////////////////////////////////////////////////////////////////////

    public Page runEvent(TweetsPageEvent event) {
        if(event.getEventType()==EventType.home) return new MainMenuPage();
        else if(event.getEventType()==EventType.back) return getPreviousPage();
        else if(currentTweet!=null){
            switch(event.getEventType()){
                case showNext-> showNext();
                case showPrevious-> showPrevious();
            }
        }
        else{
            setDialog("CantExecuteNothingToShow");
        }
        return this;
    }

    @Override
    public PageData getData() {
        String dialog;

        dialog = getDialog();
        clearDialog();
        return new TweetsPageData(new TweetsPageResponse(currentTweet,dialog,true));
    }
}
