layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax'], function () {
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ZTree = layui.ztree;
    var $ax = layui.ax;
    var laydate = layui.laydate;
    var admin = layui.admin;

    /**
     * 系统管理--供应商用户管理
     */
    var MgrSupplier = {
        tableId: "supplierTable",    //表格id
        condition: {
            name: "",
            examine: "",
            timeLimit: ""
        }
    };

    /**
     * 初始化表格的列
     */
    MgrSupplier.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'userId', hide: true, sort: true, title: '用户id'},
            {field: 'account', sort: true, title: '账号'},
            {field: 'name', sort: true, title: '姓名'},
            {field: 'roleName', sort: true, title: '角色'},
            {field: 'deptName', sort: true, title: '部门'},
            {field: 'email', sort: true, title: '邮箱'},
            {field: 'corporationPhone', sort: true, title: '电话'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'status', sort: true, templet: '#statusTpl', title: '状态'},
            {field: 'examine',sort:true,templet:function(d){
            	if(d.examine==0){
            		return "通过";
            	}else if(d.examine==1){
            		return "未通过";
            	}else if(d.examine==2){
            		return "待审";
            	}else if(d.examine==3){
            		return "再审";
            	}
            },title:'审核状态'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 280}
        ]];
    };
    
    // 渲染表格
    var tableResult = table.render({
        elem: '#' + MgrSupplier.tableId,
        url: Feng.ctxPath + '/supplier/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: MgrSupplier.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });
    
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        MgrSupplier.search();
    });
    
    /**
     * 点击查询按钮
     */
    MgrSupplier.search = function () {
        var queryData = {};
        queryData['name'] = $("#name").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        queryData['examine']=$("#examine").val();
        table.reload(MgrSupplier.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    MgrSupplier.exportExcel = function () {
        var checkRows = table.checkStatus(MgrSupplier.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击审核用户按钮时
     *
     * @param data 点击按钮时候的行数据
     */
    MgrSupplier.onAuditSupplier = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '供应商编辑审核',
            area: ['500px', '700px'],
            content: Feng.ctxPath + '/supplier/to_supplier_audit?userId=' + data.userId,
            end: function () {
                admin.getTempData('formOk') && table.reload(MgrSupplier.tableId);
            }
        });
    };

    /**
     * 点击删除用户按钮
     *
     * @param data 点击按钮时候的行数据
     */
    MgrSupplier.onDeleteUser = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/supplier/delete", function () {
                table.reload(MgrSupplier.tableId);
                Feng.success("删除成功!");
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("userId", data.userId);
            ajax.start();
        };
        Feng.confirm("是否删除供应商" + data.account + "?", operation);
    };

    /**
     * 分配角色
     *
     * @param data 点击按钮时候的行数据
     */
    MgrSupplier.roleAssign = function (data) {
        layer.open({
            type: 2,
            title: '角色分配',
            area: ['300px', '400px'],
            content: Feng.ctxPath + '/supplier/role_assign?userId=' + data.userId,
            end: function () {
                table.reload(MgrSupplier.tableId);
            }
        });
    };

    /**
     * 重置密码
     *
     * @param data 点击按钮时候的行数据
     */
    MgrSupplier.resetPassword = function (data) {
        Feng.confirm("是否重置密码为111111 ?", function () {
            var ajax = new $ax(Feng.ctxPath + "/supplier/reset", function (data) {
                Feng.success("重置密码成功!");
            }, function (data) {
                Feng.error("重置密码失败!");
            });
            ajax.set("userId", data.userId);
            ajax.start();
        });
    };

    /**
     * 修改用户状态
     *
     * @param userId 用户id
     * @param checked 是否选中（true,false），选中就是解锁用户，未选中就是锁定用户
     */
    MgrSupplier.changeUserStatus = function (userId, checked) {
        if (checked) {
            var ajax = new $ax(Feng.ctxPath + "/supplier/unfreeze", function (data) {
                Feng.success("解除冻结成功!");
            }, function (data) {
                Feng.error("解除冻结失败!");
                table.reload(MgrSupplier.tableId);
            });
            ajax.set("userId", userId);
            ajax.start();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/supplier/freeze", function (data) {
                Feng.success("冻结成功!");
            }, function (data) {
                Feng.error("冻结失败!" + data.responseJSON.message + "!");
                table.reload(MgrSupplier.tableId);
            });
            ajax.set("userId", userId);
            ajax.start();
        }
    };
    
    // 导出excel
    $('#btnExp').click(function () {
        MgrSupplier.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + MgrSupplier.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'audit') {
            MgrSupplier.onAuditSupplier(data);
        } else if (layEvent === 'delete') {
            MgrSupplier.onDeleteUser(data);
        } else if (layEvent === 'roleAssign') {
            MgrSupplier.roleAssign(data);
        } else if (layEvent === 'reset') {
            MgrSupplier.resetPassword(data);
        }
    });

    // 修改user状态
    form.on('switch(status)', function (obj) {

        var userId = obj.elem.value;
        var checked = obj.elem.checked ? true : false;

        MgrSupplier.changeUserStatus(userId, checked);
    });

});
