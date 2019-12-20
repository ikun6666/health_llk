package top.llk.interfaces;

import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.pojo.Menu;
import top.llk.pojo.Role;
import top.llk.pojo.User;

import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/12
 * @Content:
 */
public interface UserService {
    /**
     * 根据用户名查询User
     * @param username
     * @return
     */
    User findUserByUsername(String username);
    /**
     * 根据用户名查询对应权限的菜单
     * @param username
     * @return
     */
    java.util.List<Menu> findMenuByUsername(String username);

    /**
     * 用户列表分页查询
     *
     * @return
     */
    PageResult selectByCondition(QueryPageBean queryPageBean);

    /**
     * 根据id删除用户
     *
     * @param id
     * @param username 当前登录用户的用户名
     * @return
     */
    void deleteUserById(Integer id,String username) throws Exception;

    /**
     * 查询所有角色
     * @return
     */
    List<Role> getRoles();
    /**
     * 添加用户
     * @param roleIds
     * @param user
     * @return
     */
    void addUser(Integer[] roleIds, User user);


    /**
     * 获取用户对应的role id
     * @return
     */
    List<Integer> getRoleIds(Integer id);

    /**
     * 更新用户信息
     * @param roleIds
     * @param user
     * @return
     */
    void editUser(User user, Integer[] roleIds);
}
