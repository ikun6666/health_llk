package top.llk.daoTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.llk.dao.SetmealDao;

import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/14
 * @Content:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-dao.xml")
public class SetmealDaoTest {
    @Autowired
    private SetmealDao setmealDao;

    @Test
    public void testCountOredersBySetmealId() {
        List<Map<String,Object>> list = (List<Map<String, Object>>) setmealDao.countOredersGroupBySetmeal();
        System.out.println(list);
    }
}
