package top.llk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.llk.dao.MenuDao;
import top.llk.dao.PermissionDao;
import top.llk.dao.RoleDao;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.interfaces.RoleService;
import top.llk.pojo.Menu;
import top.llk.pojo.Permission;
import top.llk.pojo.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/18
 * @Content:
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private MenuDao menuDao;

    /**
     * 角色分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult selectByCondition(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Role> roles = roleDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(roles.getTotal(), roles.getResult());
    }

    /**
     * 查询权限列表,菜单列表
     *
     * @return
     */
    @Override
    public Map<String, Object> queryRoleAndMenuList() {
        Map<String, Object> map = new HashMap<>();
        //获取permissionList
        Page<Permission> permissions = permissionDao.selectByCondition("");
        List<Permission> permissionList = permissions.getResult();
        map.put("permissionList", permissionList);

        //获取menuList
        Page<Menu> menus = menuDao.selectByCondition("");
        List<Menu> menuList = menus.getResult();
        map.put("menuList", menuList);
        return map;
    }

    /**
     * 新增角色,及其关联的权限,菜单列表
     *
     * @param permissionIds
     * @param menuIds
     * @param role
     */
    @Override
    public void addRole(Integer[] permissionIds, Integer[] menuIds, Role role) {
        HashMap<String, Object> map = new HashMap<>();
        //1.添加role,获得自增的role id
        roleDao.addRole(role);
        Integer roleId = role.getId();
        map.put("roleId", roleId);
        checkAndAddMenuAndPermission(permissionIds, menuIds, map);
    }

    /**
     * 操作菜单具有特殊性:不能单独添加子菜单,如果是子菜单必须先添加父菜单,再添加子菜单
     *
     * @param permissionIds
     * @param menuIds
     * @param map
     */
    private void checkAndAddMenuAndPermission(Integer[] permissionIds, Integer[] menuIds, HashMap<String, Object> map) {
        //2.添加menu菜单
        //2.1判断菜单的层级,查询父标签id
        for (Integer id : menuIds) {
            Integer parentMenuId = menuDao.selectParentMenuIdByPrimaryKey(id);
            if (parentMenuId == null) {
                //没有父标签,说明是一级标签,可以直接绑定添加
                map.put("menuId", id);
                roleDao.addRoleAndMenu(map);
            } else {
                //有父标签,说明是二级标签
                //2.2判断父标签id是否已绑定,未绑定则绑定,已绑定则直接添加子标签
                map.put("parentMenuId", parentMenuId);
                Integer count = roleDao.haveSelected(map);
                if (count != null) {
                    //父标签已绑定,直接绑定子标签
                    map.put("menuId", id);
                    roleDao.addRoleAndMenu(map);
                } else {
                    //父菜单未绑定,先绑定父菜单
                    map.put("menuId", parentMenuId);
                    roleDao.addRoleAndMenu(map);
                    //绑定子菜单
                    map.put("menuId", id);
                    roleDao.addRoleAndMenu(map);
                }
            }
        }
        //3.添加权限,操作中间表即可
        for (Integer permissionId : permissionIds) {
            map.put("permissionId", permissionId);
            roleDao.addRoleAndPermission(map);
        }
    }

    /**
     * 删除role,包括两个中间表和role单表的数据
     *
     * @param id
     */
    @Override
    public void deleteRoleById(Integer id) throws Exception {
        //先判断role是否与user关联,如已关联,则抛出异常"该角色正被使用,不可删除"
        int count = roleDao.countBeUsed(id);
        if (count > 0) {
            throw new RuntimeException("该角色正被使用,不可删除");
        }
        roleDao.deleteRoleAndMenuByRoleId(id);
        roleDao.deleteRoleAndPermissionByRoleId(id);
        roleDao.deleteRoleByRoleId(id);
    }

    /**
     * 加载编辑角色所需要的数据
     *
     * @param id 角色id
     * @return map:key: permissionList,menuList,permissionIds,menuIds
     */
    @Override
    public Map<String, Object> findRoleById(Integer id) {
        Map<String, Object> map = queryRoleAndMenuList();
        List<Integer> permissionIds = roleDao.findPermissionIdsByRoleId(id);
        map.put("permissionIds", permissionIds);
        List<Integer> menuIds = roleDao.findMenuIdsByRoleId(id);
        map.put("menuIds", menuIds);
        return map;
    }

    /**
     * 修改角色,及其关联的权限,菜单列表
     *
     * @param permissionIds
     * @param menuIds
     * @param role
     */
    @Override
    public void editRole(Integer[] permissionIds, Integer[] menuIds, Role role) {
        //删除中间表数据
        roleDao.deleteRoleAndMenuByRoleId(role.getId());
        roleDao.deleteRoleAndPermissionByRoleId(role.getId());
        //添加中间表数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("roleId", role.getId());
        //调用公共方法
        checkAndAddMenuAndPermission(permissionIds, menuIds, map);

        //修改role单表

        roleDao.update(role);
    }
}
