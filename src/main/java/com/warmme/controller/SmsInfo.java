package com.warmme.controller;

import java.io.Serializable;

public class SmsInfo implements Serializable {

    /**
     * 接收者电话号码
     */
    private String receiverPhone;

    /**
     * 发送者电话号码
     */
    private String senderPhone;

    /**
     * 短信内容
     */
    private String msg;

    /**
     * 短信存储时间
     */
    private long storTime;

    /**
     * 短信接收时间
     */
    private long receiveTime;

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getStorTime() {
        return storTime;
    }

    public void setStorTime(long storTime) {
        this.storTime = storTime;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }


}
