package com.community;

import com.community.dao.DiscussPostMapper;
import com.community.dao.UserMapper;
import com.community.entity.DiscussPost;
import com.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void select() {
    }

    @Test
    public void insert() {
        User user = new User();
        user.setEmail("adsfd");
        user.setUsername("sasdasd");
        User user1 = userMapper.selectById(150);
        userMapper.insertUser(user);
        System.out.println(user1);
    }

    @Test
    public void testPost() {
        int i = discussPostMapper.selectDiscussPostRows(1514);
        System.out.println(i);
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(1514, 0, 10);
        discussPosts.forEach(System.out::println);
    }


}
