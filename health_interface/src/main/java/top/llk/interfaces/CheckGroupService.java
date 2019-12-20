package top.llk.interfaces;

import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.pojo.CheckGroup;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/5
 * @Content:
 */
public interface CheckGroupService {

    PageResult findPage(QueryPageBean queryPageBean);

    void addCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) throws Exception;


    /**
     * 根据id删除检查组
     *
     * @param id
     * @return
     */
    void deleteCheckGroupById(Integer id);

    /**
     * 接收编辑请求,根据id查询检查组,回传检查组信息
     *
     * @param id
     * @return
     */
    CheckGroup findCheckGroupById(Integer id);

    /**
     * 根据id查询检查项ids
     *
     * @param id
     * @return
     */
    Integer[] findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 修改检查组
     *
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    void editCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds);
    /**
     * 查找所有检查组
     * @return
     */
    PageResult findAll();
}
