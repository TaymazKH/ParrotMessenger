public interface Bot {
     String getFields();
     void setFields(String data);

     String[] answerMessage(String text, long sender);
     String[] answerGroupMessage(String text, long sender, long group, long owner);
     String[] sendMessage(long receiver);
     String[] sendGroupMessage(long group);
     String[] sendComment(long user, String text, long tweet);
     String[] sendTweet();

     boolean invokeAnswerMessage();
     boolean invokeAnswerGroupMessage();
     boolean invokeSendMessage();
     boolean invokeSendGroupMessage();
     boolean invokeSendComment();
     boolean invokeSendTweet();
     boolean acceptInvite();
}
