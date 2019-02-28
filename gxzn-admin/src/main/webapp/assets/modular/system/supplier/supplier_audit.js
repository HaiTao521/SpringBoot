layui.use([ 'layer', 'form', 'admin', 'laydate','element','ax', 'viewerjs','laytpl' ],
		function() {
			var $ = layui.jquery;
			var $ax = layui.ax;
			var form = layui.form;
			var admin = layui.admin;
			var laydate = layui.laydate;
			var layer = layui.layer;
			var $Viewerjs = layui.viewerjs;
			var laytpl = layui.laytpl;

			// 让当前iframe弹层高度适应
			admin.iframeAuto();

			// 获取用户信息
			var ajax = new $ax(Feng.ctxPath
					+ "/supplier/getSupplierInfo?userId="
					+ Feng.getUrlParam("userId"));
			var result = ajax.start();
			form.val('userForm', result.data);
			$("input:radio[value='"+result.data.examine+"']").attr("checked",true);
			laytpl(authorizePicTpl.innerHTML).render(result.data,function(html){
				document.getElementById('images').innerHTML=html;
				var viewerjs1 = new $Viewerjs("images");
				viewerjs1.setOptions({
					"url" : "data-imgurl"
				});
				viewerjs1.init();
			});
			$("#licencePic").attr({
				"data-imgurl" : result.data.licencePicPath,
				"src" : result.data.licencePicThumbnailPath
			});
			var viewerjs = new $Viewerjs("licencePic");
			viewerjs.setOptions({
				"url" : "data-imgurl"
			});
			viewerjs.init();

			// 渲染时间选择框
			laydate.render({
				elem : '#foundTime'
			});
			
			form.render('radio');

			// 表单提交事件
			form.on('submit(btnSubmit)', function(data) {
				var ajax = new $ax(Feng.ctxPath + "/supplier/supplier_audit",
						function(data) {
							Feng.success("修改成功！");

							// 传给上个页面，刷新table用
							admin.putTempData('formOk', true);

							// 关掉对话框
							admin.closeThisDialog();
						}, function(data) {
							Feng.error("请求异常！" + data.responseJSON.message)
						});
				// ajax.set(data.field);
				ajax.set("userId", data.field.userId);
				ajax.set("examine", data.field.examine);
				ajax.start();
				return false;
			});
		});