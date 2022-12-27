$(function () {
    let roleGrid = $('#roleGrid');
    let rolePermissionPanel = $("#rolePermissionPanel");
    let rolePermissionTree = $("#rolePermissionTree");
    let currentRow;
    roleGrid.datagrid({
        fit: true,
        border: false,
        url: '/system/role/list',
        singleSelect:true,
        columns: [[
            {field: 'roleName', title: '名称', width: 180},
            {field: 'roleKey', title: '标识', width: 150},
            {field: 'description', title: '描述', width: 200},
            {
                field: 'enable', title: '状态', width: 80, align: 'center', formatter: function (v) {
                    return v ? "可用" : "禁用";
                }
            },
            {
                field: 'operate', title: '操作', width: 100, align: 'center', formatter: function (v, row) {
                    let buttons = [];
                    buttons.push('<a data-id="' + row.id + '" class="actions  edit" >编辑</a>');
                    buttons.push('<a data-id="' + row.id + '" class="actions  delete">删除</a>');
                    return buttons.join("");
                }
            },

        ]],
        toolbar: [{
            text: '创建角色',
            iconCls: 'icon-add',
            handler: function () {
                formDialog();
            }
        }],
        onSelect: function (index, row) {
            // 记录当前选中行
            currentRow = row;
            rolePermissionPanel.panel("setTitle", "为[" + row.roleName + "]分配权限");

            // 取消以前选中的项目
            $.each(rolePermissionTree.tree("getChecked"), function () {
                rolePermissionTree.tree("uncheck", this.target)
            });

            // 加载当前选择角色的已有权限
            $.get("/system/role/permission/" + row.id, function (data) {
                $.each(data, function () {
                    let node = rolePermissionTree.tree("find", this.id);
                    //只有当前权限是树的叶子节点是才执行节点的check方法
                    if (rolePermissionTree.tree("isLeaf", node.target)) {
                        rolePermissionTree.tree("check", node.target);
                    }
                });
            });
        }
    });

    let gridPanel = roleGrid.datagrid("getPanel");

    // 给操作按钮绑定事件
    gridPanel.on("click", "a.edit", function () {
        let id = this.dataset.id;
        formDialog(id);
    }).on("click", "a.delete", function () {
        let id = this.dataset.id;
        $.messager.confirm("提示", "是否删除?", function (r) {
            if (r) {
                $.get("/system/role/delete?id=" + id).done(function () {
                    // 删除成功
                    roleGrid.datagrid("reload");
                });
            }
        })
    });

    /**
     * 表单窗口
     */
    function formDialog(id) {
        let dialog = $("<div/>").dialog({
            title: (id ? '编辑' : '创建') + '角色',
            href: '/system/role/' + (id ? 'load?id=' + id : 'form'),
            width: 380,
            height: 250,
            onClose: function () {
                //销毁窗口
                $(this).dialog("destroy");
            },
            buttons: [{
                text: '保存',
                handler: function () {
                    let roleForm = $("#roleForm");
                    if (roleForm.form("validate")) {
                        $.post("/system/role/" + (id ? 'update' : 'save'),
                            roleForm.serialize()
                        ).done(function () {
                            roleGrid.datagrid("reload");
                            dialog.dialog('close');
                        });
                    }
                }
            }]
        })
    }

    rolePermissionTree.tree({
        url: '/system/role/permission/tree',
        checkbox: true
    });

    // 权限保存的按钮事件
    $("#rolePermissionSave").on('click', function () {
        if (currentRow) {
            // 获取打勾和实心的节点
            let nodes = rolePermissionTree.tree("getChecked", ["checked", "indeterminate"]);
            let permissionIds = [];
            $.each(nodes, function () {
                permissionIds.push(this.id);
            });
            let params = "roleId=" + currentRow.id + "&permissionId=" + permissionIds.join("&permissionId=");
            $.post("/system/role/permission/save", params, function (resp) {
                if (resp.success) {
                    $.messager.alert("提示", "授权成功!");
                }
            })
        }
    });
});