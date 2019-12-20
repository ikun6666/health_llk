package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.llk.constant.MessageConstant;
import top.llk.dao.MemberDao;
import top.llk.entity.Result;
import top.llk.interfaces.MemberService;
import top.llk.pojo.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/12
 * @Content:
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements top.llk.interfaces.MemberService {
    @Autowired
    private MemberDao memberDao;

    /**
     * 校验登录
     *
     * @param telephone
     * @return
     */
    @Override
    public Result loginCheck(String telephone) {
        //1.查询是否已注册过,如果已注册则登录成功
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } else {
            //2.未注册则先注册,然后登录成功;
            member.setPhoneNumber(telephone);
            Date date = new Date();
            member.setRegTime(date);
            memberDao.add(member);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);

        }

    }

    /**
     * 统计查询每月的会员数量
     *
     * @param list
     * @return
     */

    @Override
    public List<Integer> findMemberCountByMonth(List<String> list) {
        List<Integer> countList = new ArrayList<>();
        for (String month : list) {
            Integer count = memberDao.findMemberCountByMonth(month + "-31");
            countList.add(count);
        }
        return countList;
    }

    /**
     * 用手机号查找用户是否已注册
     *
     * @param telephone
     * @return
     */
    @Override
    public Member findMemberByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 添加用户
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }
}
