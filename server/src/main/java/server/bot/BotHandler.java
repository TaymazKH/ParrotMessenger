package server.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import server.database.DataBase;
import server.logic.DataHolder;
import server.logic.model_logic.TweetLogic;
import server.logic.model_logic.UserLogic;
import server.model.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class BotHandler extends Thread {
    private final long botID;
    private final String path,className;
    private final DataHolder dataHolder;
    private final DataBase dataBase;
    private Class<?> botClass;

    public BotHandler(long botID, String path, DataHolder dataHolder){
        File file = new File(path);
        this.botID = botID;
        this.path = "file:///"+file.getParentFile().getAbsolutePath()+"/";
        this.dataHolder = dataHolder;
        this.dataBase = dataHolder.getDataBase();
        this.className = file.getName().substring(0,file.getName().lastIndexOf("."));
    }

    @Override
    public void run() {
        try{
            while(true){
                try{ Thread.sleep(500); } catch(InterruptedException ignored){}
                botClass = new URLClassLoader(new URL[]{new URL(path)}).loadClass(className);
                botClass.getMethod("setFields",String.class).invoke(null,dataBase.getBotData(botID));
                answerMessage();
                try{ Thread.sleep(500); } catch(InterruptedException ignored){}
                answerGroupMessage();
                try{ Thread.sleep(500); } catch(InterruptedException ignored){}
                sendMessage();
                try{ Thread.sleep(500); } catch(InterruptedException ignored){}
                sendGroupMessage();
                try{ Thread.sleep(500); } catch(InterruptedException ignored){}
                sendComment();
                try{ Thread.sleep(500); } catch(InterruptedException ignored){}
                sendTweet();
                try{ Thread.sleep(500); } catch(InterruptedException ignored){}
                acceptInvite();
                try{ Thread.sleep(500); } catch(InterruptedException ignored){}
                dataBase.updateBotData(botID,(String) botClass.getMethod("getFields").invoke(null));
            }
        } catch(IOException | ReflectiveOperationException e){
            e.printStackTrace();
        }
    }

    private void answerMessage() throws ReflectiveOperationException, IOException {
        if((boolean) botClass.getMethod("invokeAnswerMessage").invoke(null)){
            Method method = botClass.getMethod("answerMessage", String.class, long.class);
            ObjectMapper objectMapper = new ObjectMapper();
            synchronized(DataBase.getSyncObj()){
                User user = dataBase.getUser(botID);
                for(Map.Entry<Long,Integer> m: user.getUnread().entrySet()){
                    Message[] messages = new Message[m.getValue()];
                    for(int i=m.getValue();i>0;i--){
                        messages[m.getValue()-i] = dataBase.getMessage(botID,m.getKey(),user.getMessages().get(m.getKey()).get(user.getMessages().get(m.getKey()).size()-i));
                    }
                    for(Message message: messages){
                        message.setSeen(4);
                        String[] ans = (String[]) method.invoke(null,message.getText(),m.getKey());
                        if(ans!=null){
                            String text = ans[0];
                            byte[] imageData = null;
                            if(ans[1]!=null) imageData = objectMapper.readValue(ans[1],byte[].class);
                            int seen;
                            synchronized(dataHolder.getOnlineUsers()){
                                if(dataHolder.getOnlineUsers().containsValue(m.getKey())) seen=3;
                                else seen=2;
                            }
                            Message newMessage = new Message(text,botID,m.getKey(),dataBase.generateNewMessageID(botID,m.getKey()),-1,seen,dataBase.saveNewImage(imageData));
                            UserLogic.addMessage(user,newMessage);
                            dataBase.updateUser(user);
                            User targetUser = dataBase.getUser(m.getKey());
                            UserLogic.addMessage(targetUser,newMessage);
                            dataBase.updateUser(targetUser);
                            dataBase.saveNewMessage(newMessage);
                        }
                        dataBase.updateMessage(message);
                    }
                    user.getUnread().replace(m.getKey(),0);
                    dataBase.updateUser(user);
                }
            }
        }
    }
    private void answerGroupMessage() throws ReflectiveOperationException, IOException {
        if((boolean) botClass.getMethod("invokeAnswerGroupMessage").invoke(null)){
            Method method = botClass.getMethod("answerGroupMessage", String.class, long.class, long.class, long.class);
            ObjectMapper objectMapper = new ObjectMapper();
            synchronized(DataBase.getSyncObj()){
                User user = dataBase.getUser(botID);
                for(long groupID: user.getGroups()){
                    long latest = user.getGroupSeen().get(groupID), newLatest = latest;
                    Group group = dataBase.getGroup(groupID);
                    ArrayList<Long> messages = new ArrayList<>(group.getMessages());
                    for(long messageID: messages){
                        if(latest>=messageID) continue;
                        GroupMessage message = dataBase.getGroupMessage(groupID,messageID);
                        if(message.getSender()==botID) continue;
                        String[] ans = (String[]) method.invoke(null,message.getText(),message.getSender(),groupID,group.getOwner());
                        if(ans!=null){
                            String text = ans[0];
                            byte[] imageData = null;
                            if(ans[1]!=null) imageData = objectMapper.readValue(ans[1],byte[].class);
                            GroupMessage newMessage = new GroupMessage(text,dataBase.generateNewGroupMessageID(groupID),groupID,botID,-1,dataBase.saveNewImage(imageData));
                            dataBase.saveNewGroupMessage(newMessage);
                            group.getMessages().add(newMessage.getId());
                            dataBase.updateGroup(group);
                        }
                        newLatest = Math.max(newLatest,messageID);
                    }
                    user.getGroupSeen().replace(groupID,newLatest);
                    dataBase.updateUser(user);
                }
            }
        }
    }
    private void sendMessage() throws ReflectiveOperationException, IOException {
        if((boolean) botClass.getMethod("invokeSendMessage").invoke(null)){
            Method method = botClass.getMethod("sendMessage", long.class);
            ObjectMapper objectMapper = new ObjectMapper();
            synchronized(DataBase.getSyncObj()){
                User user = dataBase.getUser(botID);
                for(Map.Entry<Long,LinkedList<Long>> m: user.getMessages().entrySet()){
                    String[] ans = (String[]) method.invoke(null,m.getKey());
                    if(ans!=null){
                        String text = ans[0];
                        byte[] imageData = null;
                        if(ans[1]!=null) imageData = objectMapper.readValue(ans[1],byte[].class);
                        int seen;
                        synchronized(dataHolder.getOnlineUsers()){
                            if(dataHolder.getOnlineUsers().containsValue(m.getKey())) seen=3;
                            else seen=2;
                        }
                        Message newMessage = new Message(text,botID,m.getKey(),dataBase.generateNewMessageID(botID,m.getKey()),-1,seen,dataBase.saveNewImage(imageData));
                        UserLogic.addMessage(user,newMessage);
                        dataBase.updateUser(user);
                        User targetUser = dataBase.getUser(m.getKey());
                        UserLogic.addMessage(targetUser,newMessage);
                        dataBase.updateUser(targetUser);
                        dataBase.saveNewMessage(newMessage);
                    }
                }
            }
        }
    }
    private void sendGroupMessage() throws ReflectiveOperationException, IOException {
        if((boolean) botClass.getMethod("invokeSendGroupMessage").invoke(null)){
            Method method = botClass.getMethod("sendGroupMessage", long.class);
            ObjectMapper objectMapper = new ObjectMapper();
            synchronized(DataBase.getSyncObj()){
                User user = dataBase.getUser(botID);
                for(long groupID: user.getGroups()){
                    String[] ans = (String[]) method.invoke(null,groupID);
                    if(ans!=null){
                        String text = ans[0];
                        byte[] imageData = null;
                        if(ans[1]!=null) imageData = objectMapper.readValue(ans[1],byte[].class);
                        GroupMessage newMessage = new GroupMessage(text,dataBase.generateNewGroupMessageID(groupID),groupID,botID,-1,dataBase.saveNewImage(imageData));
                        dataBase.saveNewGroupMessage(newMessage);
                        Group group = dataBase.getGroup(groupID);
                        group.getMessages().add(newMessage.getId());
                        dataBase.updateGroup(group);
                    }
                }
            }
        }
    }
    private void sendComment() throws ReflectiveOperationException, IOException {
        if((boolean) botClass.getMethod("invokeSendComment").invoke(null)){
            Method method = botClass.getMethod("sendComment", long.class, String.class, long.class);
            ObjectMapper objectMapper = new ObjectMapper();
            synchronized(DataBase.getSyncObj()){
                User user = dataBase.getUser(botID);
                ArrayList<String> usernames = new ArrayList<>(dataBase.getUsernames());
                for(String username: usernames){
                    User targetUser = dataBase.getUser(username);
                    if(!user.equals(targetUser) && UserLogic.requestsToViewTweetsRandomly(targetUser,user.getId())){
                        ArrayList<Long> tweets = new ArrayList<>(targetUser.getTweets());
                        for(long tweetID: tweets){
                            Tweet tweet = dataBase.getTweet(targetUser.getId(),tweetID);
                            String[] ans = (String[]) method.invoke(null,targetUser.getId(),tweet.getText(),tweetID);
                            if(ans!=null){
                                String text = ans[0];
                                byte[] imageData = null;
                                if(ans[1]!=null) imageData = objectMapper.readValue(ans[1],byte[].class);
                                Tweet comment = new Tweet(text,botID,targetUser.getId(),-1,dataBase.generateNewTweetID(targetUser.getId()),tweet,dataBase.saveNewImage(imageData));
                                TweetLogic.addComment(tweet,comment);
                                dataBase.saveNewTweet(comment);
                            }
                        }
                    }
                }
            }
        }
    }
    private void sendTweet() throws ReflectiveOperationException, IOException {
        if((boolean) botClass.getMethod("invokeSendTweet").invoke(null)){
            Method method = botClass.getMethod("sendTweet");
            ObjectMapper objectMapper = new ObjectMapper();
            synchronized(DataBase.getSyncObj()){
                User user = dataBase.getUser(botID);
                String[] ans = (String[]) method.invoke(null);
                if(ans!=null){
                    String text = ans[0];
                    byte[] imageData = null;
                    if(ans[1]!=null) imageData = objectMapper.readValue(ans[1],byte[].class);
                    Tweet tweet = new Tweet(text,botID,botID,-1,dataBase.generateNewTweetID(botID),null,dataBase.saveNewImage(imageData));
                    UserLogic.addTweet(user,tweet);
                    dataBase.saveNewTweet(tweet);
                    dataBase.updateUser(user);
                }
            }
        }
    }
    private void acceptInvite() throws ReflectiveOperationException {
        if((boolean) botClass.getMethod("acceptInvite").invoke(null)){
            synchronized (DataBase.getSyncObj()){
                User user = dataBase.getUser(botID);
                for(int i=0;i<user.getIncomingRequests().size();i++){
                    long[] request = user.getIncomingRequests().get(i);
                    if(request[1]!=2) continue;
                    User targetUser = dataBase.getUser(request[0]);
                    targetUser.getOutgoingRequests().remove(new long[]{user.getId(),2,request[2]});
                    user.getGroups().add(request[2]);
                    user.getGroupSeen().put(request[2],(long) -1);
                    Group group = dataBase.getGroup(request[2]);
                    group.getMembers().add(user.getId());
                    dataBase.updateGroup(group);
                    dataBase.updateUser(targetUser);
                    user.getIncomingRequests().remove(i);
                    i--;
                    dataBase.updateUser(user);
                }
            }
        }
    }
}

/*
    String getFields();
    void setFields(String data);

    String[] answerMessage(String text, long sender);
    String[] answerGroupMessage(String text, long sender, long group, long owner);
    String[] sendMessage(long receiver);
    String[] sendGroupMessage(long group);
    String[] sendComment(long user, String text, long tweet);
    String[] sendTweet();

    boolean invokeAnswerMessage();
    boolean invokeAnswerGroupMessage();
    boolean invokeSendMessage();
    boolean invokeSendGroupMessage();
    boolean invokeSendComment();
    boolean invokeSendTweet();
    boolean acceptInvite();
 */
