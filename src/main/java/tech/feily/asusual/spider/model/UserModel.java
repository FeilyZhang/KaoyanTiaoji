package tech.feily.asusual.spider.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class UserModel {

    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    @Column(name = "SEND_COUNT")
    private int sendCount;
    @Column(name = "LAST_SEND_TIME")
    private Timestamp lastSendTime;
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }
    
    public void setLastSendTime(Timestamp lastSendTime) {
        this.lastSendTime = lastSendTime;
    }
    
    public int getId() {
        return id;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public Timestamp getCreateTime() {
        return createTime;
    }
    
    public int getSendCount() {
        return sendCount;
    }
    
    public Timestamp getLastSendTime() {
        return lastSendTime;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("phone : " + phone + "\n");
        sb.append("createTime : " + createTime.toString() + "\n");
        sb.append("sendCount : " + sendCount + "\n");
        sb.append("lastSendTime : " + lastSendTime.toString() + "\n");
        return sb.toString();
    }
    
}
