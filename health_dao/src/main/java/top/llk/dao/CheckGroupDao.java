package top.llk.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import top.llk.entity.PageResult;
import top.llk.pojo.CheckGroup;

import java.util.HashMap;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/5
 * @Content:
 */
public interface CheckGroupDao {
    /**
     * 分页搜索检查组
     *
     * @param queryString
     * @return
     */
    Page<CheckGroup> selectByCondition(String queryString);

    /**
     * 添加检查组
     *
     * @param checkGroup
     * @return
     */
    void addCheckGroup(CheckGroup checkGroup);

    /**
     * 添加检查组与检查项关联
     *
     * @param map :checkGroupId  检查组id   checkitemId 关联检查项id
     */
    void addCheckGroupAndCheckitem(HashMap<String, Object> map);


    /**
     * 根据id删除检查组
     *
     * @param id
     * @return
     */
    @Delete("delete from t_checkgroup where id= #{id}")
    void deleteCheckGroupById(Integer id);

    /**
     * 根据检查组的id删除检查组与检查项的中间表
     *
     * @param id
     */
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id= #{id}")
    void deleteCheckGroupAndCheckItemById(Integer id);

    /**
     * 根据检查组id查询检查组
     *
     * @param id
     * @return
     */
    CheckGroup findCheckGroupById(Integer id);

    /**
     * 根据套餐id查询检查组
     *
     * @param id
     * @return
     */
    CheckGroup findCheckGroupBySetmealId(Integer id);

    /**
     * 根据id查询检查项ids
     *
     * @param id
     * @return
     */
//    @Select("select checkitem_id id from t_checkgroup_checkitem where checkgroup_id= #{id}") //ok
    Integer[] findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 修改检查组单表
     *
     * @param checkGroup
     */
    void editCheckGroup(CheckGroup checkGroup);

    /**
     * 查找所有检查组
     *
     * @return
     */
    @Select("select * from t_checkgroup")
    List<CheckGroup> findALL();
}
