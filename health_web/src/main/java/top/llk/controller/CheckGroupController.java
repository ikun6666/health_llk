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
import top.llk.interfaces.CheckGroupService;
import top.llk.pojo.CheckGroup;

import java.util.HashMap;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/5
 * @Content:
 */
@RestController
@RequestMapping("checkGroup")
public class CheckGroupController {


    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 查找所有检查组,返回给新建套餐的页面
     *
     * @return
     */
    @RequestMapping("findAll")
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    public PageResult findAll() {
        return checkGroupService.findAll();
    }

    /**
     * 查询检查组
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("findPage")
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkGroupService.findPage(queryPageBean);
    }

    /**
     * 添加检查组
     *
     * @param checkGroup
     * @return
     */
    @RequestMapping("addCheckGroup")
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    public Result addCheckGroup(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {

            checkGroupService.addCheckGroup(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据id删除检查组
     *
     * @param id
     * @return
     */
    @RequestMapping("deleteCheckGroupById")
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    public Result deleteCheckGroupById(Integer id) {
        try {
            checkGroupService.deleteCheckGroupById(id);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 接收编辑请求,根据id查询检查组,回传检查组信息
     *
     * @param id
     * @return
     */
    @RequestMapping("findCheckGroupById")
    public HashMap<String, Object> findCheckGroupById(Integer id) {
        CheckGroup checkGroup = checkGroupService.findCheckGroupById(id);
        Integer[] checkitemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("checkGroup", checkGroup);
        map.put("checkitemIds", checkitemIds);
        return map;
    }

    /**
     * 修改检查组
     *
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("editCheckGroup")
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    public Result editCheckGroup(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.editCheckGroup(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }
}
