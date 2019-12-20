package top.llk.interfaces;

import top.llk.entity.Result;
import top.llk.pojo.Member;

import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/12
 * @Content:
 */
public interface MemberService {
    /**
     * 校验登录
     *
     * @param telephone
     * @return
     */
    Result loginCheck(String telephone);

    /**
     * 统计查询每月的会员数量
     *
     * @param list
     * @return
     */
    List<Integer> findMemberCountByMonth(List<String> list);

    /**
     * 用手机号查找用户是否已注册
     * @param telephone
     * @return
     */
    Member findMemberByTelephone(String telephone);

    /**
     * 添加用户
     * @param member
     */
    void add(Member member);
}
