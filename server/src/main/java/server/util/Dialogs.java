package server.util;

import java.util.HashMap;

public class Dialogs {
    private static final Config config = Config.getConfig("dialogs");
    private static final HashMap<String,String> dialogs = new HashMap<>(){
        {
            put("InvalidClassPath", config.getProperty(String.class, "InvalidClassPath"));
            put("InvalidClassStructure", config.getProperty(String.class, "InvalidClassStructure"));
            put("ValidClassStructure", config.getProperty(String.class, "ValidClassStructure"));
        }
    };
    public static String get(String name){
        String text = dialogs.get(name);
        if(text!=null) return text;
        else return "< CONFIG ERROR >";
    }
}
