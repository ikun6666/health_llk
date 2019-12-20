package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.llk.constant.MessageConstant;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.entity.Result;
import top.llk.interfaces.PermissionService;
import top.llk.pojo.CheckItem;
import top.llk.pojo.Permission;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/18
 * @Content:
 */

@RestController
@RequestMapping("authority")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    /**
     * 权限管理分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "findPage", method = RequestMethod.POST)
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = permissionService.selectByCondition(queryPageBean);
            return new Result(true, "", pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SYSTEM_ERROR);
        }
    }

    /**
     * 添加权限
     *
     * @param permission
     * @return
     */
    @RequestMapping("addPermission")
    public Result add(@RequestBody Permission permission) {
        try {
            permissionService.add(permission);
            return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);

        }
    }

    /**
     * 根据id删除权限
     *
     * @param id 权限id
     * @return
     * @description 1.未被角色使用, 直接删除 2.有被角色使用,不能删除
     */
    @RequestMapping("deleteById")
    public Result deleteCheckItemById(Integer id) {
        Result result = null;
        try {
            permissionService.deleteById(id);
            result = new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
        } catch (RuntimeException e) {
            //被其他检查组使用不允许删除
            result = new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
        return result;
    }

    /**
     * 编辑权限
     *
     * @param permission
     * @return
     */
    @RequestMapping("editPermissionById")
    public Result editPermissionById(@RequestBody Permission permission) {
        try {
            permissionService.editPermission(permission);
            return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }

    @RequestMapping("findById")
    public Permission findById(Integer id) {
        return permissionService.findById(id);
    }

}
