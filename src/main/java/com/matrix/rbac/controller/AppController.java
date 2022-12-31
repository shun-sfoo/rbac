package com.matrix.rbac.controller;

import com.matrix.rbac.common.Menu;
import com.matrix.rbac.model.dao.PermissionDao;
import com.matrix.rbac.model.dao.UserDao;
import com.matrix.rbac.model.entity.Permission;
import com.matrix.rbac.model.entity.Role;
import com.matrix.rbac.model.entity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/")
public class AppController {
    @Autowired
    private UserDao userDao;

    @Value("${system.super.user.id}")
    private Long superId;

    @Autowired
    private PermissionDao permissionDao;


    @RequestMapping
    public String index(@SessionAttribute(value = "user", required = false) User user) {
        if (user == null) {
            // 未登录
            return "login";
        }
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String account, @RequestParam String password, HttpSession session, RedirectAttributes rda) {
        User user = userDao.findFirstByAccount(account);
        if (user != null && user.getEnable()) {
            if (user.getPassword().equals(DigestUtils.md5Hex(password))) {
                Set<Permission> permissions;
                if (Objects.equals(superId, user.getId())) {
                    permissions = permissionDao.findAllByEnableOrderByWeightDesc(true);
                } else {
                    //获取用户菜单
                    Set<Role> roles = user.getRoles();
                    permissions = new HashSet<>();
                    roles.forEach(role -> permissions.addAll(role.getPermissions()));
                }

                // 存储菜单
                TreeSet<Permission> menus = new TreeSet<>((o1, o2) -> {
                    if (Objects.equals(o1.getWeight(), o2.getWeight())) {
                        return -1;
                    }
                    return o1.getWeight() - o2.getWeight();
                });

                // 存储权限keys
                Set<String> keys = new HashSet<>();

                permissions.forEach(permission -> {
                    if (permission.getEnable()) {
                        if (permission.getType().equals(Permission.Type.MENU)) {
                            menus.add(permission);
                        }
                        keys.add(permission.getPermissionKey());
                    }
                });

                //树形数据转换
                List<Menu> menuList = new ArrayList<>();
                menus.forEach(permission -> {
                    Menu m = new Menu();
                    m.setId(permission.getId());
                    m.setText(permission.getName());
                    m.setHref(permission.getPath());
                    m.setParentId(permission.getParent() == null ? null : permission.getParent().getId());
                    menuList.add(m);
                });

                session.setAttribute("user", user);
                session.setAttribute("menus", menuList);
                session.setAttribute("keys", keys);

            } else {
                rda.addFlashAttribute("error", "帐号和密码不匹配");
            }
        } else {
            rda.addFlashAttribute("error", "账户不可用!");
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/menus")
    @ResponseBody
    public List<Menu> menus(@SessionAttribute("menus") List<Menu> menuList) {
        return menuList;
    }
}