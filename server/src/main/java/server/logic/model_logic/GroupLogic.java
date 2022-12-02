package server.logic.model_logic;

import java.util.LinkedList;

public class GroupLogic {
    public static long showNext(long id, LinkedList<Long> members) throws IndexOutOfBoundsException{
        return members.get(members.indexOf(id)+1);
    }
    public static long showPrevious(long id, LinkedList<Long> members) throws IndexOutOfBoundsException{
        return members.get(members.indexOf(id)-1);
    }
}
