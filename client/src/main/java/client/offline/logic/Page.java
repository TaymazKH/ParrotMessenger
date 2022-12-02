package client.offline.logic;

import client.page_data.PageData;
import shared.simple_model.SimpleUser;

public abstract class Page {
    private final Page previousPage;
    private static SimpleUser currentUser;
    private String dialog;

    public Page(Page previousPage) {
        this.previousPage = previousPage;
    }

//    public abstract Page runEvent(Event event);
    public abstract PageData getData();

    public Page getPreviousPage() {
        return previousPage;
    }
    public static SimpleUser getCurrentUser() {
        return currentUser;
    }
    public String getDialog() {
        return dialog;
    }

    public static void setCurrentUser(SimpleUser currentUser) {
        Page.currentUser = currentUser;
    }
    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
    public void clearDialog() {
        setDialog(null);
    }
}
