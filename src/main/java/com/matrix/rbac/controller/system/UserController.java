package com.matrix.rbac.controller.system;


import com.matrix.rbac.common.JsonResult;
import com.matrix.rbac.model.dao.RoleDao;
import com.matrix.rbac.model.dao.UserDao;
import com.matrix.rbac.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/system/user")
@Slf4j
public class UserController {
    @Autowired
    private UserDao userDao;


    @Autowired
    private RoleDao roleDao;

    @RequestMapping
    public void index() {
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int rows) {
        PageRequest pr = PageRequest.of(page - 1, rows, Sort.Direction.DESC, "id");
        Page<User> pageData = userDao.findAll(pr);
        Map<String, Object> data = new HashMap<>();
        data.put("total", pageData.getTotalElements());
        data.put("rows", pageData.getContent());
        return data;
    }

    @RequestMapping({"/form", "/load"})
    public String form(Long id, Model model) {
        if (id != null) {
            //编辑
            model.addAttribute("user", userDao.findById(id).get());
        }

        model.addAttribute("roles", roleDao.findAllByEnable(true));
        return "system/user/form";
    }

    @PostMapping({"/save", "/update"})
    @ResponseBody
    @Transactional
    public JsonResult save(@Validated User user, BindingResult br) {
        if (!br.hasErrors()) {
            if (user.getId() == null) { // 创建
                user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            } else { // 更新
                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    User org = userDao.findById(user.getId()).get();
                    user.setPassword(org.getPassword());
                } else {
                    user.setPassword(DigestUtils.md5Hex(user.getPassword()));
                }
            }
            userDao.save(user);
            return JsonResult.success();
        }
        return JsonResult.error("校验不通过! ");
    }

    @GetMapping("/delete")
    @ResponseBody
    @Transactional
    public JsonResult delete(Long id) {
        if (userDao.findById(id).isPresent()) {
            userDao.deleteById(id);
            return JsonResult.success();
        }
        return JsonResult.error("数据不存在!");
    }

    @PostMapping("/check")
    @ResponseBody
    public String check(String account) {
        if (userDao.countByAccount(account) == 0) {
            return "true";
        }
        return "false";
    }
}
