package com.community.entity;

import javax.xml.crypto.Data;
import java.util.Objects;

public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Data creatTime;
    private int commentCount;
    private double score;

    @Override
    public String   toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", creatTime=" + creatTime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscussPost that = (DiscussPost) o;
        return id == that.id && userId == that.userId && type == that.type && status == that.status && commentCount == that.commentCount && Double.compare(that.score, score) == 0 && Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(creatTime, that.creatTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, title, content, type, status, creatTime, commentCount, score);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Data creatTime) {
        this.creatTime = creatTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
