package tech.feily.asusual.spider.service;

import java.util.LinkedList;

import tech.feily.asusual.spider.model.InfoModel;

public class BlockedQueue {
    private final int max;
    private final LinkedList<InfoModel> eventQueue = new LinkedList<InfoModel>();
    private final static int DEFAULT_MAX_EVENT = 10;
    
    public BlockedQueue() {
        this(DEFAULT_MAX_EVENT);
    }
    
    public BlockedQueue(int max) {
        this.max = max;
    }
    
    public void produce(InfoModel infoModel) {
        synchronized(eventQueue) {
            while (eventQueue.size() >= max) {
                try {
                    eventQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            eventQueue.addLast(infoModel);
            eventQueue.notifyAll();
        }
    }
    
    public InfoModel consume() {
        synchronized(eventQueue) {
            while (eventQueue.isEmpty()) {
                try {
                    eventQueue.wait();
                } catch (InterruptedException e) {
                   e.printStackTrace();
                }
            }
            InfoModel event = eventQueue.removeFirst();
            this.eventQueue.notifyAll();
            return event;
        }
    }
    
}