package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.llk.constant.MessageConstant;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.entity.Result;
import top.llk.interfaces.UserService;
import top.llk.pojo.Menu;
import top.llk.pojo.Role;

import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/13
 * @Content:
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 获取当前登录用户的用户名
     *
     * @return
     */
    @RequestMapping("getUsername")
    public Result getUsername() {
        try {
            //获取当前登录用户的用户名
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);

        }
    }

    /**
     * 获取用户响应权限的左侧menu
     *
     * @return
     */
    @RequestMapping("getMenu")
    public Result getMenu() {
        try {
            //获取用户响应权限的左侧menu
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Menu> menuList = userService.findMenuByUsername(user.getUsername());
            return new Result(true, MessageConstant.GET_MENU_SUCCESS, menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);

        }
    }

    /**
     * 用户列表分页查询
     *
     * @return
     */

    @RequestMapping("findPage")
    @PreAuthorize("hasAuthority('USER_QUERY')")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = userService.selectByCondition(queryPageBean);
            return new Result(true, MessageConstant.GET_USER_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USER_FAIL);
        }
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("deleteUserById")
    @PreAuthorize("hasAuthority('USER_DELETE')")

    public Result deleteUserById(Integer id) {
        try {
            //获取当前登录的管理员,
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //在删除用户前权限校验,不能删除管理员
            userService.deleteUserById(id, user.getUsername());
            return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
        } catch (RuntimeException e) {
            //接收异常:不能删除当前用户
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_USER_FAIL);
        }
    }


    /**
     * 查询所有角色
     *
     * @return
     */
    @RequestMapping("getRoles")
    @PreAuthorize("hasAuthority('ROLE_QUERY')")

    public List<Role> getRoles() {
        return userService.getRoles();
    }

    /**
     * 添加用户
     *
     * @param roleIds
     * @param user
     * @return
     */
    @RequestMapping("addRole")
    @PreAuthorize("hasAuthority('USER_ADD')")

    public Result addRole(Integer[] roleIds, @RequestBody top.llk.pojo.User user) {
        try {
            userService.addUser(roleIds, user);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
    }

    /**
     * 获取用户对应的role id
     *
     * @return
     */
    @RequestMapping("getRoleIds")
    @PreAuthorize("hasAuthority('USER_EDIT')")

    public List<Integer> getRoleIds(Integer id) {
        return userService.getRoleIds(id);
    }

    /**
     * 更新用户信息
     * @param roleIds
     * @param user
     * @return
     */
    @RequestMapping("edit")
    @PreAuthorize("hasAuthority('USER_EDIT')")

    public Result edit(Integer[] roleIds, @RequestBody top.llk.pojo.User user) {
        try {
            userService.editUser(user, roleIds);
            return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();            return new Result(true, MessageConstant.EDIT_USER_SUCCESS);

        }

    }
}
