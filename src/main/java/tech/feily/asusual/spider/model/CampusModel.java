package tech.feily.asusual.spider.model;

import java.io.Serializable;

public class CampusModel implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 7680771484174395613L;
    private String name;
    private String url;
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getName() {
        return name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name : " + name + "\n");
        sb.append("url : " + url + "\n");
        return sb.toString();
    }
    
}
