package com.community.service;

import com.community.dao.DiscussPostMapper;
import com.community.entity.DiscussPost;
import com.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author aptx
 */
@Service
public class DiscussPostServer {

    @Autowired
    private DiscussPostMapper discussPostMapper;


    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 通过用户id获取该用户所发送的帖子内容
     *
     * @param userId 用户id
     * @param offset 帖子的起始编号
     * @param limit  帖子的最大数量
     * @return 帖子集合
     */
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    /**
     * 通过用户id获取该用户的总发帖数
     *
     * @param userId 用户id
     * @return 总发帖数
     */
    public int findDiscussPostSize(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * 添加帖子
     * @param post 帖子实体类
     */
    public void addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        StringBuffer content = new StringBuffer(post.getContent());
        StringBuffer title = new StringBuffer(post.getTitle());

        sensitiveFilter.replace(content);
        sensitiveFilter.replace(title);
        post.setContent(content.toString());
        post.setTitle(title.toString());
        discussPostMapper.insertDiscussPost(post);

    }

    /**
     * 通过帖子的id编号获取该帖子的具体内容
     * @param id 帖子的id编号
     * @return 帖子的实体类
     */
    public DiscussPost getDiscussPost(int id) {
        List<DiscussPost> discussPost = discussPostMapper.selectDiscussPostById(id);
        if (discussPost.size() == 1) {
            return discussPost.get(0);
        }
        return null;
    }

    /**
     * 通过帖子的编号修改帖子的评论数目
     * @param id 帖子的编号
     * @param newCount 新的评论数目
     */
    public void changeCommentCount(int id, int newCount) {
        discussPostMapper.updateCommentCountById(id, newCount);
    }

}
