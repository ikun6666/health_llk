package top.llk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.llk.constant.MessageConstant;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.entity.Result;
import top.llk.interfaces.CheckItemService;
import top.llk.pojo.CheckItem;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/3
 * @Content:
 */

@Controller
@RequestMapping("checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    /**
     * 添加检查项
     *
     * @param checkItem 检查项
     * @return
     */
    @RequestMapping("add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public @ResponseBody
    Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);

        }
    }


    /**
     * 查询检查项,返回list
     *
     * @param
     * @return
     */
    @RequestMapping("findPage")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public @ResponseBody
    Result findPage(@RequestBody QueryPageBean queryPageBean) {
//        System.out.println("findPage   执行了.....");
//        System.out.println(queryPageBean);
        Result result = null;
        try {
            PageResult pageResult = checkItemService.findPage(queryPageBean);
            result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return result;
    }

    /**
     * 根据id删除checkitem
     *
     * @param id
     * @return
     * @description 1.未被检查组使用 2.有被检查组使用
     */
    @RequestMapping("deleteCheckItemById")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public @ResponseBody
    Result deleteCheckItemById(String id) {
        Result result = null;
        try {
            checkItemService.deleteCheckItemById(id);
            result = new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (RuntimeException e) {
            //被其他检查组使用不允许删除
            result = new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return result;
    }

    /**
     * 编辑检查项
     *
     * @param checkItem
     */
    @RequestMapping("editCheckItem")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public @ResponseBody
    Result editCheckItem(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.editCheckItem(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        }
    }

    /**
     * 新增检查组时动态查询所有检查项
     * @return
     */
    @RequestMapping("findAll")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public @ResponseBody PageResult findAll() {
        return checkItemService.findAll();
    }
}
