package com.community.entity;

import java.util.Date;
import java.util.Objects;

public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type = 0;
    private int status = 0;
    private Date createTime;
    private int commentCount;
    private double score;

    @Override
    public String
    toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createTime=" + createTime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscussPost that = (DiscussPost) o;
        return id == that.id && userId == that.userId && type == that.type && status == that.status && commentCount == that.commentCount && Double.compare(that.score, score) == 0 && Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, title, content, type, status, createTime, commentCount, score);
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
/*
* comments
*   list cvo
*       comment
*       user
*       replyCount
*       replys rvo
*           reply Comment对象
*           user 发送者对象
*           target 发送评论的目标对象
*
* */
