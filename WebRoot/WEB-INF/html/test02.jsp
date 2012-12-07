<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#assign path="${request.getContextPath()}">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>上传+裁剪+含有服务器端处理</title>
				
		<script src="${path}/js/jquery-1.7.2.js" type="text/javascript"></script>
		<script src="${path}/art_resource/jquery.artDialog.js?skin=blue"></script>
		<script src="${path}/art_resource/iframeTools.js"></script>
		
		<script>
			$(document).ready(function(){
					
			});
			
			//打开上传照片的div
			//弹出子页面
			function opendiv()
			{	 	
			var args="requestId=${requestId}&fieldName=${fieldName}";
		 	art.dialog.open(
				"${path}/art/upload_page.do?"+args+"",
				{
					lock : true,
					background : 'gray', //背景颜色
					opacity : 0.4,	//淡化背景颜色
					width : '650px',
					height : '450px',
					drag : true,
					title : "上传照片",
					resize : true,
					fixed : true
				}
			);
			}
		</script>
</head>
	<body>
		<div style="text-align: center;">
			
			<div style="height: 150px;width: 120px; border: 1px solid red;margin: 20px auto;">
				 <img id="returndata" width="120px" height="150px;" style="display:none" src="" alt="" />	
			</div>
			
		<div>
			<a href="javascript:opendiv();">上传照片</a>
		</div>
		</div>
	</body>
</html>
