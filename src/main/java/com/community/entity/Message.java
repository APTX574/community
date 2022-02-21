package com.community.entity;

import java.util.Date;
import java.util.Objects;

/**
 * 私信消息实体类
 * @author aptx
 */
public class Message {
    int id;
    int fromId;
    int toId;
    String conversationId;
    String content;
    int status;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", conversationId='" + conversationId + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id && fromId == message.fromId && toId == message.toId && status == message.status && Objects.equals(conversationId, message.conversationId) && Objects.equals(content, message.content) && Objects.equals(createTime, message.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromId, toId, conversationId, content, status, createTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Message() {
    }

    public Message(int id, int fromId, int toId, String conversationId, String content, int status, Date createTime) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.conversationId = conversationId;
        this.content = content;
        this.status = status;
        this.createTime = createTime;
    }

    Date createTime;
}
