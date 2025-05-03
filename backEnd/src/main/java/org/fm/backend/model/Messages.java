package org.fm.backend.model;

import java.util.Date;

public class Messages {
    private int messageID;
    private int senderID;
    private int receiverID;
    private String messageType;
    private String content;
    private Date sendTime;
    private int isRead;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageID=" + messageID +
                ", senderID=" + senderID +
                ", receiverID=" + receiverID +
                ", messageType='" + messageType + '\'' +
                ", content='" + content + '\'' +
                ", sendTime=" + sendTime +
                ", isRead=" + isRead +
                '}';
    }
}
