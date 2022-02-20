package com.community.dao;

import com.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author aptx
 */
@Mapper
public interface DiscussPostMapper {

    /**
     * @param userId 查找的帖子的用户id，若是id为0则查找全部
     * @param offset 分页的起始位置
     * @param limit 分页的每页页数
     * @return 帖子的实体类的集合
     */
    List<DiscussPost> selectDiscussPosts(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);


    /**
     * 若参数列表只有一个参数且是动态sql的参数(在if中使用)，则必须添加@Param注解起别名。
     * @param userId 查找帖子数量的用户id
     * @return 当前id的总帖子数量
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 添加帖子
     * @param discussPost 帖子对象
     * @return 改变条数
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 通过帖子的id查找帖子的详细内容
     * @param id 贴子的id
     * @return 查找条数
     */
    List<DiscussPost> selectDiscussPostById(int id);

    int updateCommentCountById(int id,int newCount);

}
