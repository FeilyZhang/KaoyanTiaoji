package tech.feily.asusual.spider.model;

import java.sql.Timestamp;

public class InfoModel {

    private String title;
    private Timestamp firstVisit;
    private Timestamp lastVisit;
    private long visitCount;
    private String url;
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setFirstVisit(Timestamp firstVisit) {
        this.firstVisit = firstVisit;
    }
    
    public void setLastVisit(Timestamp lastVisit) {
        this.lastVisit = lastVisit;
    }
    
    public void setVisitCount(long visitCount) {
        this.visitCount = visitCount;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Timestamp getFirstVisit() {
        return firstVisit;
    }
    
    public Timestamp getLastVisit() {
        return lastVisit;
    }
    
    public long getVisitCount() {
        return visitCount;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("title : " + title + "\n");
        sb.append("firstVisit : " + firstVisit.toString() + "\n");
        sb.append("lastVisit : " + lastVisit.toString() + "\n");
        sb.append("visitCount : " + visitCount + "\n");
        sb.append("url : " + url + "\n");
        return sb.toString();
    }
    
}
