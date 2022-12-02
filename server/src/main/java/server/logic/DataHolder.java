package server.logic;

import server.database.DataBase;
import server.util.MessageTimer;

import java.security.SecureRandom;
import java.util.HashMap;

public class DataHolder {
    private volatile DataBase dataBase = new DataBase();
    private volatile HashMap<Integer,Long> onlineUsers = new HashMap<>();
    private volatile SecureRandom random = new SecureRandom();
    private volatile MessageTimer messageTimer = new MessageTimer(this){{start();}};

    public synchronized DataBase getDataBase() {
        return dataBase;
    }
    public synchronized HashMap<Integer, Long> getOnlineUsers() {
        return onlineUsers;
    }
    public synchronized SecureRandom getRandom() {
        return random;
    }
    public synchronized MessageTimer getMessageTimer() {
        return messageTimer;
    }
}
