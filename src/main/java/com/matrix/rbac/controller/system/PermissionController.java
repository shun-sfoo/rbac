package com.matrix.rbac.controller.system;

import com.matrix.rbac.common.JsonResult;
import com.matrix.rbac.model.dao.PermissionDao;
import com.matrix.rbac.model.entity.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/system/permission")
@Slf4j
@Transactional(readOnly = true)
public class PermissionController {
    @Autowired
    private PermissionDao permissionDao;

    @RequestMapping
    public void index() {
    }

    @RequestMapping({"/form", "/load"})
    public String form(Long id, Model model) {
        if (id != null) {
            //编辑
            model.addAttribute("permission", permissionDao.findById(id).get());
        }
        return "system/permission/form";
    }

    @PostMapping({"/list", "/combo"})
    @ResponseBody
    public List<Permission> list() {
        List<Permission> roots = permissionDao.findAllByParentIsNull();
        this.revers(roots);
        return roots;
    }


    @PostMapping({"/save", "/update"})
    @ResponseBody
    @Transactional
    public JsonResult save(@Validated Permission permission, BindingResult br) {
        if (!br.hasErrors()) {
            permissionDao.save(permission);
            return JsonResult.success();
        }
        return JsonResult.error("校验不通过! ");
    }

    @GetMapping("/delete")
    @ResponseBody
    @Transactional
    public JsonResult delete(Long id) {
        if (permissionDao.findById(id).isPresent()) {
            permissionDao.deleteById(id);
            return JsonResult.success();
        }
        return JsonResult.error("数据不存在!");
    }

    private void revers(List<Permission> nodes) {
        for (Permission root : nodes) {
            List<Permission> children = permissionDao.findAllByParent(root);
            this.revers(children);
            root.setChildren(children);
        }
    }
}
