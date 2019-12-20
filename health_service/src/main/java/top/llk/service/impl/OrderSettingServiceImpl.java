package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import top.llk.dao.OrderSettingDao;
import top.llk.interfaces.OrderSettingService;
import top.llk.pojo.OrderSetting;
import top.llk.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/9
 * @Content:
 */

@Service
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量修改数据库中的预约时间,预约数量
     *
     * @param list
     * @return
     */
    @Override
    public void setOrders(List<String[]> list) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        for (String[] strings : list) {
            String orderDate = strings[0];
            String number = strings[1];
//            map.put("orderDate", orderDate);
//            map.put("number", number);
            //修改,返回修改行数
            //todo 更新前,检查已预约日期的预约人数,可预约必须>已预约
            OrderSetting orderSetting = new OrderSetting();
            orderSetting.setOrderDate(DateUtils.parseString2Date(orderDate,"yyyy-MM-dd"));
            orderSetting.setNumber(Integer.parseInt(number));
            int update = orderSettingDao.updateOrders(orderSetting);
            if (update < 1) {
                //说明数据库里没有对应的日期,说明要插入这条数据
                orderSettingDao.insertOrder(orderSetting);
            }
        }
    }

    /**
     * 查询年.月的预约情况,回传给页面
     *
     * @param
     * @return
     */
    @Override
    public List<Map<String, Object>> queryMonthOrders(Integer currentYear, Integer currentMonth) {
        //拼接日期,dao层模糊查找 like 2019-05%
        String date = null;
        if (currentMonth < 10) {
            date = currentYear + "-0" + currentMonth + "%";
        } else {
            date = currentYear + "-" + currentMonth + "%";
        }
        List<OrderSetting> orderSettings = orderSettingDao.queryOrderSettings(date);
        List<Map<String, Object>> maps = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettings) {
            HashMap<String, Object> map = new HashMap<>();
            /**this.leftobj = [
             { date: 1, number: 120, reservations: 1 },
             { date: 3, number: 120, reservations: 1 },
             { date: 4, number: 120, reservations: 120 },
             { date: 6, number: 120, reservations: 1 },
             { date: 8, number: 120, reservations: 1 }
             ];*/
            //转成前端需要的格式
            map.put("date", orderSetting.getOrderDate().getDate());
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            maps.add(map);
        }
        return maps;
    }

    /**
     * 设置某一天的约束数
     *
     * @param number
     * @param day
     * @return
     */
    @Override
    public void setNumber(Date day, Integer number) throws Exception {
        //处理日期格式
        String date = new SimpleDateFormat("yyyy-MM-dd").format(day);
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("orderDate", date);
//        map.put("number", number);

        OrderSetting orderSetting = new OrderSetting(DateUtils.parseString2Date(date), number);
        //调用dao
        int update = orderSettingDao.updateOrders(orderSetting);
        if (update < 1) {
            orderSettingDao.insertOrder(orderSetting);
        }
    }
}
