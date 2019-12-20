package top.llk.daoTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.llk.dao.OrderDao;
import top.llk.pojo.Order;
import top.llk.pojo.OrderSetting;
import top.llk.util.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/11
 * @Content:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-dao.xml")
public class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    //     * 根据所选日期查询当日的可预约情况


    @Test
    public void testDate() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("orderDate", "2019-12-12");
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate, "yyyy-MM-dd");
        System.out.println(date);

    }

    @Test
    public void testFindByCondition() throws Exception {
        Order order = new Order(84, DateUtils.parseString2Date("2019-04-28", "yyyy-MM-dd"), null, null, 12);
        List<Order> orders = orderDao.findByCondition(order);
        orders.forEach(System.out::println);
    }

    @Test
    public void testAdd() {
        Order order = new Order();
        order.setMemberId(84);
        order.setSetmealId(12);
        orderDao.add(order);
    }
}
