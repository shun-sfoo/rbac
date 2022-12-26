$(function () {
    let types = {
        MENU: "菜单",
        FUNCTION: "功能",
        BLOCK: "区域",
    };
    let permissionGrid = $('#permissionGrid');
    permissionGrid.treegrid({
        fit: true,
        border: false,
        url: '/system/permission/list',
        idField: 'id',
        treeField: 'name',
        columns: [[
            {field: 'name', title: '名称', width: 180},
            {field: 'permissionKey', title: '标识', width: 150},
            {
                field: 'type', title: '类型', width: 80, align: 'center', formatter: function (v) {
                    return types[v];
                }
            },
            {field: 'path', title: '路径', width: 200},
            {field: 'resource', title: '资源', width: 200},
            {field: 'weight', title: '权重', align: 'center', width: 80},
            {field: 'description', title: '描述', width: 200},
            {
                field: 'enable', title: '状态', width: 80, align: 'center', formatter: function (v) {
                    return v ? "可用" : "禁用";
                }
            },
            {
                field: 'operate', title: '操作', width: 100, align: 'center', formatter: function (v, row) {
                    let buttons = [];
                    buttons.push('<a data-id="' + row.id + '" class="actions easyui-linkbutton edit" data-options="iconCls:\'icon-edit\'" >编辑</a>');
                    buttons.push('<a data-id="' + row.id + '" class="actions easyui-linkbutton delete" data-options="iconCls:\'icon-remove\'" >删除</a>');
                    return buttons.join("");
                }
            },

        ]],
        toolbar: [{
            text: '创建权限',
            iconCls: 'icon-add',
            handler: function () {
                formDialog();
            }
        }]
    });

    let gridPanel = permissionGrid.treegrid("getPanel");

    // 给操作按钮绑定事件
    gridPanel.on("click", "a.edit", function () {
        let id = this.dataset.id;
        formDialog(id);
    }).on("click", "a.delete", function () {
        let id = this.dataset.id;
        $.messager.confirm("提示", "是否删除?", function (r) {
            if (r) {
                $.get("/system/permission/delete?id=" + id).done(function () {
                    // 删除成功
                    permissionGrid.treegrid("reload");
                });
            }
        })
    });

    /**
     * 表单窗口
     */
    function formDialog(id) {
        let dialog = $("<div/>").dialog({
            title: (id ? '编辑' : '创建') + '权限',
            href: '/system/permission/' + (id ? 'load?id=' + id : 'form'),
            width: 380,
            height: 450,
            onClose: function () {
                //销毁窗口
                $(this).dialog("destroy");
            },
            buttons: [{
                text: '保存',
                handler: function () {
                    let permissionForm = $("#permissionForm");
                    if (permissionForm.form("validate")) {
                        $.post("/system/permission/" + (id ? 'update' : 'save'),
                            permissionForm.serialize()
                        ).done(function () {
                            permissionGrid.treegrid("reload");
                            dialog.dialog('close');
                        });
                    }
                }
            }]
        })
    }
});