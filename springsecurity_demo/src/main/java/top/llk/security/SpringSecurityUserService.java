package top.llk.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.llk.pojo.User;

import java.util.ArrayList;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/12
 * @Content:
 */
public class SpringSecurityUserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询数据库,获得user
        User user = findUserByUname(username);

        //2.判读是否weinull
        if (user == null) {
            return null;
        }

        //3.把用户名,数据库的密码,以及查询出来的权限访问,创建UserDetails对象返回.
        ArrayList<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("add"));
        list.add(new SimpleGrantedAuthority("delete"));
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        org.springframework.security.core.userdetails.User userDetials = new org.springframework.security.core.userdetails
                .User(username, user.getPassword(), list);

        return userDetials;
    }

    //模拟
    private User findUserByUname(String username) {
        if ("admin".equals(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode("123456"));
            return user;
        }
        return null;
    }
}
