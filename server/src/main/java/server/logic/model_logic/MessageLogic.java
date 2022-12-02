package server.logic.model_logic;

import server.model.Message;

import java.util.LinkedList;

public class MessageLogic {
    public static long[] showNext(Message currentMessage, LinkedList<Long> messages) throws IndexOutOfBoundsException{
        return new long[]{currentMessage.getSender(), currentMessage.getReceiver(), messages.get(messages.indexOf(currentMessage.getId())+1)};
    }
    public static long[] showPrevious(Message currentMessage, LinkedList<Long> messages) throws IndexOutOfBoundsException{
        return new long[]{currentMessage.getSender(), currentMessage.getReceiver(), messages.get(messages.indexOf(currentMessage.getId())-1)};
    }
    public static long getRepostedID(Message currentMessage){
        long repostedID = currentMessage.getRepostedFrom();
        if(repostedID==-1) repostedID = currentMessage.getSender();
        return repostedID;
    }
}
