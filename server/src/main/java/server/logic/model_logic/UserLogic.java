package server.logic.model_logic;

import server.model.Message;
import server.model.Tweet;
import server.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class UserLogic {
    public static void setBirthDateWithLocalDate(User currentUser,LocalDate birthDate){
        if(birthDate.isEqual(LocalDate.of(0,1,1))) currentUser.setBirthDate("****-**-**");
        else currentUser.setBirthDate(birthDate.getYear()+"-"+birthDate.getMonthValue()+"-"+birthDate.getDayOfMonth());
    }

    public static boolean requestsToSeeTweets(User targetUser,long id){
        return !targetUser.getBlackList().contains(id) && (targetUser.isPublic() || targetUser.getFollowers().contains(id));
    }
    public static boolean requestsToSeeTweetsInTweetsPage(User targetUser,long id){
        return requestsToSeeTweets(targetUser,id) && targetUser.isActive() && targetUser.isOwned();
    }
    public static boolean requestsToViewTweetsRandomly(User targetUser,long id){
        return requestsToSeeTweets(targetUser,id) && targetUser.isOwned() && targetUser.isActive() && !targetUser.getMutedBy().contains(id);
    }
    public static boolean canMessage(User user,long other){
        return user.getFollowers().contains(other) || user.getFollowing().contains(other);
    }
    public static boolean canFollow(User currentUser,long id){
        return !currentUser.getBlackList().contains(id) && !currentUser.getBlockedBy().contains(id);
    }

    public static void follow(User currentUser,User targetUser){
        currentUser.getFollowing().add(targetUser.getId());
        targetUser.getFollowers().add(currentUser.getId());
        addNotification(targetUser,currentUser.getId(),1);
    }
    public static void sendFollowRequest(User currentUser,User targetUser){
        currentUser.getOutgoingRequests().add(new long[]{targetUser.getId(),1});
        targetUser.getIncomingRequests().add(new long[]{currentUser.getId(),1});
    }
    public static void sendGroupInvitation(User currentUser, User targetUser, long groupID){
        currentUser.getOutgoingRequests().add(new long[]{targetUser.getId(),2,groupID});
        targetUser.getIncomingRequests().add(new long[]{currentUser.getId(),2,groupID});
    }
    public static void block(User currentUser,User targetUser){
        targetUser.getFollowers().remove(currentUser.getId());
        targetUser.getFollowing().remove(currentUser.getId());
        LinkedList<long[]> toDelete = new LinkedList<>();
        for(long[] request: targetUser.getOutgoingRequests())
            if(request[0]== currentUser.getId()) toDelete.add(request);
        for(long[] request: toDelete)
            targetUser.getOutgoingRequests().remove(request);
        toDelete = new LinkedList<>();
        for(long[] request: targetUser.getIncomingRequests())
            if(request[0]== currentUser.getId()) toDelete.add(request);
        for(long[] request: toDelete)
            targetUser.getIncomingRequests().remove(request);
        targetUser.getBlockedBy().add(currentUser.getId());
        currentUser.getFollowers().remove(currentUser.getId());
        currentUser.getFollowing().remove(currentUser.getId());
        toDelete = new LinkedList<>();
        for(long[] request: currentUser.getIncomingRequests())
            if(request[0] == targetUser.getId()) toDelete.add(request);
        for(long[] request: toDelete)
            currentUser.getIncomingRequests().remove(request);
        toDelete = new LinkedList<>();
        for(long[] request: currentUser.getOutgoingRequests())
            if(request[0] == targetUser.getId()) toDelete.add(request);
        for(long[] request: toDelete)
            currentUser.getOutgoingRequests().remove(request);
        currentUser.getBlackList().add(targetUser.getId());
    }
    public static void unblock(User currentUser,User targetUser){
        targetUser.getBlockedBy().remove(currentUser.getId());
        currentUser.getBlackList().remove(targetUser.getId());
    }
    public static void mute(User currentUser,User targetUser){
        targetUser.getMutedBy().add(currentUser.getId());
        currentUser.getMuted().add(targetUser.getId());
    }
    public static void unmute(User currentUser,User targetUser){
        targetUser.getMutedBy().remove(currentUser.getId());
        currentUser.getMuted().remove(targetUser.getId());
    }
    public static void report(User currentUser,User targetUser){
        targetUser.getReportedBy().add(currentUser.getId());
        currentUser.getReported().add(targetUser.getId());
    }

    public static void addTweet(User currentUser,Tweet tweet){
        currentUser.getTweets().add(tweet.getId());
    }
    public static void addMessage(User user,Message message){
        if(message.getSender()==user.getId()){
            //System.out.println(user.getMessages().get(message.getReceiver()));
            user.getMessages().get(message.getReceiver()).add(message.getId());
            user.getMessages().put(message.getReceiver(),correct(user.getMessages().get(message.getReceiver())));
            //System.out.println(user.getMessages().get(message.getReceiver()));
        }
        else{
            //System.out.println(user.getMessages().get(message.getSender()));
            user.getMessages().get(message.getSender()).add(message.getId());
            user.getMessages().put(message.getSender(),correct(user.getMessages().get(message.getSender())));
            user.getUnread().put(message.getSender(), user.getUnread().get(message.getSender())+1);
            //System.out.println(user.getMessages().get(message.getSender()));
        }
    }
    public static void addNotification(User currentUser, long id, long type){
        LocalDateTime date = LocalDateTime.now();
        currentUser.getNotifications().add(new long[]{id,type,(long) date.getYear(),(long) date.getMonthValue(),(long) date.getDayOfMonth(),(long) date.getHour(),(long) date.getMinute(),(long) date.getSecond()});
    }

    public static void logInOut(User currentUser,boolean b){
        if(b){
            currentUser.setActive(true);
        }
        else{
            LocalDateTime date = LocalDateTime.now();
            int[] lastSeen = new int[6];
            lastSeen[0] = date.getYear();
            lastSeen[1] = date.getMonthValue();
            lastSeen[2] = date.getDayOfMonth();
            lastSeen[3] = date.getHour();
            lastSeen[4] = date.getMinute();
            lastSeen[5] = date.getSecond();
            currentUser.setLastSeen(lastSeen);
        }
    }

    private static LinkedList<Long> correct(LinkedList<Long> list){
        LinkedList<Long> correctedList = new LinkedList<>();
        for(Long id: list) if(!correctedList.contains(id)) correctedList.add(id);
        return correctedList;
    }
}
