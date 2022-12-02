package server.logic.model_logic;

import server.model.Tweet;

import java.util.Arrays;
import java.util.LinkedList;

public class TweetLogic {
    public static long[] showNext(Tweet currentTweet, Tweet upperTweet, LinkedList<long[]> primaryTweetList) throws IndexOutOfBoundsException{
        if(currentTweet.isPrimaryTweet()) return primaryTweetList.get(indexOf(primaryTweetList,new long[]{currentTweet.getMainSender(),currentTweet.getId()})+1);
        else return new long[]{currentTweet.getMainSender(), upperTweet.getComments().get(upperTweet.getComments().indexOf(currentTweet.getId())+1)};
    }
    public static long[] showPrevious(Tweet currentTweet, Tweet upperTweet, LinkedList<long[]> primaryTweetList) throws IndexOutOfBoundsException{
        if(currentTweet.isPrimaryTweet()) return primaryTweetList.get(indexOf(primaryTweetList,new long[]{currentTweet.getMainSender(),currentTweet.getId()})-1);
        else return new long[]{currentTweet.getMainSender(), upperTweet.getComments().get(upperTweet.getComments().indexOf(currentTweet.getId())-1)};
    }
    public static long showNextInPersonalPage(Tweet currentTweet, Tweet upperTweet, LinkedList<Long> primaryTweetList) throws IndexOutOfBoundsException{
        if(currentTweet.isPrimaryTweet()) return primaryTweetList.get(primaryTweetList.indexOf(currentTweet.getId())+1);
        else return upperTweet.getComments().get(upperTweet.getComments().indexOf(currentTweet.getId())+1);
    }
    public static long showPreviousInPersonalPage(Tweet currentTweet, Tweet upperTweet, LinkedList<Long> primaryTweetList) throws IndexOutOfBoundsException{
        if(currentTweet.isPrimaryTweet()) return primaryTweetList.get(primaryTweetList.indexOf(currentTweet.getId())-1);
        else return upperTweet.getComments().get(upperTweet.getComments().indexOf(currentTweet.getId())-1);
    }
    public static long showUpperTweetID(Tweet currentTweet) throws NullPointerException{
        if(currentTweet.isPrimaryTweet()) throw new NullPointerException();
        return currentTweet.getUpperTweetID();
    }
    public static long showComments(Tweet currentTweet) throws IndexOutOfBoundsException{
        return currentTweet.getComments().get(0);
    }
    public static void addComment(Tweet currentTweet, Tweet tweet){
        currentTweet.getComments().add(tweet.getId());
    }
    public static int like(Tweet currentTweet, long userID){
        if(currentTweet.getDislikedUsers().remove(userID)){
            currentTweet.setDislikedTimes(currentTweet.getDislikedTimes()-1);
            return -1;
        }
        if(currentTweet.getLikedUsers().contains(userID)) return 0;
        currentTweet.getLikedUsers().add(userID);
        currentTweet.setLikedTimes(currentTweet.getLikedTimes()+1);
        return 1;
    }
    public static int dislike(Tweet currentTweet, long userID){
        if(currentTweet.getLikedUsers().remove(userID)){
            currentTweet.setLikedTimes(currentTweet.getLikedTimes()-1);
            return 1;
        }
        if(currentTweet.getDislikedUsers().contains(userID)) return 0;
        currentTweet.getDislikedUsers().add(userID);
        currentTweet.setDislikedTimes(currentTweet.getDislikedTimes()+1);
        return -1;
    }
    public static long getRepostedID(Tweet currentTweet){
        long repostedID = currentTweet.getRepostedFrom();
        if(repostedID==-1) repostedID = currentTweet.getSender();
        return repostedID;
    }

    private static int indexOf(LinkedList<long[]> list, long[] item){
        for (long[] longs : list) {
            if(Arrays.equals(longs,item)) return list.indexOf(longs);
        }
        return -1;
    }
}
