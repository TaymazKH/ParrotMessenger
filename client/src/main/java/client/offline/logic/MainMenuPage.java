package client.offline.logic;

import client.offline.event.MainMenuPageEvent;
import client.page_data.MainMenuPageData;
import client.page_data.PageData;

public class MainMenuPage extends Page {
    public MainMenuPage() {
        super(null);
    }

    //////////////////////////////////////////////////////////////////////////

    public Page runEvent(MainMenuPageEvent event) {
        switch(event.getEventType()){
            case gotoPersonalPage->{ return new PersonalPage(this); }
            case gotoChatListPage->{ return new ChatListPage(this); }
            case gotoGroupsPage->{ return new GroupsPage(this); }
        }
        return this;
    }

    @Override
    public PageData getData() {
        return new MainMenuPageData();
    }
}
