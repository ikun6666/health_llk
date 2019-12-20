package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import sun.misc.MessageUtils;
import top.llk.constant.MessageConstant;
import top.llk.constant.RedisMessageConstant;
import top.llk.entity.Result;
import top.llk.interfaces.OrderService;
import top.llk.util.SMSUtils;

import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/10
 * @Content:
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping("submit")
    public Result submitOrder(@RequestBody Map map) {
//        idCard: "123132111323231231"
//name: "张三"
//orderDate: "2019-12-12"
//setmealId: "12"
//sex: "1"
//telephone: "13771526398"
//validateCode: "1234"

        //校验身份证...略

        //校验日期...略

        //手机号和验证码都不能为空
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        if (telephone == null || validateCode == null || "".equals(telephone) || "".equals(validateCode)) {
            return new Result(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
        }
        //验证码输入校对
        String rightValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if (!validateCode.equals(rightValidateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;


        try {
            //调用业务,执行预约
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, MessageConstant.ORDER_FAIL);
        }
        //预约成功,发送短信
        if (result.isFlag()) {
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @RequestMapping(value = "findById",method = RequestMethod.POST)
    public Result findById(Integer id) {

        try {
            Result result = orderService.findById4Detail(id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }
}
