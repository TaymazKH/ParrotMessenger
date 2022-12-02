package client.offline.logic;

import client.offline.event.EventType;
import client.offline.event.PersonalPageEvent;
import client.page_data.PageData;
import client.page_data.PersonalPageData;
import shared.response.PersonalPageResponse;
import shared.simple_model.SimpleUser;

public class PersonalPage extends Page {
    public PersonalPage(Page previousPage) {
        super(previousPage);
    }

    //////////////////////////////////////////////////////////////////////////

    public Page runEvent(PersonalPageEvent event) {
        if(event.getEventType()== EventType.home) return new MainMenuPage();
        else if(event.getEventType()==EventType.back) return getPreviousPage();
        else if(event.getEventType()==EventType.gotoTweetsPage) return new TweetsPage(this);
        return this;
    }

    @Override
    public PageData getData() {
        String dialog,username,bio,firstName,lastName,birthdate,email,phoneNumber;
        byte[] profileImage;

        dialog = getDialog();
        clearDialog();
        SimpleUser user = getCurrentUser();
        profileImage = user.getPicture();
        username = user.getUserName();
        bio = user.getBio();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        birthdate = user.getBirthDate();
        email = user.getEmail();
        phoneNumber = user.getPhoneNumber();
        return new PersonalPageData(new PersonalPageResponse(dialog,username,bio,firstName,lastName,birthdate,email,phoneNumber,true,true,false,null,profileImage));
    }
}
