package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.llk.constant.MessageConstant;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.entity.Result;
import top.llk.interfaces.RoleService;
import top.llk.pojo.Role;

import java.util.Map;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/18
 * @Content:
 */
@RestController
@RequestMapping("role")
public class RoleController {

    @Reference
    private RoleService roleService;

    /**
     * 角色分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = roleService.selectByCondition(queryPageBean);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询权限列表,菜单列表
     *
     * @return
     */
    @RequestMapping("queryRoleAndMenuList")
    public Map queryRoleAndMenuList() {
        try {
            Map<String, Object> map = roleService.queryRoleAndMenuList();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增角色,及其关联的权限,菜单列表
     *
     * @param permissionIds
     * @param menuIds
     * @param role
     */
    @RequestMapping("addRole")
    public Result addRole(Integer[] permissionIds, Integer[] menuIds, @RequestBody Role role) {
        try {
            roleService.addRole(permissionIds, menuIds, role);
            return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
    }

    /**
     * 删除role
     *
     * @param id
     * @return
     */
    @RequestMapping("deleteRoleById")
    public Result deleteRoleById(Integer id) throws Exception {
        try {
            roleService.deleteRoleById(id);
            return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
        } catch (RuntimeException e) {
            //接收异常"该角色正被使用,不可删除"
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ROLE_FAIL);
        }
    }

    /**
     * 加载编辑角色所需要的数据
     *
     * @param id
     * @return map:key: permissionList,menuList,permissionIds,menuIds
     */
    @RequestMapping("findById")
    public Map findById(Integer id) {
        try {
            Map<String, Object> map = roleService.findRoleById(id);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改角色,及其关联的权限,菜单列表
     *
     * @param permissionIds
     * @param menuIds
     * @param role
     */
    @RequestMapping("editRole")
    public Result editRole(Integer[] permissionIds, Integer[] menuIds, @RequestBody Role role) {
        try {
            roleService.editRole(permissionIds, menuIds, role);
            return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_ROLE_FAIL);
        }
    }
}
