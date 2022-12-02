package client.resource_loader;

import client.util.PageDataType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import client.util.Config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SceneLoader {
    private static final Logger logger = LogManager.getLogger(SceneLoader.class);
    private final HashMap<PageDataType,String> onlineScenesFilesLocations,offlineScenesFilesLocations;

    public SceneLoader(){
        Config config = Config.getConfig("fxmlLocations");
        onlineScenesFilesLocations = new HashMap<>();
        onlineScenesFilesLocations.put(PageDataType.login, config.getProperty(String.class,"login"));
        onlineScenesFilesLocations.put(PageDataType.mainMenu, config.getProperty(String.class,"mainMenu"));
        onlineScenesFilesLocations.put(PageDataType.personal, config.getProperty(String.class,"personal"));
        onlineScenesFilesLocations.put(PageDataType.tweets, config.getProperty(String.class,"tweets"));
        onlineScenesFilesLocations.put(PageDataType.lists, config.getProperty(String.class,"lists"));
        onlineScenesFilesLocations.put(PageDataType.requests, config.getProperty(String.class,"requests"));
        onlineScenesFilesLocations.put(PageDataType.notifications, config.getProperty(String.class,"notifications"));
        onlineScenesFilesLocations.put(PageDataType.timeLine, config.getProperty(String.class,"timeLine"));
        onlineScenesFilesLocations.put(PageDataType.exploring, config.getProperty(String.class,"exploring"));
        onlineScenesFilesLocations.put(PageDataType.chatList, config.getProperty(String.class,"chatList"));
        onlineScenesFilesLocations.put(PageDataType.messaging, config.getProperty(String.class,"messaging"));
        onlineScenesFilesLocations.put(PageDataType.groups, config.getProperty(String.class,"groups"));
        onlineScenesFilesLocations.put(PageDataType.settings, config.getProperty(String.class,"settings"));
        offlineScenesFilesLocations = new HashMap<>();
        offlineScenesFilesLocations.put(PageDataType.connection, config.getProperty(String.class,"noConnection"));
        offlineScenesFilesLocations.put(PageDataType.mainMenu, config.getProperty(String.class,"offlineMainMenu"));
        offlineScenesFilesLocations.put(PageDataType.personal, config.getProperty(String.class,"offlinePersonal"));
        offlineScenesFilesLocations.put(PageDataType.tweets, config.getProperty(String.class,"offlineTweets"));
        offlineScenesFilesLocations.put(PageDataType.chatList, config.getProperty(String.class,"offlineChatList"));
        offlineScenesFilesLocations.put(PageDataType.messaging, config.getProperty(String.class,"offlineMessaging"));
        offlineScenesFilesLocations.put(PageDataType.groups, config.getProperty(String.class,"offlineGroups"));
    }

    public Scene loadScene(PageDataType pageDataType, boolean online){
        try{
            String location;
            if(online) location = onlineScenesFilesLocations.get(pageDataType);
            else location = offlineScenesFilesLocations.get(pageDataType);
            Scene scene = new Scene(FXMLLoader.load(new File(location).toURI().toURL()));
            logger.info("loaded and returned scene: "+pageDataType);
            return scene;
        } catch(IOException e){
            logger.error("unable to load scene fxml file: "+pageDataType);
            return null;
        }
    }
}
