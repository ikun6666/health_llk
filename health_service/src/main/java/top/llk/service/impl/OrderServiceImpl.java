package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.sun.tools.javac.api.ClientCodeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.llk.constant.MessageConstant;
import top.llk.dao.MemberDao;
import top.llk.dao.OrderDao;
import top.llk.dao.OrderSettingDao;
import top.llk.entity.Result;
import top.llk.interfaces.OrderService;
import top.llk.pojo.Member;
import top.llk.pojo.Order;
import top.llk.pojo.OrderSetting;
import top.llk.util.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/11
 * @Content:
 */

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;


    /**
     * 预约体检
     *
     * @param map
     * @return
     */
    @Override
    public Result order(Map map) throws Exception {
        //map数据:
        //idCard: "123132111323231231"
        //name: "张三"
        //orderDate: "2019-12-12"
        //setmealId: "12"
        //sex: "1"
        //telephone: "13771526398"
        //validateCode: "1234"


        //1.所选日期是否可预约,或已预约满
        String orderDate = (String) map.get("orderDate");
//        Date date = DateUtils.parseString2Date(orderDate, "yyyy-MM-dd");
        //根据所选日期查询当日的可预约情况
        OrderSetting orderSetting = orderSettingDao.queryOrderSettingByDate(orderDate);
        if (orderSetting == null || orderSetting.getNumber() == 0) {
            //所选日期不可预约,或预约数为0
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        if (!(orderSetting.getNumber() > orderSetting.getReservations())) {
            //可预约人数<=已预约人数
            return new Result(false, MessageConstant.ORDER_FULL);
        }


        //2.0根据手机号查询是否重复预约
        String telephone = (String) map.get("telephone");
        //2.1.1查询是否是会员,如果是会员查询是否已重复预约
        Member member = memberDao.findByTelephone(telephone);
        String strSetmealId = (String) map.get("setmealId");
        Integer setmealId = Integer.valueOf(strSetmealId);
        if (member != null) {
            //2.1.2 查询出今日日期之后的所有预约信息,判断已预约的日期和套餐是否重复

            Order order = new Order(member.getId(), DateUtils.parseString2Date((String) map.get("orderDate"),
                    "yyyy-MM-dd"), null, null, setmealId);
            //2.1.3 封装order去查询订单
            List<Order> orders = orderDao.findByCondition(order);
            if (orders != null && orders.size() > 0) {
                //重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
            //2.1.4 执行到此说明未重复预约
        } else {
            //2.2 不是会员则先注册会员,封装一个member
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber((String) map.get("telephone"));
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //3.执行预约操作
        try {
            Order order = new Order(member.getId(), DateUtils.parseString2Date((String) map.get("orderDate")),
                    Order.ORDERTYPE_WEIXIN, Order.ORDERSTATUS_NO, setmealId);
            orderSetting.setReservations(orderSetting.getReservations() + 1);
            //编辑预约表
            orderSettingDao.updateOrders(orderSetting);
            //预约设置中 已预约数+1
            orderDao.add(order);
            return new Result(true, MessageConstant.ORDER_SUCCESS, order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }

    /**
     * 查询预约详情
     *
     * @param orderId
     * @return
     */
    @Override
    public Result findById4Detail(Integer orderId) throws Exception {
        Map map = orderDao.findById4Detail(orderId);
        if (map != null) {
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        }
        return null;
    }


}
