package com.matrix.rbac.controller.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system/role")
@Slf4j
public class RoleController {

    @RequestMapping
    public void index() {
    }
}
