package server.logic.model_logic;

import server.model.GroupMessage;

import java.util.LinkedList;

public class GroupMessageLogic {
    public static long showNext(long currentMessageID, LinkedList<Long> messages) throws IndexOutOfBoundsException{
        return messages.get(messages.indexOf(currentMessageID)+1);
    }
    public static long showPrevious(long currentMessageID, LinkedList<Long> messages) throws IndexOutOfBoundsException{
        return messages.get(messages.indexOf(currentMessageID)-1);
    }
    public static long getRepostedID(GroupMessage currentMessage){
        long repostedID = currentMessage.getRepostedFrom();
        if(repostedID==-1) repostedID = currentMessage.getSender();
        return repostedID;
    }
}
