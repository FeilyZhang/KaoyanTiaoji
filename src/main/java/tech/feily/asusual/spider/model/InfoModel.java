package tech.feily.asusual.spider.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INFO")
public class InfoModel implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -3028242250897440464L;
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "FIRST_VISIT")
    private Timestamp firstVisit;
    @Column(name = "LAST_VISIT")
    private Timestamp lastVisit;
    @Column(name = "VISIT_COUNT")
    private long visitCount;
    @Column(name = "URL")
    private String url;
    
    public void setId(int id) {
        this.id = id;
    }
    
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
    
    public int getId() {
        return id;
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
