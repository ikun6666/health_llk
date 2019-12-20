package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import top.llk.dao.MenuDao;
import top.llk.dao.PermissionDao;
import top.llk.dao.RoleDao;
import top.llk.dao.UserDao;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.interfaces.UserService;
import top.llk.pojo.Menu;
import top.llk.pojo.Role;
import top.llk.pojo.User;

import java.util.*;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/12
 * @Content:
 */

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private MenuDao menuDao;


    /**
     * 根据用户名查询User
     *
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {

        /*
         * User结构:包括多个roles
         * Role结构:包括多个Permission
         * */

        //查询用户
        User user = userDao.findUserByUsername(username);
        //查询用户的角色
        Set<Role> roles = roleDao.findRolesByUserId(user.getId());
        //查询每个角色的权限
        roles.forEach(role -> {
            role.setPermissions(permissionDao.findPermissionsByRoleId(role.getId()));
        });

        //设置用户的roles属性
        user.setRoles(roles);
        return user;
    }

    /**
     * 根据用户名查询对应权限的菜单
     *
     * @param username
     * @return
     */
    @Override
    public List<Menu> findMenuByUsername(String username) {
        List<Menu> menuList = new LinkedList<>();
        //查询用户
        User user = userDao.findUserByUsername(username);
        //查询用户的角色
        Set<Role> roles = roleDao.findRolesByUserId(user.getId());
        //根据用户的每个角色查询对应的menu的一级菜单
        for (Role role : roles) {
            //用户角色id
            Integer roleId = role.getId();
            //根据角色id查询出所有menuId
            List<Integer> menuIds = roleDao.findMenuIdsByRoleId(roleId);
            //根据角色id查询一级菜单集合
            LinkedHashSet<Menu> menus = menuDao.findMenuListByRoleId(roleId);
            //遍历查询menu的二级菜单
            for (Menu menu : menus) {
                Integer parentMenuId = menu.getId();
                List<Menu> allChildrenMenu = (List<Menu>) menuDao.findMenuListByparentMenuId(parentMenuId);
                //遍历这个二级菜单集合,
                ArrayList<Menu> children = new ArrayList<>();
                for (Menu childrenMenu : allChildrenMenu) {
                    Integer childrenMenuId = childrenMenu.getId();
                    //如果菜单id集合包括这个子菜单,就添加到children集合
                    if (menuIds.contains(childrenMenuId)) {
                        children.add(childrenMenu);
                    }
                }
                menu.setChildren(children);
            }
            menuList.addAll(menus);
        }
        return menuList;
    }

    /**
     * 用户列表分页查询
     *
     * @return
     */
    @Override
    public PageResult selectByCondition(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<User> users = userDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(users.getTotal(), users.getResult());
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Override
    public void deleteUserById(Integer id, String username) throws Exception {
        User user = userDao.findUserByUsername(username);

        if (user.getId() != id) {
            userDao.deleteUserAndRoleByUserId(id);
            userDao.deleteUserByUserId(id);
        } else {
            throw new RuntimeException("不能删除当前登录用户...→_→");
        }
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @Override
    public List<Role> getRoles() {
        return userDao.getRoles();
    }

    /**
     * 添加用户
     *
     * @param roleIds
     * @param user
     * @return
     */
    @Override
    public void addUser(Integer[] roleIds, User user) {
        String password = new BCryptPasswordEncoder().encode("1234");
        user.setPassword(password);
        userDao.addUser(user);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        if (roleIds != null && roleIds.length > 0) {
            for (Integer roleId : roleIds) {
                map.put("roleId", roleId);
                userDao.addUserAndRole(map);
            }
        }
    }


    /**
     * 获取用户对应的role id
     *
     * @return
     */
    @Override
    public List<Integer> getRoleIds(Integer id) {
        return userDao.findRoleIdsByUserId(id);
    }

    /**
     * 更新用户信息
     *
     * @param roleIds
     * @param user
     * @return
     */
    @Override
    public void editUser(User user, Integer[] roleIds) {
        //删除中间表
        userDao.deleteUserAndRoleByUserId(user.getId());
        HashMap<String, Object> map = new HashMap<>();

        map.put("userId", user.getId());
        if (roleIds != null && roleIds.length > 0) {
            for (Integer roleId : roleIds) {
                //遍历,中间表插入数据
                map.put("roleId", roleId);
                userDao.addUserAndRole(map);
            }

        }
        //更新role单表
        userDao.updateUser(user);
    }


}
