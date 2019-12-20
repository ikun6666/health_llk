package top.llk.daoTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.llk.dao.OrderSettingDao;
import top.llk.pojo.OrderSetting;
import top.llk.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/9
 * @Content:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-dao.xml")
public class OrderSettingDaoTest {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Test
    public void testQueryOrderSettings() {
        List<OrderSetting> orderSettings = orderSettingDao.queryOrderSettings("2019-05%");
        orderSettings.forEach(System.out::println);
    }

    @Test
    public void testUpdateOrders() throws Exception {
        OrderSetting orderSetting = new OrderSetting();
        orderSetting.setNumber(40);
        orderSetting.setOrderDate(DateUtils.parseString2Date("2019-12-12"));
        System.out.println(orderSetting);
        orderSettingDao.updateOrders(orderSetting);
    }
}
