package top.llk.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import top.llk.pojo.Role;
import top.llk.pojo.User;

import java.util.HashMap;
import java.util.List;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/13
 * @Content:
 */
public interface UserDao {

    /**
     * 根据用户名查询User
     *
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 用户列表分页查询
     *
     * @return
     */
    Page<User> selectByCondition(String queryString);

    /**
     * 根据id删除用户和角色的关系表数据
     *
     * @param id
     * @return
     */
    @Delete("delete from t_user_role where user_id = #{id}")
    void deleteUserAndRoleByUserId(Integer id);

    /**
     * 删除user内单表数据
     *
     * @param id
     */
    @Delete("delete from t_user where id =#{id}")
    void deleteUserByUserId(Integer id);

    /**
     * 查询所有角色
     *
     * @return
     */
    @Select("select * from t_role")
    List<Role> getRoles();

    /**
     * 添加用户
     *
     * @param user
     */
    void addUser(User user);

    /**
     * UserAndRole中间表添加数据
     *
     * @param map key:userId,roleId
     */
    @Insert("insert into t_user_role set user_id =#{userId},role_id = #{roleId}")
    void addUserAndRole(HashMap<String, Object> map);

    /**
     * 根据用户id查询对应的角色id
     * @param id
     * @return
     */
    @Select("SELECT role_id FROM t_user_role where user_id = #{id}")
    List<Integer> findRoleIdsByUserId(Integer id);

    /**
     * 更新用户信息
     * @param user
     */
    void updateUser(User user);
}
