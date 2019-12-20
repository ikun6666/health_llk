package top.llk.interfaces;

import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.pojo.Role;

import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/18
 * @Content:
 */
public interface RoleService {
    /**
     * 角色分页查询
     * @param queryPageBean
     * @return
     */
    PageResult selectByCondition(QueryPageBean queryPageBean);
    /**
     * 查询权限列表,菜单列表
     * @return
     */
    Map<String,Object> queryRoleAndMenuList();

    /**
     * 新增角色,及其关联的权限,菜单列表
     * @param permissionIds
     * @param menuIds
     * @param role
     */
    void addRole(Integer[] permissionIds, Integer[] menuIds, Role role);

    /**
     * 删除role
     * @param id
     */
    void deleteRoleById(Integer id) throws Exception;

    /**
     * 加载编辑角色所需要的数据
     * @param id
     * @return map:key: permissionList,menuList,permissionIds,menuIds
     */
    Map<String,Object> findRoleById(Integer id);

    /**
     * 修改角色,及其关联的权限,菜单列表
     *
     * @param permissionIds
     * @param menuIds
     * @param role
     */
    void editRole(Integer[] permissionIds, Integer[] menuIds, Role role);
}
