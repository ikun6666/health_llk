package top.llk.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Select;
import org.jboss.netty.channel.ChannelHandler;
import top.llk.pojo.Member;
import top.llk.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/11
 * @Content:
 */
public interface MemberDao {


    /**
     * 根据用户id查询所有预约
     *
     * @param map :Integer :id;String :todayDate
     * @return
     */
    @Select("select * from t_order where member_id = #{memberId} and orderDate > #{todayDate}")
    List<Order> queryOrderByMemberIdAndDate(Map<String, Object> map);

    /**
     * 注册用户信息
     *
     * @param member
     */
    void add(Member member);


    List<Member> findAll();

    Page<Member> selectByCondition(String queryString);

    void deleteById(Integer id);

    Member findById(Integer id);

    /**
     * 根据身份证查询用户
     *
     * @param telephone
     * @return
     */
    @Select("select * from t_member where phoneNumber = #{value}")
    Member findByTelephone(String telephone);

    void edit(Member member);

    /**
     * 查询指定日期前的人数
     * @param date
     * @return
     */
    @Select("select Count(*) from t_member where regTime < #{value}")
    Integer findMemberCountBeforeDate(String date);

    /**
     * 本日新增会员数
     * @param date 2019-12-14
     * @return
     */
    @Select("SELECT COUNT(*) FROM t_member WHERE regTime= #{value}")
    Integer findMemberCountByDate(String date);

    Integer findMemberCountAfterDate(String date);

    /**
     * 统计会员总数
     * @return
     */
    @Select("SELECT COUNT(*) FROM t_member")
    Integer findMemberTotalCount();


    /**
     * 统计查询每月的会员数量
     *
     * @param month 2019-12
     * @return
     */
    Integer findMemberCountByMonth(String month);

    List<Integer> findage();

    List<Map> memberbygenderCount();

}
