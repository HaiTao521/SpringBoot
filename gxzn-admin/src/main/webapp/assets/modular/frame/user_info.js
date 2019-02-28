/**
 * 地址详情对话框
 */
var AddressInfoDlg = {
    data: {
        pid: "",
        pName: ""
    }
};
layui.use(['form', 'upload', 'element', 'ax', 'laydate','viewerjs'], function () {
    var $ = layui.jquery;
    var form = layui.form;
    var upload = layui.upload;
    var element = layui.element;
    var $ax = layui.ax;
    var laydate = layui.laydate;
    var $Viewerjs = layui.viewerjs;

    //渲染时间选择框
    laydate.render({
        elem: '#birthday'
    });
    laydate.render({
        elem: '#foundTime'
    });
    laydate.render({
        elem: '#validityTime'
    });

    // 添加表单验证方法
    form.verify({
        phone: [/^1(3|4|5|7|8)\d{9}$/, '请输入正确的手机号'],
    });

    //普通图片上传
    var uploadInst = upload.render({
      elem: '#licencePic_click'
      ,url: Feng.ctxPath + '/supplier/singleUpload/'
      ,before: function(obj){
        //预读本地文件示例，不支持ie8
        obj.preview(function(index, file, result){
          // $('#licencePic_show').attr('src', result); //图片链接（base64）
        });
      }
      ,done: function(res){
        // 上传结果
        if(res.code == 200){
            var lpic_smallImg = res.data.substring(0, res.data.lastIndexOf(".")) + "-small" + res.data.substring(res.data.lastIndexOf("."))
            $("#licencePic_show").attr({
                "data-imgurl" :  res.data,
                "src" :  lpic_smallImg
            });
            var viewerjs = new $Viewerjs("licencePic_show");
            viewerjs.setOptions({
                "url" : "data-imgurl"
            });
            viewerjs.init();

            $("#licencePic").val(res.data);
          return layer.msg('上传成功');
        } else {
          return layer.msg('上传失败');
        }
      }
      ,error: function(){
        //演示失败状态，并实现重传
        var demoText = $('#licencePic_rep');
        demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
        demoText.find('.demo-reload').on('click', function(){
          uploadInst.upload();
        });
      }
    });

    var authorizePic="";
    var viewerjs_up = null;
    //多图片上传
    upload.render({
    elem: '#authorizePic_click'
        ,url: Feng.ctxPath + '/supplier/singleUpload/'
        ,multiple: true
        ,before: function(obj){
        //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                // $('#authorizePic').append('<img src="'+ result +'" alt="'+ file.name +'" class="layui-upload-img">')
            });
        }
        ,done: function(res){
            //如果上传成功
            if(res.code == 200){
                authorizePic += res.data + ",";
                $("#authorizePic").val(authorizePic.substring(0,authorizePic.lastIndexOf(',')));
                var auth_smallImg = res.data.substring(0, res.data.lastIndexOf(".")) + "-small" + res.data.substring(res.data.lastIndexOf("."));

                var div_img_uuid = guid();
                $('#authorizePic_show').append('<div id="' + div_img_uuid + '"><img src="'+ auth_smallImg +'"class="layui-upload-img" data-imgurl="'+ res.data +'"></div>');

                var viewerjs = new $Viewerjs(div_img_uuid);
                viewerjs.setOptions({
                    "url" : "data-imgurl"
                });
                viewerjs.init();

                $("#authorizePic_cancel").attr("style","");
            } else {
                return layer.msg('上传失败');
            }
        }
    });

    //用于生成uuid
    function S4() {
        return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    }
    function guid() {
        return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
    }

    // 让当前iframe弹层高度适应
    // admin.iframeAuto();

    // 点击上级地址时
    $('#pName').click(function () {
        var formName = encodeURIComponent("parent.AddressInfoDlg.data.name");
        var formId = encodeURIComponent("parent.AddressInfoDlg.data.pId");
        var treeUrl = encodeURIComponent(Feng.ctxPath + "/address/addressTree");

        layer.open({
            type: 2,
            title: '父级地址',
            area: ['300px', '90%'],
            content: Feng.ctxPath + '/system/commonTree?formName=' + formName + "&formId=" + formId + "&treeUrl=" + treeUrl,
            end: function () {
                $("#pid").val(AddressInfoDlg.data.pId);
                $("#pName").val(AddressInfoDlg.data.name);
            }
        });
    });

    //获取用户详情
    var ajax = new $ax(Feng.ctxPath + "/system/currentUserInfo");
    var result = ajax.start();
    var userType = result.data.userType;
    if (userType == 0) {
        $("#supplierPage_show").hide();
    }
    //用这个方法必须用在class有layui-form的元素上
    form.val('userInfoForm', result.data);

    //用户表单提交事件
    form.on('submit(userInfoSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/mgr/edit", function (data) {
            Feng.success("修改成功!");
        }, function (data) {
            Feng.error("修改失败!" + data.responseJSON.message + "!");
        });
        ajax.set(data.field);

        ajax.start();
    });

    //获取用户商家详情
    if (userType == 1) {
        var ajaxSupplier = new $ax(Feng.ctxPath + "/supplier/getSupplierInfo?userId="+$("#userId").val());
        var resultSupplier = ajaxSupplier.start();
        var licencePic_val = resultSupplier.data.licencePic;
        if (licencePic_val != null && licencePic_val != "") {
            // var lpic_smallImg = licencePic_val.substring(0, licencePic_val.lastIndexOf(".")) + "-small" + licencePic_val.substring(licencePic_val.lastIndexOf("."))
            $("#licencePic_show").attr({
                "data-imgurl" :   resultSupplier.data.licencePicPath,
                "src" :  resultSupplier.data.licencePicThumbnailPath
            });
            var viewerjs = new $Viewerjs("licencePic_show");
            viewerjs.setOptions({
                "url" : "data-imgurl"
            });
            viewerjs.init();
        }

        var authorizePic_val = resultSupplier.data.authorizePic;
        if (authorizePic_val != null && authorizePic_val != "") {
            authorizePic = authorizePic_val + ",";
            // var split = authorizePic_val.split(",");
            for (var i = 0; i < resultSupplier.data.authorizePicPaths.length; i++) {
                var div_img_uuid = guid();
                $('#authorizePic_show').append('<div id="'+ div_img_uuid +'"><img src="'+ resultSupplier.data.authorizePicThumbnailPaths[i] +'"class="layui-upload-img" data-imgurl="'+ resultSupplier.data.authorizePicPaths[i] +'"></div>');

                var viewerjs1 = new $Viewerjs(div_img_uuid);
                viewerjs1.setOptions({
                    "url" : "data-imgurl"
                });
                viewerjs1.init();
            }
            $("#authorizePic_cancel").attr("style","display: ");
        }

        //用这个方法必须用在class有layui-form的元素上
        form.val('supplierInfoForm', resultSupplier.data);
    }

    $("#authorizePic_cancel").click(function () {
        layer.confirm('即将清空全部文件，是否继续？', {
            btn: ['确定', '取消']
            }, function () {
                //移除、初始化、隐藏
                authorizePic = "";
                $("#authorizePic").val("");
                $("#authorizePic_show div").remove();
                $("#authorizePic_cancel").attr("style","display: none");
                layer.closeAll('dialog');  //加入这个信息点击确定 会关闭这个消息框
                layer.msg("全部清空!",{ icon: 1, time: 1000 });
            }
        );

    });

    //供应商表单提交事件
    form.on('submit(supplierSumbit)', function (data) {

        var ajax = new $ax(Feng.ctxPath + "/supplier/supplier_update", function (data) {
            Feng.success("修改成功!");
        }, function (data) {
            Feng.error("修改失败!" + data.responseJSON.message + "!");
        });
        ajax.set(data.field);
        ajax.start();
    });

    upload.render({
        elem: '#imgHead',
        url: '', // 上传接口
        done: function (res) {
            // 上传完毕回调
        },
        error: function () {
            // 请求异常回调
        }
    });
});