package server.model;

import java.util.LinkedList;

public class Group {
    private String name;
    private long id, owner;
    private LinkedList<Long> members;
    private LinkedList<Long> messages;
    private boolean isActive;

    public Group(){}
    public Group(String name, long id, long owner){
        this.name = name;
        this.id = id;
        this.owner = owner;
        this.members = new LinkedList<>();
        this.members.add(owner);
        this.messages = new LinkedList<>();
        this.isActive = true;
    }

    public String getName() {
        return name;
    }
    public long getId() {
        return id;
    }
    public long getOwner() {
        return owner;
    }
    public LinkedList<Long> getMembers() {
        return members;
    }
    public LinkedList<Long> getMessages() {
        return messages;
    }
    public boolean isActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setOwner(long owner) {
        this.owner = owner;
    }
    public void setMembers(LinkedList<Long> members) {
        this.members = members;
    }
    public void setMessages(LinkedList<Long> messages) {
        this.messages = messages;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
