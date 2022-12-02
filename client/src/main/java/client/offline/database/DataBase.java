package client.offline.database;

import client.util.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.simple_model.SimpleUser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataBase {
    private static final Logger logger = LogManager.getLogger(DataBase.class);
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final File offlineUserFile;

    public DataBase(){
        offlineUserFile = new File(Config.getConfig("databaseDirectories").getProperty(String.class,"userFile"));
    }

    ///////////////////////////////////////////////////////////////////

    public synchronized SimpleUser loadUser(){
        try{
            return objectMapper.readValue(offlineUserFile,SimpleUser.class);
        } catch(IOException ignored){}
        return null;
    }
    public synchronized void saveUser(SimpleUser user){
        if(user!=null){
            try{
                objectMapper.writeValue(new FileWriter(offlineUserFile),user);
                logger.info("saved user: "+user.getId());
            } catch(IOException e){
                logger.error("unable to save user: "+user.getId());
                e.printStackTrace();
            }
        }
    }
}
