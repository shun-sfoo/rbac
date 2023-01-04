$(function () {
    let infoGrid = $('#infoGrid');
    let infoGridAction = $('#infoGridAction');
    let searchFrom = $("#info_search_form");

    infoGrid.datagrid({
        fit: true,
        border: false,
        emptyMsg: "无数据",
        url: '/bus/info/list',
        singleSelect: true,
        pagination: true,
        rownumbers: true,
        pageSize: 20,
        columns: [[
            {field: 'salesDate', title: '日期', width: 90},
            {field: 'customer', title: '收货客户', width: 250},
            {field: 'operator', title: '开单员', width: 80},
            {field: 'productCode', title: '商品编号', width: 80},
            {field: 'productName', title: '商品名称', width: 150},
            {field: 'specification', title: '商品规格', width: 100},
            {field: 'productLocation', title: '商品产地', width: 300},
            {field: 'productUnit', title: '单位', width: 50},
            {field: 'provider', title: '供应商名称', width: 180},
            {field: 'batchNo', title: '批号', width: 80},
            {field: 'availableDate', title: '有效期至', width: 80},
            {field: 'num', title: '数量', width: 50},
            {field: 'price', title: '核算成本价', width: 80},
            {
                field: 'operate', title: '操作', width: 100, align: 'center', formatter: function (v, row) {
                    return infoGridAction.children("a.actions").attr('data-id', row.id).end().html();
                }
            },

        ]],
        toolbar: "#infoGridToolbar"
    });

    let gridPanel = infoGrid.datagrid("getPanel");

    // 给操作按钮绑定事件
    gridPanel.on("click", "a.edit", function () {
        let id = this.dataset.id;
        formDialog(id);
    }).on("click", "a.delete", function () {
        let id = this.dataset.id;
        $.messager.confirm("提示", "是否删除?", function (r) {
            if (r) {
                $.get("/bus/info/delete?id=" + id).done(function () {
                    // 删除成功
                    infoGrid.datagrid("reload");
                });
            }
        })
    }).on("click", "a.import", function () {
        $("#infoUpload").click();
    }).on("click", "a.export", function () {
        let xhr = new XMLHttpRequest();
        xhr.responseType = "arraybuffer";
        xhr.open("get", "/bus/info/export?" + searchFrom.serialize(), true);
        xhr.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                let blob = new Blob([xhr.response], {
                    type: "application/vnd.ms-excel",
                });
                let link = document.createElement("a");
                link.href = window.URL.createObjectURL(blob);
                link.download = name;
                link.click();
                window.URL.revokeObjectURL(link.href);
            }
        };
        xhr.send();
    });

    $("#infoUpload").click(function () {
        this.value = "";
    }).on('change', function () {
        let formData = new FormData();
        formData.append('file', this.files.item(0));
        $.ajax("/bus/info/upload", {
            type: 'post',
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            complete: function (res) {
                console.log(res);
                let result = res.responseJSON;
                $.messager.alert("系统提示", result.msg);
                infoGrid.datagrid("reload");
            }
        })
    });

    searchFrom.on('click', 'a.searcher', function () {//检索
        infoGrid.datagrid('load', '/bus/info/list?' + searchFrom.serialize());
    }).on('click', 'a.reset', function () {//重置
        searchFrom.form('clear');
        infoGrid.datagrid('load', '/bus/info/list?' + searchFrom.serialize());
        // infoGrid.datagrid("reload");
    });

    function formDialog(id) {
        let dialog = $("<div/>").dialog({
            title: '编辑',
            href: '/bus/info/load?id=' + id,
            width: 380,
            height: 650,
            modal: true,
            onClose: function () {
                //销毁窗口
                $(this).dialog("destroy");
            },
            buttons: [{
                text: '保存',
                handler: function () {
                    let infoForm = $("#infoForm");
                    $.post("/bus/info/update",
                        infoForm.serialize()
                    ).done(function () {
                        infoGrid.datagrid("reload");
                        dialog.dialog('close');
                    });
                }
            }]
        })
    }
});