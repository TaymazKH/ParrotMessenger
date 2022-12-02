package client.util;

import client.agent.LogicalAgent;
import shared.request.*;

import java.util.concurrent.atomic.AtomicInteger;

public class Loop extends Thread {
    private final int delay = 1000;
    private final LogicalAgent logicalAgent;
    private volatile boolean keepRunning = true, active = false;
    private final AtomicInteger count = new AtomicInteger(0);

    public Loop(LogicalAgent logicalAgent){
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void run() {
        while(keepRunning){
            try{
                if(!active){
                    synchronized(this){
                        wait();
                    }
                    continue;
                }
                Thread.sleep(delay);
                if(count.addAndGet(1)==10){
                    sendOfflineDBRequest();
                    count.set(0);
                }
                else{
                    sendUpdateRequest();
                }
            } catch(InterruptedException ignored){}
        }
    }

    public boolean isActive(){
        return active;
    }
    public void deactivate(){
        active = false;
    }
    public void activate(){
        active = true;
        synchronized(this){
            notifyAll();
        }
    }
    public void stopLater(){
        keepRunning = false;
        synchronized(this){
            notifyAll();
        }
    }

    private void sendUpdateRequest(){
        logicalAgent.loopSendRequest(new UpdatePageRequest());
    }
    private void sendOfflineDBRequest(){
        logicalAgent.loopSendRequest(new GetOfflineDataBaseRequest());
    }
}
