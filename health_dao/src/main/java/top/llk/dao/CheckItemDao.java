package top.llk.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jboss.netty.channel.ChannelHandler;
import top.llk.entity.PageResult;
import top.llk.entity.QueryPageBean;
import top.llk.pojo.CheckItem;

import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/3
 * @Content:
 */
public interface CheckItemDao {

    List<CheckItem> queryCheckItems(QueryPageBean queryPageBean);

    @Insert("insert into t_checkitem (id,code,name,sex,age,price,type,attention,remark) values (null,#{code},#{name}," +
            "#{sex},#{age},#{price},#{type},#{attention},#{remark})")
    void add(CheckItem checkItem);


    Long countItems(QueryPageBean queryPageBean);

    /**
     * 查询检查项被几个检查组使用
     *
     * @param id
     * @return
     */
    @Select("select count(*) from t_checkgroup_checkitem where checkitem_id = #{id}")
    int queryBeUsedCountById(String id);

    /**
     * 根据id删除检查项
     *
     * @param id
     */
    @Update("delete from t_checkitem where id =#{id}")
    void deleteCheckItemById(String id);

    /**
     * 编辑检查项
     *
     * @param checkItem
     */
    @Update("update t_checkitem set code=#{code},name=#{name},sex=#{sex},age=#{age},price=#{price},type=#{type}," +
            "attention=#{attention},remark=#{remark} where id =#{id}")
    void editCheckItem(CheckItem checkItem);

    /**
     * 查询所有检查项
     *
     * @return
     */
    @Select("select * from t_checkitem")
    List<CheckItem> findAllCheckItem();

    /**
     * 查询所有检查项的数量
     *
     * @return
     */
    @Select("select count(*) from t_checkitem")
    Long countAllCheckItem();

    /**
     * 根据检查组id查询检查项
     *
     * @param id
     * @return
     */
    List<CheckItem> findCheckItemByCheckGroupId(Integer id);
}
