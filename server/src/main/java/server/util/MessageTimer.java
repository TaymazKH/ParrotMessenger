package server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.database.DataBase;
import server.logic.DataHolder;
import server.model.TimedMessage;
import server.model.Group;
import server.model.GroupMessage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class MessageTimer extends Thread {
    private static final Logger logger = LogManager.getLogger(MessageTimer.class);
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final File timedMessagesDirectory = new File(Config.getConfig("databaseDirectories").getProperty(String.class,"timedMessagesDirectory"));
    private final DataHolder dataHolder;

    public MessageTimer(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    @Override
    public void run() {
        try{
            while(true){
                Thread.sleep(3000);
                LocalDateTime now = LocalDateTime.now();
                File[] files;
                synchronized(this){
                    files = timedMessagesDirectory.listFiles();
                }
                for(File file: files){
                    try{
                        TimedMessage message;
                        synchronized(this){
                            message = objectMapper.readValue(file,TimedMessage.class);
                        }
                        int[] date = message.getSendDate();
                        LocalDateTime sendDate = LocalDateTime.of(date[0],date[1],date[2],date[3],date[4],date[5]);
                        if(now.isAfter(sendDate)){
                            synchronized(DataBase.getSyncObj()){
                                DataBase dataBase = dataHolder.getDataBase();
                                GroupMessage newMessage = new GroupMessage(message.getText(),dataBase.generateNewGroupMessageID(message.getGroupID()),message.getGroupID(),message.getSender(),-1,message.getPictureID());
                                dataBase.saveNewGroupMessage(newMessage);
                                Group group = dataBase.getGroup(message.getGroupID());
                                group.getMessages().add(newMessage.getId());
                                dataBase.updateGroup(group);
                            }
                            synchronized(this){
                                file.delete();
                            }
                        }
                    } catch(JsonProcessingException e){
                        logger.warn("could not process file: "+file.getName());
                        synchronized(this){
                            file.delete();
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void add(TimedMessage message){
        try{ Thread.sleep(1); } catch(InterruptedException ignored){}
        try{
            String name = LocalDateTime.now().toString();
            name = name.replace('.','_').replace('T','_').replace(':','-');
            objectMapper.writeValue(new FileWriter(new File(timedMessagesDirectory, name+".json")),message);
            logger.info("created new file for timed message: "+message.getGroupID()+"/"+message.getSender());
        } catch(IOException e){
            logger.error("could not create new file for timed message: "+message.getGroupID()+"/"+message.getSender());
            e.printStackTrace();
        }
    }
}
