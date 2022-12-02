package server.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.DataHolder;
import server.model.User;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class BotStarter {
    private static final Logger logger = LogManager.getLogger(BotStarter.class);
    private final DataHolder dataHolder;

    public BotStarter(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    public void initializeAll(){
        HashMap<Long,String> map = dataHolder.getDataBase().getBotConfigMap();
        for(Map.Entry<Long,String> m: map.entrySet())
            initializeOne(m.getKey(),m.getValue());
        logger.info("initialized "+map.size()+" bot(s)");
    }

    public void initializeOne(long id, String path){
        synchronized(dataHolder.getOnlineUsers()){
            dataHolder.getOnlineUsers().put(-1,id);
        }
        new BotHandler(id,path,dataHolder).start();
    }

    public String check(String path){
        try{
            File file = new File(path);
            URL url = new URL("file:///"+file.getParentFile().getAbsolutePath()+"/");
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
            Class<?> botClass = classLoader.loadClass(file.getName().substring(0,file.getName().lastIndexOf(".")));
            checkClassValidity(botClass);
            add(file);
        } catch(MalformedURLException|ClassNotFoundException e){
            return "InvalidClassPath";
        } catch(NoSuchMethodException e){
            return "InvalidClassStructure";
        }
        return "ValidClassStructure";
    }

    private void checkClassValidity(Class<?> botClass) throws NoSuchMethodException {
        botClass.getMethod("getFields");
        botClass.getMethod("setFields",String.class);
        
        botClass.getMethod("answerMessage", String.class, long.class);
        botClass.getMethod("answerGroupMessage", String.class, long.class, long.class, long.class);
        botClass.getMethod("sendMessage", long.class);
        botClass.getMethod("sendGroupMessage", long.class);
        botClass.getMethod("sendComment", long.class, String.class, long.class);
        botClass.getMethod("sendTweet");
        
        botClass.getMethod("invokeAnswerMessage");
        botClass.getMethod("invokeAnswerGroupMessage");
        botClass.getMethod("invokeSendMessage");
        botClass.getMethod("invokeSendGroupMessage");
        botClass.getMethod("invokeSendComment");
        botClass.getMethod("invokeSendTweet");
        botClass.getMethod("acceptInvite");
    }

    private void add(File file){
        DataBase dataBase = dataHolder.getDataBase();
        String username = file.getName().substring(0,file.getName().lastIndexOf("."));
        long id;
        synchronized(DataBase.getSyncObj()){
            while(dataBase.getUsernames().contains(username)) username = "-"+username;
            id = dataBase.generateNewUserID();
            User user = new User(username,id);
            dataBase.saveNewBotData(id);
            dataBase.saveNewUser(user);
            dataBase.updateIDUsernameMapFiles(user);
            dataBase.updateEmailMapFile(user);
            dataBase.updatePhoneNumberMapFile(user);
            dataBase.updateBotConfigMapFile(id,file.getAbsolutePath());
        }
        initializeOne(id,file.getAbsolutePath());
    }
}
