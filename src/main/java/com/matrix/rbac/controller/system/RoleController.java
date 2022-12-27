package com.matrix.rbac.controller.system;

import com.matrix.rbac.common.JsonResult;
import com.matrix.rbac.model.dao.PermissionDao;
import com.matrix.rbac.model.dao.RoleDao;
import com.matrix.rbac.model.entity.Permission;
import com.matrix.rbac.model.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/system/role")
@Slf4j
public class RoleController {

    @Autowired
    private RoleDao roleDao;


    @Autowired
    private PermissionDao permissionDao;

    @RequestMapping
    public void index() {
    }

    @RequestMapping("/list")
    @ResponseBody
    public Iterable list() {
        return roleDao.findAll();
    }

    @RequestMapping({"/form", "/load"})
    public String form(Long id, Model model) {
        if (id != null) {
            //编辑
            model.addAttribute("role", roleDao.findById(id).get());
        }
        return "system/role/form";
    }

    @PostMapping({"/save", "/update"})
    @ResponseBody
    @Transactional
    public JsonResult save(@Validated Role role, BindingResult br) {
        if (!br.hasErrors()) {
            roleDao.save(role);
            return JsonResult.success();
        }
        return JsonResult.error("校验不通过! ");
    }

    @GetMapping("/delete")
    @ResponseBody
    @Transactional
    public JsonResult delete(Long id) {
        if (roleDao.findById(id).isPresent()) {
            roleDao.deleteById(id);
            return JsonResult.success();
        }
        return JsonResult.error("数据不存在!");
    }

    @RequestMapping("/permission/tree")
    @ResponseBody
    public List<Permission> permissionTree() {
        List<Permission> permissions = permissionDao.findAllByParentIsNull();
        return permissionDao.findAllByParentIsNull();
    }

    /**
     * 获取角色对应的权限列表
     *
     * @param id
     * @return
     */
    @RequestMapping("/permission/{id}")
    @ResponseBody
    public Set<Permission> permission(@PathVariable("id") Long id) {
        return roleDao.findById(id).get().getPermissions();
    }

    @RequestMapping("/permission/save")
    @Transactional
    @ResponseBody
    public JsonResult permissionSave(Long roleId, Long[] permissionId) {
        Role role = roleDao.findById(roleId).get();
        //先清除已有角色
        role.getPermissions().clear();
        Arrays.stream(permissionId).forEach(pid -> role.getPermissions().add(permissionDao.findById(pid).get()));
        roleDao.save(role);
        return JsonResult.success("授权成功!");
    }
}