package client.page_data;

import client.util.PageDataType;
import client.util.ViewTexts;
import shared.response.RequestsPageResponse;

public class RequestsPageData implements PageData {
    private String dialog,listTypeText,requestText;
    private int listType;
    private boolean isThereItemInList;
    private byte[] profileImage;

    public RequestsPageData(RequestsPageResponse response) {
        listType = response.getListType();
        isThereItemInList = response.isThereItemInList();
        dialog = response.getDialog();
        if(listType!=-1){
            switch(listType){
                case 0-> listTypeText = ViewTexts.getIncomingRequestsTitle();
                case 1-> listTypeText = ViewTexts.getOutgoingRequestsTitle();
            }
            if(response.getItemInList()==null) requestText = ViewTexts.getListIsEmpty();
            else{
                profileImage = response.getProfileImage();
                switch(listType){
                    case 0->{
                        if(response.getItemInList()[1]==1)
                            requestText = response.getUsername()+" "+ViewTexts.getWantsToFollow();
                        else requestText = response.getUsername()+" "+ViewTexts.getInvitedYou()+response.getGroupName();
                    }
                    case 1->{
                        if(response.getItemInList()[1]==1)
                            requestText = ViewTexts.getRequestedToFollow()+response.getUsername();
                        else requestText = ViewTexts.getYouHaveInvited()+response.getUsername()+" "+ViewTexts.getToJoin()+response.getGroupName();
                    }
                }
            }
        }
    }

    public String getDialog() {
        return dialog;
    }

    public String getListTypeText() {
        return listTypeText;
    }

    public String getRequestText() {
        return requestText;
    }

    public int getListType() {
        return listType;
    }

    public boolean isThereItemInList() {
        return isThereItemInList;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.requests;
    }
}
