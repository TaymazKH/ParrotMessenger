import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class PollBot {
    private static HashMap<Long,Poll> polls;

    public static String getFields() {
        try {
            return new ObjectMapper().writeValueAsString(polls);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
    public static void setFields(String data) {
        if(data==null) polls = new HashMap<>();
        else{
            try {
                polls = new ObjectMapper().readValue(data,new TypeReference<HashMap<Long,Poll>>(){});
            } catch (IOException e) {
                e.printStackTrace();
                polls = new HashMap<>();
            }
        }
    }

    public static String[] answerMessage(String text, long sender) {
        return null;
    }
    public static String[] answerGroupMessage(String text, long sender, long group, long owner) {
        if(text.equals("/help")) return new String[]{"/newPoll <question>+<2 spaces>+<option>+... /vote <index> /retract /get /end",null};
        else if(text.startsWith("/newPoll ")){
            if(polls.containsKey(group)) return new String[]{"there already is an active poll",null};
            ArrayList<String> options = new ArrayList<>();
            String question = null;
            text = text.substring(8)+"  ";
            while(true){
                while(!text.isEmpty() && text.charAt(0)==' ') text = text.substring(1);
                if(text.isEmpty()) break;
                int index = text.indexOf("  ");
                String option = text.substring(0,index);
                if(question==null) question = option;
                else if(!options.contains(option)) options.add(option);
                text = text.substring(index+2);
            }
            if(options.size()>1){
                polls.put(group,new Poll(question,options));
                String answer = question;
                for(int i=0;i<options.size();i++) answer += "  "+i+": "+options.get(i);
                return new String[]{answer,null};
            }
        }
        else{
            try {
                if(text.startsWith("/vote ")){
                    return new String[]{"voted on: "+polls.get(group).add(sender,Integer.parseInt(text.substring(6))),null};
                }
                else if(text.equals("/retract")){
                    if(polls.get(group).remove(sender)) return new String[]{"retracted vote",null};
                }
                else if(text.equals("/get")){
                    return new String[]{"results so far:"+polls.get(group).print(),null};
                }
                else if(text.equals("/end")){
                    return new String[]{"final results:"+polls.remove(group).print(),null};
                }
            } catch(Exception ignored){}
        }
        return null;
    }
    public static String[] sendMessage(long receiver) {
        return null;
    }
    public static String[] sendGroupMessage(long group) {
        return null;
    }
    public static String[] sendComment(long user, String text, long tweet) {
        return null;
    }
    public static String[] sendTweet() {
        return null;
    }

    public static boolean invokeAnswerMessage() {
        return false;
    }
    public static boolean invokeAnswerGroupMessage() {
        return true;
    }
    public static boolean invokeSendMessage() {
        return false;
    }
    public static boolean invokeSendGroupMessage() {
        return false;
    }
    public static boolean invokeSendComment() {
        return false;
    }
    public static boolean invokeSendTweet() {
        return false;
    }
    public static boolean acceptInvite() {
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    static class Poll {
        private String question;
        private HashMap<String,HashSet<Long>> votes;
        private ArrayList<String> options;

        public Poll(){}
        public Poll(String question, ArrayList<String> options) {
            this.question = question;
            this.options = options;
            votes = new HashMap<>();
            for(String option: options) votes.put(option,new HashSet<>());
        }

        public String getQuestion() {
            return question;
        }
        public HashMap<String, HashSet<Long>> getVotes() {
            return votes;
        }
        public ArrayList<String> getOptions() {
            return options;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
        public void setVotes(HashMap<String, HashSet<Long>> votes) {
            this.votes = votes;
        }
        public void setOptions(ArrayList<String> options) {
            this.options = options;
        }

        public String add(long id, int index){
            remove(id);
            String option = options.get(index);
            votes.get(option).add(id);
            return option;
        }
        public boolean remove(long id){
            for(Map.Entry<String,HashSet<Long>> m: votes.entrySet()) if(m.getValue().remove(id)) return true;
            return false;
        }
        public String print(){
            int totalCount=0;
            for(Map.Entry<String,HashSet<Long>> m: votes.entrySet()) totalCount+=m.getValue().size();
            String answer="";
            for(Map.Entry<String,HashSet<Long>> m: votes.entrySet()){
                answer += "  "+m.getKey()+": ";
                if(m.getValue().size()==0) answer += "0%";
                else answer += m.getValue().size()*100/totalCount+"%";
            }
            return answer;
        }
    }
}
