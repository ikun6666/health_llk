package top.llk.daoTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.llk.dao.UserDao;
import top.llk.pojo.User;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/13
 * @Content:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-dao.xml")
public class UserDaoTest {


    @Autowired
    private UserDao userDao;
    @Test
        public void testFindByUSERNAME() {
        User admin = userDao.findUserByUsername("admin");
        System.out.println(admin);
    }
}

