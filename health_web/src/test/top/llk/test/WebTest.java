package top.llk.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.llk.controller.CheckGroupController;
import top.llk.controller.CheckItemController;
import top.llk.pojo.CheckGroup;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/4
 * @Content:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:springmvc.xml")
public class WebTest {
    @Autowired
    public CheckItemController checkItemController;
    @Autowired
    private CheckGroupController checkGroupController;

    @Test
    public void testDeleteCheckItemById() {

        checkItemController.deleteCheckItemById("98");
    }


    @Test
    public void testFindAll() {
        System.out.println(checkItemController.findAll());
    }

    @Test
    public void testde() {
        CheckGroup checkGroup = new CheckGroup();
        checkGroup.setId(28);
        checkGroup.setCode("188");
        checkGroup.setName("测试修改2");
        checkGroup.setAttention("测试修改2");
        checkGroup.setRemark("测试修改2");

        checkGroupController.editCheckGroup(checkGroup,null);
    }
}

