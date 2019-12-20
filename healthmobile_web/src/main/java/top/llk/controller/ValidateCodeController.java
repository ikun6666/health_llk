package top.llk.controller;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import top.llk.constant.MessageConstant;
import top.llk.constant.RedisMessageConstant;
import top.llk.entity.Result;
import top.llk.util.SMSUtils;
import top.llk.util.ValidateCodeUtils;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/10
 * @Content:
 */
@RestController
@RequestMapping("validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("send4Order")
    public Result send4Order(String telephone) {
        try {
            if (true) {
                String validateCode = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode);
                //设置超时  setex0(key,seconds,value)  == .expire(key,seconds)
                jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER, 5*60*12*12*2*5,validateCode);
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } else {
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }

        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    @RequestMapping("send4Login")
    public Result send4Login(String telephone) {
        try {
            if (true) {
                String validateCode = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode);
                //设置超时  setex0(key,seconds,value)  == .expire(key,seconds)
                jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN, 5*60*12*12*2*5,validateCode);
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } else {
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }

        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
