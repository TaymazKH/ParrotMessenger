import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class XOGameBot {
    private static HashMap<Long,GameState> games;
    private static HashMap<Integer,Long> waiting;
    private static ArrayList<Long> messages;

    public static String getFields() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String s1 = objectMapper.writeValueAsString(games),
                    s2 = objectMapper.writeValueAsString(waiting),
                    s3 = objectMapper.writeValueAsString(messages);
            String[] array = new String[]{s1,s2,s3};
            return objectMapper.writeValueAsString(array);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void setFields(String data) {
        if(data==null){
            games = new HashMap<>();
            waiting = new HashMap<>();
            messages = new ArrayList<>();
        }
        else{
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String[] array = objectMapper.readValue(data,String[].class);
                games = objectMapper.readValue(array[0],new TypeReference<HashMap<Long,GameState>>(){});
                waiting = objectMapper.readValue(array[1],new TypeReference<HashMap<Integer,Long>>(){});
                messages =objectMapper.readValue(array[2],new TypeReference<ArrayList<Long>>(){});
            } catch (IOException e) {
                e.printStackTrace();
                games = new HashMap<>();
                waiting = new HashMap<>();
                messages = new ArrayList<>();
            }
        }
    }

    public static String[] answerMessage(String text, long sender) {
        if(text.equals("/help")) return new String[]{"/newGame  /join <invite_code>  /mark <1-9>  /cancel",null};
        else{
            try {
                if(text.equals("/newGame")){
                    if(games.containsKey(sender)) return new String[]{"finish your current game first",null};
                    else{
                        Random random = new Random();
                        int inviteCode;
                        do{
                            inviteCode = random.nextInt(Integer.MAX_VALUE);
                        } while(waiting.containsKey(inviteCode));
                        waiting.put(inviteCode,sender);
                        return new String[]{"send this invite code to your opponent: "+inviteCode,null};
                    }
                }
                else if(text.startsWith("/join ")){
                    if(games.containsKey(sender)) return new String[]{"finish your current game first",null};
                    else if(waiting.containsValue(sender)) return new String[]{"you are in the waiting list",null};
                    else{
                        int inviteCode = Integer.parseInt(text.substring(6));
                        if(!waiting.containsKey(inviteCode)) return new String[]{"invalid or expired invite code",null};
                        else{
                            long otherPlayer = waiting.remove(inviteCode);
                            GameState gameState = new GameState(otherPlayer,sender);
                            games.put(otherPlayer,gameState);
                            games.put(sender,gameState);
                            messages.add(otherPlayer);
                            return new String[]{gameState.print(sender),gameState.draw()};
                        }
                    }
                }
                else if(text.startsWith("/mark ")){
                    if(!games.containsKey(sender)) return new String[]{"you are not in a game",null};
                    else{
                        int index = Integer.parseInt(text.substring(6,7))-1;
                        GameState gameState = games.get(sender);
                        long otherPlayer = gameState.getPlayers()[0];
                        int player=2;
                        if(sender==otherPlayer){
                            otherPlayer = gameState.getPlayers()[1];
                            player=1;
                        }
                        boolean changed = gameState.mark(player,index);
                        if(changed){
                            if(gameState.getState()>0) games.remove(sender);
                            else games.replace(sender,gameState);
                            games.replace(otherPlayer,gameState);
                            messages.add(otherPlayer);
                        }
                        return new String[]{gameState.print(sender),gameState.draw()};
                    }
                }
                else if(text.equals("/cancel")){
                    if(games.containsKey(sender)){
                        GameState gameState = games.remove(sender);
                        long otherPlayer = gameState.getPlayers()[0];
                        if(sender==otherPlayer) otherPlayer = gameState.getPlayers()[1];
                        games.remove(otherPlayer);
                        messages.add(otherPlayer);
                        return new String[]{"canceled the game",null};
                    }
                    else if(waiting.containsValue(sender)){
                        int inviteCode=-1;
                        for(Map.Entry<Integer,Long> m: waiting.entrySet()){
                            if(m.getValue()==sender){
                                inviteCode = m.getKey();
                                break;
                            }
                        }
                        waiting.remove(inviteCode);
                        return new String[]{"canceled invitation",null};
                    }
                    else return new String[]{"nothing to cancel",null};
                }
            } catch(Exception ignored){}
        }
        return null;
    }
    public static String[] answerGroupMessage(String text, long sender, long group, long owner) {
        return null;
    }
    public static String[] sendMessage(long receiver) {
        if(messages.remove(receiver)){
            GameState gameState = games.get(receiver);
            if(gameState==null) return new String[]{"your opponent canceled the game",null};
            else{
                if(gameState.getState()>0) games.remove(receiver);
                return new String[]{gameState.print(receiver),gameState.draw()};
            }
        }
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
        return true;
    }
    public static boolean invokeAnswerGroupMessage() {
        return false;
    }
    public static boolean invokeSendMessage() {
        return true;
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
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    static class GameState {
        private int[] cells;
        private int turn,state; // 0: on going / 1,2: winner / 3: draw
        private long[] players;

        public GameState(){}
        public GameState(long player1, long player2) {
            players = new long[]{player1,player2};
            turn=1;
            state=0;
            cells = new int[]{0,0,0,0,0,0,0,0,0};
        }

        public int[] getCells() {
            return cells;
        }
        public int getTurn() {
            return turn;
        }
        public int getState() {
            return state;
        }
        public long[] getPlayers() {
            return players;
        }

        public void setCells(int[] cells) {
            this.cells = cells;
        }
        public void setTurn(int turn) {
            this.turn = turn;
        }
        public void setState(int state) {
            this.state = state;
        }
        public void setPlayers(long[] players) {
            this.players = players;
        }

        public boolean mark(int player, int index){
            if(cells[index]==0 && turn%2==player%2){
                cells[index] = player;
                turn++;
                if(cells[0]==cells[1] && cells[0]==cells[2] && cells[0]==player) state=player;
                else if(cells[3]==cells[4] && cells[3]==cells[5] && cells[3]==player) state=player;
                else if(cells[6]==cells[7] && cells[6]==cells[8] && cells[6]==player) state=player;
                else if(cells[0]==cells[3] && cells[0]==cells[6] && cells[0]==player) state=player;
                else if(cells[1]==cells[4] && cells[1]==cells[7] && cells[1]==player) state=player;
                else if(cells[2]==cells[5] && cells[2]==cells[8] && cells[2]==player) state=player;
                else if(cells[0]==cells[4] && cells[0]==cells[8] && cells[0]==player) state=player;
                else if(cells[2]==cells[4] && cells[2]==cells[6] && cells[2]==player) state=player;
                else{
                    boolean draw = true;
                    for(int i=0;i<9;i++){
                        if(cells[i]==0){
                            draw = false;
                            break;
                        }
                    }
                    if(draw) state=3;
                }
                return true;
            }
            return false;
        }
        public String print(long id){
            String ans;
            if(id==players[0]){
                switch(state){
                    case 1-> ans = "you have won!";
                    case 2-> ans = "you have lost!";
                    case 3-> ans = "a draw!";
                    default ->{
                        if(turn%2==1) ans = "your turn";
                        else ans = "opponent's turn";
                    }
                }
                ans += " ( you are red )";
            }
            else{
                switch(state){
                    case 1-> ans = "you have lost!";
                    case 2-> ans = "you have won!";
                    case 3-> ans = "a draw!";
                    default ->{
                        if(turn%2==1) ans = "opponent's turn";
                        else ans = "your turn";
                    }
                }
                ans += " ( you are blue )";
            }
            return ans;
        }
        public String draw(){
            BufferedImage image = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setPaint(Color.PINK);
            g.fillRect(0,0,100,100);
            g.setPaint(Color.BLACK);
            g.drawLine(33,0,33,100);
            g.drawLine(66,0,66,100);
            g.drawLine(0,33,100,33);
            g.drawLine(0,66,100,66);
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    int x=33*j, y=33*i;
                    if(cells[3*i+j]==1){
                        g.setPaint(Color.RED);
                        g.fillRect(x+5,y+5,23,23);
                    }
                    else if(cells[3*i+j]==2){
                        g.setPaint(Color.BLUE);
                        g.fillOval(x+5,y+5,23,23);
                    }
                }
            }
            g.dispose();
            try{
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(image,"png",outputStream);
                byte[] imageData = outputStream.toByteArray();
                return new ObjectMapper().writeValueAsString(imageData);
            } catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GameState gameState = (GameState) o;
            return Arrays.equals(players, gameState.players);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(players);
        }
    }
}
