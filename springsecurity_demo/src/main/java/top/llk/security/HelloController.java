package top.llk.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Version 1.0
 * @Author: Lin Liangkun
 * @Date: 2019/12/12
 * @Content:
 */

@Controller
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/add")
    //表示用户有add权限才能调用
    @PreAuthorize("hasAuthority('add')")
    public String add() {

        System.out.println("add...");
        return null;
    }

    @RequestMapping("delete")
    //用户有ROLE_ADMIN角色才能调用
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete() {
        System.out.println("delete...");
        return null;
    }

}
