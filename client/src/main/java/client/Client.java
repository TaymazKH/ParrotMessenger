package client;

import client.agent.LogicalAgent;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client extends Application {
    private static final Logger logger = LogManager.getLogger(Client.class);

    @Override
    public void start(Stage stage){
        new LogicalAgent(stage);
    }

    public static void main(String[] args){
        logger.info("has started working");
        launch(args);
    }
}

/*
TO DO LIST:
==================

 */
