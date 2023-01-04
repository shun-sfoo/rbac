$(function () {
    let userGrid = $('#userGrid');
    let userGridAction = $('#userGridAction');

    userGrid.datagrid({
        fit: true,
        border: false,
        url: '/system/user/list',
        singleSelect: true,
        pagination: true,
        rownumbers: true,
        columns: [[
            {field: 'account', title: '账户', width: 180},
            {field: 'userName', title: '姓名', width: 150},
            {field: 'tel', title: '电话', width: 200},
            {
                field: 'roles', title: '角色', width: 200, formatter: function (v) {
                    let roles = [];
                    $.each(v, function () {
                        roles.push(this.roleName);
                    });
                    return roles.join(",");
                }
            },
            {
                field: 'enable', title: '状态', width: 80, align: 'center', formatter: function (v) {
                    return v ? "可用" : "禁用";
                }
            },
            {
                field: 'operate', title: '操作', width: 100, align: 'center', formatter: function (v, row) {
                    return userGridAction.children("a.actions").attr('data-id', row.id).end().html();
                }
            },

        ]],
        toolbar: "#userGridToolbar"
    });

    let gridPanel = userGrid.datagrid("getPanel");

    // 给操作按钮绑定事件
    gridPanel.on("click", "a.edit", function () {
        let id = this.dataset.id;
        formDialog(id);
    }).on("click", "a.delete", function () {
        let id = this.dataset.id;
        $.messager.confirm("提示", "是否删除?", function (r) {
            if (r) {
                $.get("/system/user/delete?id=" + id).done(function () {
                    // 删除成功
                    userGrid.datagrid("reload");
                });
            }
        })
    }).on("click", "a.create", function () {
        formDialog();
    });

    /**
     * 表单窗口
     */
    function formDialog(id) {
        let dialog = $("<div/>").dialog({
            title: (id ? '编辑' : '创建') + '用户',
            href: '/system/user/' + (id ? 'load?id=' + id : 'form'),
            width: 380,
            height: 350,
            modal: true,
            onClose: function () {
                //销毁窗口
                $(this).dialog("destroy");
            },
            buttons: [{
                text: '保存',
                handler: function () {
                    let userForm = $("#userForm");
                    if (userForm.form("validate")) {
                        $.post("/system/user/" + (id ? 'update' : 'save'),
                            userForm.serialize()
                        ).done(function () {
                            userGrid.datagrid("reload");
                            dialog.dialog('close');
                        });
                    }
                }
            }]
        })
    }

});