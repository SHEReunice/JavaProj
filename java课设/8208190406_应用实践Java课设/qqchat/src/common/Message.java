/*
mesType1 →表示登录成功

mesType2 →表示登录失败

mesType3 →表示是普通的消息包
 */
package common;

import java.util.Date;

public class Message implements java.io.Serializable{
    private String mesType;

    private String sender;
    private String getter;
    private String con; //消息本身内容
    private String sendTime;

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public String getCon() {
        return con;
    }

    public String getGetter() {
        return getter;
    }

    public String getSender() {
        return sender;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
