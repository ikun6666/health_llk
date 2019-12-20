package top.llk.daoTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.llk.dao.MemberDao;
import top.llk.dao.PermissionDao;
import top.llk.pojo.Member;
import top.llk.pojo.Order;
import top.llk.pojo.Permission;
import top.llk.util.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/11
 * @Content:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-dao.xml")
public class MemberDaoTest {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private PermissionDao permissionDao;


    @Test
    public void testQueryOrderByMemberId() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", 84);
        String todayDate = DateUtils.parseDate2String(new Date());
        map.put("todayDate", todayDate);
        List<Order> orders = memberDao.queryOrderByMemberIdAndDate(map);
        orders.forEach(System.out::println);
    }


    @Test
    public void testRegisterMember() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("idCard", "123131311313131341");


    }

    @Test
    public void testFindByTelephone() {
        Member byTelephone = memberDao.findByTelephone("18511279942");
        System.out.println(byTelephone);
    }

    @Test
    public void testAdd() {
        Member member = new Member();
        member.setName("周杰伦");
        memberDao.add(member);
        System.out.println(member.getId());
    }

    @Test
    public void testpd() {
        Permission permission = new Permission();
        permission.setId(27);
        permission.setName("测试项名2");
//        permission.setDescription("测试修改");
        permission.setDescription("");
        permissionDao.editPermission(permission);
    }

}
