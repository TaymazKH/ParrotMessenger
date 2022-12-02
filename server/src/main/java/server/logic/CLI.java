package server.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.bot.BotStarter;
import server.network.SocketStarter;
import server.util.Config;
import server.util.Dialogs;

import java.util.Scanner;

public class CLI {
    private static final Logger logger = LogManager.getLogger(CLI.class);

    public void initialize(){
        logger.info("Started working");
        DataHolder dataHolder = new DataHolder();
        SocketStarter socketStarter = new SocketStarter(dataHolder){{ start(); }};
        BotStarter botStarter = new BotStarter(dataHolder){{ initializeAll(); }};
        Scanner scanner = new Scanner(System.in);
        while(true){
            try{
                String command = scanner.nextLine();
                if(command.equals(loadCommand("Stop"))) break;
                else if(command.startsWith(loadCommand("AddBot")+" ") && !command.substring(loadCommand("AddBot").length()).isBlank())
                    System.out.println(Dialogs.get(botStarter.check(command.substring(loadCommand("AddBot").length()+1))));
            } catch(Exception ignored){}
        }
        logger.info("end of process");
        System.exit(0);
    }

    private String loadCommand(String name){
        return Config.getConfig("cli_commands").getProperty(String.class,name);
    }
}
