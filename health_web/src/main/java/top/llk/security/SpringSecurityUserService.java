package top.llk.security;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import top.llk.interfaces.UserService;
import top.llk.pojo.Permission;
import top.llk.pojo.Role;
import top.llk.pojo.User;

import java.util.ArrayList;
import java.util.Set;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/12
 * @Content:
 */

@Component
public class SpringSecurityUserService implements UserDetailsService {


    public static void main(String[] args) {
        String encode = new BCryptPasswordEncoder().encode("1234");
        System.out.println(encode);
    }
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.调用业务, 根据用户名查询
        User user  =  userService.findUserByUsername(username);
        if(user == null){
            return null;
        }

        /*
         * User结构:包括多个roles
         * Role结构:包括多个Permission
         * */

        //2.进行授权
        ArrayList<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            //取出每个permission的keyword,赋值添加给权限集合
            permissions.forEach(permission ->
                    grantedAuthorityList.add(new SimpleGrantedAuthority(permission.getKeyword())));
        }


        //3.创建UserDetails对象返回
        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails
                .User(username, user.getPassword(), grantedAuthorityList);

        return userDetail;
    }
}
