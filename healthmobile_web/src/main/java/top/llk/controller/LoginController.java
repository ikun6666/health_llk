package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import top.llk.constant.MessageConstant;
import top.llk.constant.RedisMessageConstant;
import top.llk.entity.Result;
import top.llk.interfaces.MemberService;
import top.llk.pojo.Member;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/12
 * @Content:
 */
@RestController
@RequestMapping("login")
public class LoginController {
    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 校验登录
     *
     * @param map
     * @return
     */
    @RequestMapping("check")
    public Result loginCheck(HttpServletResponse response, @RequestBody Map map) {
        //校验手机号和验证码是否正确
        //前端数据:{telephone: "15606957027", validateCode: "1234"}
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");

        String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (!(redisValidateCode != null && redisValidateCode.equals(validateCode))) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);

        }


        //
        try {

            //查询用户,如果未注册则直接注册
            Member member = memberService.findMemberByTelephone(telephone);
            if (member == null) {
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            //登录验证
            Result result = memberService.loginCheck(telephone);
            Cookie cookie = new Cookie("telephone", telephone);
            //路径
            cookie.setPath("/");
            //生命时间
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.LOGIN_FAIL);
        }
    }
}
