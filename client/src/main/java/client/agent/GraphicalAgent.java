package client.agent;

import client.offline.event.Event;
import client.page_data.PageData;
import client.util.Config;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.request.Request;
import client.fx_controller.PageController;
import client.resource_loader.SceneLoader;

public class GraphicalAgent {
    private static final Logger logger = LogManager.getLogger(GraphicalAgent.class);
    private final LogicalAgent logicalAgent;
    private final SceneLoader sceneLoader;
    private PageController currentPageController;
    private final Stage stage;
    private PageData pageData;

    public GraphicalAgent(LogicalAgent logicalAgent, Stage stage) {
        this.logicalAgent = logicalAgent;
        this.sceneLoader = new SceneLoader();
        this.stage = stage;
        PageController.setGraphicalAgent(this);
    }

    public void initialize(){
        logger.info("has started working");
        stage.setTitle("Parrot Messenger");
        stage.getIcons().add(new Image(Config.getConfig("imageLocations").getProperty(String.class,"parrotLogo")));
        stage.setResizable(false);
        stage.show();
    }
    public void sendRequest(Request request){
        logicalAgent.sendRequest(request);
    }
    public void runEvent(Event event){
        logicalAgent.runEvent(event);
    }
    public void updateGraphics(PageData data, boolean online){
        pageData = data;
        Platform.runLater(()->{
            if(pageData!=null){
                if(currentPageController==null || pageData.getPageDataType()!=currentPageController.getPageDataType())
                    stage.setScene(sceneLoader.loadScene(pageData.getPageDataType(),online));
                currentPageController.update(pageData);
            }
            else close();
        });
        logger.info("updated scene");
    }
    public void close(){
        logger.info("end of process");
        stage.close();
    }

    /////////////////////////////////////////////////////////////////////////////////

    public LogicalAgent getLogicalAgent() {
        return logicalAgent;
    }
    public SceneLoader getResourceAgent() {
        return sceneLoader;
    }
    public PageController getCurrentPageController() {
        return currentPageController;
    }
    public Stage getStage() {
        return stage;
    }

    public void setCurrentPageController(PageController currentPageController) {
        this.currentPageController = currentPageController;
    }
}
