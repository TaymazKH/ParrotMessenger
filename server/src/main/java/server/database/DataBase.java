package server.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.model.*;
import server.util.Config;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DataBase {
    private static final Logger logger = LogManager.getLogger(DataBase.class);
    private static final Object syncObj = new Object();
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final File usersDirectory,tweetsDirectory,messagesDirectory,groupsDirectory,groupMessagesDirectory,imagesDirectory, botsDirectory,
            IDMapFile,usernameMapFile,phoneNumberMapFile,emailMapFile,botConfigMapFile;

    public DataBase(){
        Config config = Config.getConfig("databaseDirectories");
        usersDirectory = new File(config.getProperty(String.class,"usersDirectory"));
        tweetsDirectory = new File(config.getProperty(String.class,"tweetsDirectory"));
        messagesDirectory = new File(config.getProperty(String.class,"messagesDirectory"));
        groupsDirectory = new File(config.getProperty(String.class,"groupsDirectory"));
        groupMessagesDirectory = new File(config.getProperty(String.class,"groupMessagesDirectory"));
        imagesDirectory = new File(config.getProperty(String.class,"imagesDirectory"));
        botsDirectory = new File(config.getProperty(String.class,"botsDirectory"));
        IDMapFile = new File(config.getProperty(String.class,"IDMapFile"));
        usernameMapFile = new File(config.getProperty(String.class,"usernameMapFile"));
        phoneNumberMapFile = new File(config.getProperty(String.class,"phoneNumberMapFile"));
        emailMapFile = new File(config.getProperty(String.class,"emailMapFile"));
        botConfigMapFile = new File(config.getProperty(String.class,"botConfigMapFile"));
    }

    /////////////////////////////////////////////////////////////////////////

    public static Object getSyncObj() {
        return syncObj;
    }

    public synchronized HashMap<Long,String> getIDMap(){
        try {
            if(IDMapFile.createNewFile()){
                logger.info("returned new username/id map");
                return new HashMap<>();
            }
            else{
                HashMap<Long,String> map = objectMapper.readValue(IDMapFile, new TypeReference<HashMap<Long,String>>(){});
                logger.info("loaded and returned username/id map");
                return map;
            }
        } catch (IOException e) {
            logger.error("unable to get username/id map");
            return new HashMap<>();
        }
    }
    public synchronized HashMap<String,Long> getUsernameMap(){
        try {
            if(usernameMapFile.createNewFile()){
                logger.info("returned new id/username map");
                return new HashMap<>();
            }
            else{
                HashMap<String,Long> map = objectMapper.readValue(usernameMapFile, new TypeReference<HashMap<String,Long>>(){});
                logger.info("loaded and returned new id/username map");
                return map;
            }
        } catch (IOException e) {
            logger.error("unable to get id/username map");
            return new HashMap<>();
        }
    }
    public synchronized HashMap<Long,String> getPhoneNumberMap(){
        try {
            if(phoneNumberMapFile.createNewFile()){
                logger.info("returned new id/phoneNumber map");
                return new HashMap<>();
            }
            else{
                HashMap<Long,String> map = objectMapper.readValue(phoneNumberMapFile, new TypeReference<HashMap<Long,String>>(){});
                logger.info("loaded and returned id/phoneNumber map");
                return map;
            }
        } catch (IOException e) {
            logger.error("unable to get id/phoneNumber map");
            return new HashMap<>();
        }
    }
    public synchronized HashMap<Long,String> getEmailMap(){
        try {
            if(emailMapFile.createNewFile()){
                logger.info("returned new id/email map");
                return new HashMap<>();
            }
            else{
                HashMap<Long,String> map = objectMapper.readValue(emailMapFile, new TypeReference<HashMap<Long,String>>(){});
                logger.info("loaded and returned id/email map");
                return map;
            }
        } catch (IOException e) {
            logger.error("unable to get id/email map");
            return new HashMap<>();
        }
    }
    public synchronized HashMap<Long,String> getBotConfigMap(){
        try {
            if(botConfigMapFile.createNewFile()){
                logger.info("returned new id/class_path map");
                return new HashMap<>();
            }
            else{
                HashMap<Long,String> map = objectMapper.readValue(botConfigMapFile, new TypeReference<HashMap<Long,String>>(){});
                logger.info("loaded and returned id/class_path map");
                return map;
            }
        } catch (IOException e) {
            logger.error("unable to get id/class_path map");
            return new HashMap<>();
        }
    }
    public synchronized void updateIDUsernameMapFiles(User user){
        HashMap<Long,String> IDMap = getIDMap();
        HashMap<String,Long> usernameMap = getUsernameMap();
        IDMap.put(user.getId(),user.getUserName());
        if(!user.isOwned()){
            String oldUsername = null;
            for(Map.Entry<String,Long> m: usernameMap.entrySet()){
                if(m.getValue()==user.getId()){
                    oldUsername = m.getKey();
                    break;
                }
            }
            usernameMap.remove(oldUsername);
        }
        usernameMap.put(user.getUserName(),user.getId());
        try {
            objectMapper.writeValue(new FileWriter(IDMapFile),IDMap);
            objectMapper.writeValue(new FileWriter(usernameMapFile),usernameMap);
            logger.info("updated username/id map files");
        } catch (IOException e) {
            logger.error("unable to update username/id map files");
            e.printStackTrace();
        }
    }
    public synchronized void updatePhoneNumberMapFile(User user){
        HashMap<Long,String> phoneNumberMap = getPhoneNumberMap();
        phoneNumberMap.put(user.getId(),user.getPhoneNumber());
        try {
            objectMapper.writeValue(new FileWriter(phoneNumberMapFile),phoneNumberMap);
            logger.info("updated id/phoneNumber map files");
        } catch (IOException e) {
            logger.error("unable to update id/phoneNumber map files");
            e.printStackTrace();
        }
    }
    public synchronized void updateEmailMapFile(User user){
        HashMap<Long,String> emailMap = getEmailMap();
        emailMap.put(user.getId(),user.getEmail());
        try {
            objectMapper.writeValue(new FileWriter(emailMapFile),emailMap);
            logger.info("updated id/email map files");
        } catch (IOException e) {
            logger.error("unable to update id/email map files");
            e.printStackTrace();
        }
    }
    public synchronized void updateBotConfigMapFile(long id, String path){
        HashMap<Long,String> botMap = getBotConfigMap();
        botMap.put(id,path);
        try {
            objectMapper.writeValue(new FileWriter(botConfigMapFile),botMap);
            logger.info("updated id/class_path map files");
        } catch (IOException e) {
            logger.error("unable to update id/class_path map files");
            e.printStackTrace();
        }
    }

    public synchronized User getUser(String username){
        if(!getUsernameMap().containsKey(username)){
            logger.info("no user with name: "+username+" - returning null");
            return null;
        }
//        logger.info("loaded and returned user: "+getUsernameMap().get(username));
        return getUser(getUsernameMap().get(username));
    }
    public synchronized User getUser(long id){
        try{
            if(id==-1) return null;
            User user = objectMapper.readValue(new File(usersDirectory.getPath()+"/"+id+".json"),User.class);
            //logger.info("loaded and returned user: "+id);
            return user;
        } catch(IOException e){
            logger.error("unable to get user: "+id);
            //e.printStackTrace();
            return null;
        }
    }
    public synchronized LinkedList<String> getUsernames(){
        LinkedList<String> usernames = new LinkedList<>();
        for(Map.Entry<String,Long> m: getUsernameMap().entrySet())
            usernames.add(m.getKey());
        logger.info("loaded and returned username list with "+usernames.size()+" elements in it");
        return usernames;
    }
    public synchronized String getUsername(long id){
        logger.info("loaded and returned username of user: "+id);
        if(id==-1) return null;
        return getIDMap().get(id);
    }
    public synchronized void saveNewUser(User user){
        try{
            objectMapper.writeValue(new FileWriter(usersDirectory.getPath()+"/"+user.getId()+".json"),user);
            logger.info("created new file for user: "+user.getId());
        } catch(IOException e){
            logger.error("unable to create new file for user: "+user.getId());
            e.printStackTrace();
        }
    }
    public synchronized void updateUser(User user){
        try{
            objectMapper.writeValue(new FileWriter(usersDirectory.getPath()+"/"+user.getId()+".json"),user);
//            logger.info("updated user data of: "+user.getId());
        } catch(IOException e){
            logger.error("unable to update user data of: "+user.getId());
            e.printStackTrace();
        }
    }

    public synchronized Tweet getTweet(long mainSender,long id){
        try{
            if(id==-1 || id==0) return null;
            Tweet tweet = objectMapper.readValue(new File(tweetsDirectory.getPath()+"/"+mainSender+"/"+id+".json"),Tweet.class);
            //logger.info("loaded and returned tweet: "+mainSender+"/"+id);
            return tweet;
        } catch(IOException e){
            logger.error("unable to get tweet: "+mainSender+"/"+id);
            e.printStackTrace();
            return null;
        }
    }
    public synchronized void saveNewTweet(Tweet tweet){
        try{
            objectMapper.writeValue(new FileWriter(tweetsDirectory.getPath()+"/"+tweet.getMainSender()+"/"+tweet.getId()+".json"),tweet);
            logger.info("created new file for tweet: "+tweet.getMainSender()+"/"+tweet.getId());
            if(!tweet.isPrimaryTweet()){
                Tweet upperTweet = getTweet(tweet.getMainSender(),tweet.getUpperTweetID());
                upperTweet.getComments().add(tweet.getId());
                updateTweet(upperTweet);
            }
        } catch(IOException e){
            logger.error("unable to create new file for tweet: "+tweet.getMainSender()+"/"+tweet.getId());
            e.printStackTrace();
        }
    }
    public synchronized void updateTweet(Tweet tweet){
        try {
            objectMapper.writeValue(new FileWriter(tweetsDirectory.getPath()+"/"+tweet.getMainSender()+"/"+tweet.getId()+".json"),tweet);
            logger.info("updated tweet data of: "+tweet.getMainSender()+"/"+tweet.getId());
        } catch (IOException e) {
            logger.error("unable to update tweet data of: "+tweet.getMainSender()+"/"+tweet.getId());
            e.printStackTrace();
        }
    }

    public synchronized Message getMessage(long sender,long receiver,long id){
        try{
            if(id==-1) return null;
            File directory1 = new File(messagesDirectory.getPath()+"/"+sender+"-"+receiver);
            File directory2 = new File(messagesDirectory.getPath()+"/"+receiver+"-"+sender);
            Message message;
            if(directory1.exists()){
                message = objectMapper.readValue(new File(directory1.getPath()+"/"+id+".json"),Message.class);
                logger.info("loaded and returned message: "+sender+"-"+receiver+"/"+id);
            }
            else{
                message = objectMapper.readValue(new File(directory2.getPath()+"/"+id+".json"),Message.class);
                logger.info("loaded and returned message: "+receiver+"-"+sender+"/"+id);
            }
            return message;
        } catch(IOException e){
            logger.error("unable to load message: "+sender+"-"+receiver+"/"+id+" nor "+receiver+"-"+sender+"/"+id);
            //e.printStackTrace();
            return null;
        }
    }
    public synchronized void saveNewMessage(Message message){
        try{
            File directory1 = new File(messagesDirectory.getPath()+"/"+message.getSender()+"-"+message.getReceiver());
            File directory2 = new File(messagesDirectory.getPath()+"/"+message.getReceiver()+"-"+message.getSender());
            if(directory1.exists()){
                objectMapper.writeValue(new FileWriter(directory1.getPath() + "/" + message.getId() + ".json"), message);
                logger.info("created new file for message: "+message.getSender()+"-"+message.getReceiver()+"/"+message.getId());
            }
            else{
                objectMapper.writeValue(new FileWriter(directory2.getPath() + "/" + message.getId() + ".json"), message);
                logger.info("created new file for message: "+message.getReceiver()+"-"+message.getSender()+"/"+message.getId());
            }
        } catch(IOException e){
            logger.error("unable to create new file for message: "+message.getSender()+"-"+message.getReceiver()+"/"+message.getId()+" nor "+message.getReceiver()+"-"+message.getSender()+"/"+message.getId());
            e.printStackTrace();
        }
    }
    public synchronized void updateMessage(Message message){
        try{
            File directory1 = new File(messagesDirectory.getPath()+"/"+message.getSender()+"-"+message.getReceiver());
            File directory2 = new File(messagesDirectory.getPath()+"/"+message.getReceiver()+"-"+message.getSender());
            if(directory1.exists()){
                objectMapper.writeValue(new FileWriter(directory1.getPath() + "/" + message.getId() + ".json"), message);
                logger.info("updated message data of: "+message.getSender()+"-"+message.getReceiver()+"/"+message.getId());
            }
            else{
                objectMapper.writeValue(new FileWriter(directory2.getPath() + "/" + message.getId() + ".json"), message);
                logger.info("updated message data of: "+message.getReceiver()+"-"+message.getSender()+"/"+message.getId());
            }
        } catch(IOException e){
            logger.error("unable to update message data of: "+message.getSender()+"-"+message.getReceiver()+"/"+message.getId()+" nor "+message.getReceiver()+"-"+message.getSender()+"/"+message.getId());
            e.printStackTrace();
        }
    }

    public synchronized Group getGroup(long id){
        try{
            if(id==-1) return null;
            Group group = objectMapper.readValue(new File(groupsDirectory.getPath()+"/"+id+".json"),Group.class);
            //logger.info("loaded and returned group: "+id);
            return group;
        } catch(Exception e){
            logger.error("unable to get group: "+id);
            e.printStackTrace();
            return null;
        }
    }
    public synchronized void saveNewGroup(Group group){
        try{
            objectMapper.writeValue(new FileWriter(groupsDirectory.getPath()+"/"+group.getId()+".json"),group);
            logger.info("created new file for group: "+group.getId());
        } catch(IOException e){
            logger.error("unable to create new file for group: "+group.getId());
            e.printStackTrace();
        }
    }
    public synchronized void updateGroup(Group group){
        try{
            objectMapper.writeValue(new FileWriter(groupsDirectory.getPath()+"/"+group.getId()+".json"),group);
            logger.info("updated group data of: "+group.getId());
        } catch(IOException e){
            logger.error("unable to update group data of: "+group.getId());
            e.printStackTrace();
        }
    }

    public synchronized GroupMessage getGroupMessage(long groupID, long id){
        try{
            if(id==-1) return null;
            GroupMessage message = objectMapper.readValue(new File(groupMessagesDirectory.getPath()+"/"+groupID+"/"+id+".json"),GroupMessage.class);
            //logger.info("loaded and returned group message: "+mainSender+"/"+id);
            return message;
        } catch(IOException e){
            logger.error("unable to get group message: "+groupID+"/"+id);
            e.printStackTrace();
            return null;
        }
    }
    public synchronized void saveNewGroupMessage(GroupMessage message){
        try{
            objectMapper.writeValue(new FileWriter(groupMessagesDirectory.getPath()+"/"+message.getGroupID()+"/"+message.getId()+".json"),message);
            logger.info("created new file for group message: "+message.getGroupID()+"/"+message.getId());
        } catch(IOException e){
            logger.error("unable to create new file for group message: "+message.getGroupID()+"/"+message.getId());
            e.printStackTrace();
        }
    }
    public synchronized void updateGroupMessage(GroupMessage message){
        try{
            objectMapper.writeValue(new FileWriter(groupMessagesDirectory.getPath()+"/"+message.getGroupID()+"/"+message.getId()+".json"),message);
            logger.info("updated group message data of: "+message.getGroupID()+"/"+message.getId());
        } catch(IOException e){
            logger.error("unable to update group message data of: "+message.getGroupID()+"/"+message.getId());
            e.printStackTrace();
        }
    }

    public synchronized byte[] getImage(long id){
        byte[] bytes = null;
        try{
            if(id>-1){
                File file = new File(imagesDirectory,id+".png");
                bytes = Files.readAllBytes(file.toPath());
                logger.info("loaded and returned image bytes: "+id);
            }
        } catch(Exception e){
            logger.error("unable to get image: "+id);
            e.printStackTrace();
        }
        return bytes;
    }
    public synchronized long saveNewImage(byte[] imageData){
        try{
            if(imageData!=null){
                ImageIcon imageIcon = new ImageIcon(imageData);
                BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(),imageIcon.getIconHeight(),BufferedImage.TYPE_INT_RGB);
                Graphics g = bufferedImage.createGraphics();
                imageIcon.paintIcon(null,g,0,0);
                g.dispose();
                long newID = generateNewImageID();
                File destination = new File(imagesDirectory,newID+".png");
                ImageIO.write(bufferedImage,"png",destination);
                logger.info("created new file for image: "+newID);
                return newID;
            }
        } catch(Exception e){
            logger.error("unable to save image: "+Arrays.toString(imageData));
            e.printStackTrace();
        }
        return -1;
    }

    public synchronized String getBotData(long id){
        try{
            if(id==-1) return null;
            String data = objectMapper.readValue(new File(botsDirectory.getPath()+"/"+id+".json"),String.class);
//            logger.info("loaded and returned bot data: "+id);
            return data;
        } catch(IOException e){
            logger.error("unable to get bot data: "+id);
            e.printStackTrace();
            return null;
        }
    }
    public synchronized void saveNewBotData(long id){
        try{
            objectMapper.writeValue(new FileWriter(botsDirectory.getPath()+"/"+id+".json"),null);
            logger.info("created new file for bot data: "+id);
        } catch(IOException e){
            logger.error("unable to create new file for bot data: "+id);
            e.printStackTrace();
        }
    }
    public synchronized void updateBotData(long id, String data){
        try{
            objectMapper.writeValue(new FileWriter(botsDirectory.getPath()+"/"+id+".json"),data);
//            logger.info("updated bot data of: "+id);
        } catch(IOException e){
            logger.error("unable to update bot data of: "+id);
            e.printStackTrace();
        }
    }

    public synchronized void deleteMessageFiles(User user1, User user2){
        long id1 = user1.getId(), id2 = user2.getId();
        File directory1 = new File(messagesDirectory.getPath()+"/"+id1+"-"+id2);
        File directory2 = new File(messagesDirectory.getPath()+"/"+id2+"-"+id1);
        directory1.delete();
        directory2.delete();
    }

    public synchronized long generateNewUserID(){
        try {
            File jsonFile = new File(usersDirectory.getPath()+"/latestUserID.json");
            if(!jsonFile.createNewFile()){
                long newID = objectMapper.readValue(jsonFile,long.class)+1;
                objectMapper.writeValue(new FileWriter(jsonFile),newID);
                logger.info("generated and returned new user id: "+newID);
                return newID;
            }
            else{
                objectMapper.writeValue(new FileWriter(jsonFile),1);
                logger.info("created latestUserID file and returned: 1");
                return 1;
            }
        } catch(Exception e) {
            logger.error("unable to generate new user id");
            e.printStackTrace();
            return 0;
        }
    }
    public synchronized long generateNewTweetID(long mainSenderID){
        try{
            File directory = new File(tweetsDirectory.getPath()+"/"+mainSenderID);
            File jsonFile = new File(directory.getPath()+"/latestTweetID.json");
            if(!directory.mkdir()){
                long newID = objectMapper.readValue(jsonFile,long.class)+1;
                objectMapper.writeValue(new FileWriter(jsonFile),newID);
                logger.info("generated and returned new tweet id: "+mainSenderID+"/"+newID);
                return newID;
            }
            else{
                objectMapper.writeValue(new FileWriter(jsonFile),1);
                logger.info("created latestTweetID file and returned: "+mainSenderID+"/1");
                return 1;
            }
        } catch(IOException e){
            logger.error("unable to generate new tweet id for user: "+mainSenderID);
            e.printStackTrace();
            return 0;
        }
    }
    public synchronized long generateNewMessageID(long sender, long receiver){
        try{
            File directory1 = new File(messagesDirectory.getPath()+"/"+sender+"-"+receiver);
            File directory2 = new File(messagesDirectory.getPath()+"/"+receiver+"-"+sender);
            if(!directory2.exists()){
                File jsonFile = new File(directory1.getPath()+"/latestMessageID.json");
                if(!directory1.mkdir()){
                    long newID = objectMapper.readValue(jsonFile,long.class)+1;
                    objectMapper.writeValue(new FileWriter(jsonFile),newID);
                    logger.info("generated and returned new message id: "+sender+"-"+receiver+"/"+newID);
                    return newID;
                }
                else{
                    objectMapper.writeValue(new FileWriter(jsonFile),1);
                    logger.info("created latestMessageID file and returned new message id: "+sender+"-"+receiver+"/1");
                    return 1;
                }
            }
            else{
                File jsonFile = new File(directory2.getPath()+"/latestMessageID.json");
                long newID = objectMapper.readValue(jsonFile,long.class)+1;
                objectMapper.writeValue(new FileWriter(jsonFile),newID);
                logger.info("generate and returned new message id: "+receiver+"-"+sender+"/"+newID);
                return newID;
            }
        } catch(IOException e){
            logger.error("unable to generate new message id for users: "+sender+"-"+receiver+" nor "+receiver+"-"+sender);
            e.printStackTrace();
            return 0;
        }
    }
    public synchronized long generateNewGroupID(){
        try{
            File jsonFile = new File(groupsDirectory.getPath()+"/latestGroupID.json");
            if(!jsonFile.createNewFile()){
                long newID = objectMapper.readValue(jsonFile,long.class)+1;
                objectMapper.writeValue(new FileWriter(jsonFile),newID);
                logger.info("generated and returned new group id: "+newID);
                return newID;
            }
            else{
                objectMapper.writeValue(new FileWriter(jsonFile),1);
                logger.info("created latestGroupID file and returned: 1");
                return 1;
            }
        } catch(Exception e){
            logger.error("unable to generate new group id");
            e.printStackTrace();
            return 0;
        }
    }
    public synchronized long generateNewGroupMessageID(long groupID){
        try{
            File directory = new File(groupMessagesDirectory.getPath()+"/"+groupID);
            File jsonFile = new File(directory.getPath()+"/latestGroupMessageID.json");
            if(!directory.mkdir()){
                long newID = objectMapper.readValue(jsonFile,long.class)+1;
                objectMapper.writeValue(new FileWriter(jsonFile),newID);
                logger.info("generated and returned new group message id: "+newID);
                return newID;
            }
            else{
                objectMapper.writeValue(new FileWriter(jsonFile),1);
                logger.info("created latestGroupMessageID file and returned: "+groupID+"/1");
                return 1;
            }
        } catch(Exception e){
            logger.error("unable to generate new group message id");
            e.printStackTrace();
            return 0;
        }
    }
    private synchronized long generateNewImageID(){
        try{
            File jsonFile = new File(imagesDirectory,"latestImageID.json");
            if(!jsonFile.createNewFile()){
                long newID = objectMapper.readValue(jsonFile,long.class)+1;
                objectMapper.writeValue(new FileWriter(jsonFile),newID);
                logger.info("generated and returned new image id: "+newID);
                return newID;
            }
            else{
                objectMapper.writeValue(new FileWriter(jsonFile),1);
                logger.info("created latestImageID file and returned: 1");
                return 1;
            }
        } catch(Exception e){
            logger.error("unable to generate new image id");
            e.printStackTrace();
            return 0;
        }
    }

    public synchronized long getLatestUserID(){
        try {
            File jsonFile = new File(usersDirectory.getPath()+"/latestUserID.json");
            long id = objectMapper.readValue(jsonFile,long.class);
            //logger.info("loaded and returned latest user id: "+id);
            return id;
        } catch (IOException e) {
            logger.error("unable to get latest user id");
            e.printStackTrace();
            return 0;
        }
    }
//    public synchronized long getLatestTweetID(User mainSender){
//        try {
//            File jsonFile = new File(tweetsDirectory.getPath()+"/"+mainSender+"/latestTweetID.json");
//            long id = objectMapper.readValue(jsonFile,long.class)+1;
//            logger.info("loaded and returned latest tweet id: "+mainSender+"/"+id);
//            return id;
//        } catch (IOException e) {
//            logger.error("unable to get latest tweet id for user: "+mainSender);
//            e.printStackTrace();
//            return 0;
//        }
//    }
}
