package tech.feily.asusual.spider.utils;

import java.util.LinkedList;

import tech.feily.asusual.spider.model.BQModel;

/*
 * Blocked Queue to simulate the provider and consumer scenarios.
 * @author Feily Zhang
 * @version v0.1
 * @email fei@feily.tech
 */
public class BlockedQueue {
    private final int max;
    private final LinkedList<BQModel> eventQueue = new LinkedList<BQModel>();
    private final static int DEFAULT_MAX_EVENT = 10;
    
    public BlockedQueue() {
        this(DEFAULT_MAX_EVENT);
    }
    
    public BlockedQueue(int max) {
        this.max = max;
    }
    
    public void produce(BQModel bqModel) {
        synchronized(eventQueue) {
            while (eventQueue.size() >= max) {
                try {
                    eventQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            eventQueue.addLast(bqModel);
            eventQueue.notifyAll();
        }
    }
    
    public BQModel consume() {
        synchronized(eventQueue) {
            while (eventQueue.isEmpty()) {
                try {
                    eventQueue.wait();
                } catch (InterruptedException e) {
                   e.printStackTrace();
                }
            }
            BQModel event = eventQueue.removeFirst();
            this.eventQueue.notifyAll();
            return event;
        }
    }
    
}