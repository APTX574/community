package com.community;

import com.community.dao.DemoDao;
import com.community.dao.DemoDaoImpl;
import com.community.dao.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {

	@Autowired
	private UserMapper userMapper;
	private ApplicationContext applicationContext;
	@Test
	void testContextLoads() {
		userMapper.selectById(1);
		DemoDao dao=applicationContext.getBean(DemoDao.class);
		System.out.println(dao.select());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
}
