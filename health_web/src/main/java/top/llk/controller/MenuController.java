package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.llk.constant.MessageConstant;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.entity.Result;
import top.llk.interfaces.MenuService;
import top.llk.pojo.Menu;

import javax.print.attribute.standard.PrinterURI;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/18
 * @Content:
 */

@RestController
@RequestMapping("menu")
public class MenuController {
    @Reference
    private MenuService menuService;

    /**
     * 菜单列表分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("findPage")
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = menuService.selectByCondition(queryPageBean);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_SUCCESS);

        }
    }

    /**
     * 删除菜单项
     *
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @PreAuthorize("hasAuthority('MENU_DELETE')")

    public Result delete(Integer id) {
        try {
            menuService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_MENU_SUCCESS);
        } catch (RuntimeException e) {
            //捕获:1.菜单使用中,无法直接删除 2.不能直接删除非空的一级菜单
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MENU_FAIL);
        }
    }

    /**
     * 添加菜单
     *
     * @param menu
     * @return
     */
    @RequestMapping("add")
    @PreAuthorize("hasAuthority('MENU_ADD')")

    public Result addMenu(@RequestBody Menu menu) {
        try {
            menuService.addMenu(menu);
            return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MENU_FAIL);

        }
    }

    /**
     * 新建菜单时动态加载所有父菜单
     *
     * @return
     */
    @RequestMapping("getParentMenuList")
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    public List<Menu> getParentMenuList() {
        return menuService.getParentMenuList();
    }

    /**
     * 编辑菜单
     *
     * @param menu
     * @return
     */
    @RequestMapping("edit")
    @PreAuthorize("hasAuthority('MENU_EDIT')")

    public Result editMenu(@RequestBody Menu menu) {
        try {
            menuService.editMenu(menu);
            return new Result(true, MessageConstant.EDIT_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MENU_FAIL);

        }
    }

}
