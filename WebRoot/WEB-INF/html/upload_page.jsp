<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#assign path="${request.getContextPath()}">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>上传页面</title>
		<link href="${path}/demo_files02/jquery.Jcrop.css" rel="stylesheet" type="text/css" />
		
		<script src="${path}/js/jquery-1.7.2.js" ></script>
		<script src="${path}/js/common.js" ></script>
		<script src="${path}/art_resource/jquery.artDialog.js?skin=blue"></script>
		<script src="${path}/art_resource/iframeTools.js"></script>
		
		<script src="${path}/swf_resource/swfupload.js"></script>
		<script src="${path}/swf_resource/swfupload.queue.js"></script>
		<script src="${path}/swf_resource/fileprogress.js"></script>
		<script src="${path}/swf_resource/handlers.js"></script>
		<script src="${path}/demo_files02/jquery.Jcrop.js" ></script>
		
	<script>
	$(document).ready(function() 
	{
		//alert("我是弹出的子页面");
		create_swf();
	});
	
	var jcrop_api, boundx, boundy;
	function create_thumb()
	{
		$('#target').Jcrop({
              	onChange: updatePreview,
                onSelect: updatePreview,
                aspectRatio: 4 / 5, // 更改选中区域大小的时候，指定的比例。不要跟下面的选中区域大小相矛盾。
                minSize: [120, 150]  //设置最小选中区域
                //maxSize:[100,150],
            }, 
            function () 
            {
                var bounds = this.getBounds();
                boundx = bounds[0]; //原始大图的宽度[我们所看到的]
                boundy = bounds[1]; //原始大图的高度[我们所看到的]
                //由于第一次的图片会遗留变量boundx,boundy.下面重新刷新一下用于清除该变量。
                jcrop_api = this;
				jcrop_api.setSelect([10, 10, 130, 160]);
				if(boundx<120||boundy<150)
				{    
				 	 clear_();
				 	 jcrop_api.destroy();
					
					 //onload="this.style.display='block'" style="display:none"
					  
					 $("#target").attr("onload","this.style.display='block'");
					 $("#target").attr("style","display:none");
					 $("#preview").attr("onload","this.style.display='block'");
					 $("#preview").attr("style","display:none");
					 
					 alert("图片最小不能小于120*150(w*h)");
					 
					 //return false;
				}
            });
            
        //变量说明
        // boundx,boundy  原图宽高比
        // c.w c.h    选中区域的高宽
        // c.x c.y    选中区域的左上角坐标
        function updatePreview(c) {
            if (parseInt(c.w) > 0) {
                var rx = 120 / c.w; //缩略图宽度和实际选中的区域的宽度的比例
                var ry = 150 / c.h; //缩略图高度和实际选中的区域的高度的比例
                $('#preview').css({
                    width: Math.round(rx * boundx) + 'px', //对于原始图来说被缩放后的宽度
                    height: Math.round(ry * boundy) + 'px', //对于原始图来说被缩放后的高度
                    marginLeft: '-' + Math.round(rx * c.x) + 'px', //移动杯缩放后的图，加上 overflow:hidden，使得只看到部分区域[div中的区域]
                    marginTop: '-' + Math.round(ry * c.y) + 'px'
                });
            }
            $("#x").val(rx * c.x);
            $("#y").val(ry * c.y);
            $("#w").val("120");
            $("#h").val("150");
            $("#r").val(rx);
        };
	}
	
 	function clear_()
	{
		$("#target").attr("src","");
		$("#preview").attr("src","");
		 $("#target").removeAttr("style");
		 $("#preview").removeAttr("style");
		 $("#process").text("");
	}
        	
	function closediv()
	{
			art.dialog.close();
	}
	
		
	function save_part()
	{
		//取得页面上的参数：如果参数太多，可以用 var args=getPostDatas($(document));[common.js]
		var x=$("#x").val();
		var y=$("#y").val();
		var w=$("#w").val();
		var h=$("#h").val();
		var r=$("#r").val();
		var file_name=$("#file_name").val();
		var requestid=$("#requestid").val();
		var fieldName=$("#fieldName").val();
		var args="x="+x+"&y="+y+"&w="+w+"&h="+h+"&r="+r+"&file_name="+file_name+"&requestid="+requestid+"&fieldName="+fieldName+"";
		
		var result=getJson(args,"${path}/FileUpload/get_thumb.do");
		//alert(result['result']);
		
		//设置回父窗口，然后关闭当前页面
		var origin=artDialog.open.origin;
		var returnElement = origin.document.getElementById('returndata');
		$(returnElement).attr("src",result['result']);
		$(returnElement).attr("style","");
		
		//alert(result['result']);
		//art.dialog.tips("头像设置成功");
		alert("头像设置成功");
		//暂停一会再关闭
		window.setTimeout("closediv()",10);
	}
	
	//创建上传按钮
	var  swf;
	function  create_swf()
	{
		var settings = {
				flash_url : "${path}/swf_resource/swfupload.swf",
				upload_url: "${path}/FileUpload/save_pic.do",
				post_params : {
							"requestid" : $("#requestid").val(), 
							"fieldName":"photo"  
				},
				file_types : "*.jpg",
				file_types_description : "JPG图片文件",
 				custom_settings : {
				//	progressTarget : "fsUploadProgress"
				//	cancelButtonId : "btnCancel",
				//	divStatus:"divStatus"
				},
		 		
				// Button settings
			    button_width:"100",
	            button_text:"<span class='button_swf'>上传</span>",
	            button_image_url: "${path}/swf_resource/swfbg100.png",
	            button_text_style: ".button_swf {width:100px; height:25px; font-size:12px; text-align:center;vertical-align: middle; font-family:'微软雅黑', ''黑体; color:#1c527a; }",
	            button_placeholder_id: "spanButtonPlaceHolder" ,
				
				//触发事件
				file_queued_handler : fileQueued,
				file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : fileDialogComplete,
				upload_start_handler : uploadStart_, //单个文件上传开始
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete_,//单个文件上传over
				queue_complete_handler : queueComplete	// 队列上传over
				
			};
			swfu = new SWFUpload(settings);
	}
	
	//下面是文件上传的各种监听事件
	//开始上传[这里只是开始上传的一个提示，真正处理在其他地方]
function uploadStart_(file) {
	//alert(file.name+"文件开始上传");
	$("#process").text(file.name+"  文件正在上传");
	return true;
}

//上传完成 
function uploadComplete_(file) {
	
	//alert(file.name+"文件上传完成");
	$("#process").html(file.name+"  文件上传完成");	
	//待上传队列是否为0,[是否已经全部上传完毕]
	if (this.getStats().files_queued === 0) 
	{	
		var  downurl="${path}/FileUpload/download/"+$("#requestid").val()+"/"+$("#fieldName").val()+"/"+file.name+".do";
		//$("#process").append("<a href="+downurl+">下载地址</a>");
		
		if(jcrop_api)
		{
			jcrop_api.destroy();
		}
		
		//$("#target").attr("src","");
		//$("#preview").attr("src","");
		
		//$("#target").removeAttr("style");
		//$("#preview").removeAttr("style");
		$("#target").attr("src",downurl);
		$("#preview").attr("src",downurl);
		$("#file_name").val(file.name);
		
		create_thumb();
	}
}
	
</script>
 
	</head>
	<body>
			<!-- 参数区域 -->
			<div style="display: none;">
						<input id="x" name="x" value="">
		 				<input id="y" name="y" value="">
		 				<input id="w" name="w" value="">
		 				<input id="h" name="h" value="">
		 				<input id="r" name="r" value="">
		 				<input id="file_name" name="file_name" value="">
		 				<input id="requestid" name="requestid" value="${requestid}">
	 					<input id="fieldName" name="fieldName" value="${fieldName}">
			</div>
			
			<div>
			 <table>
			 		<tr>
			 			<td colspan="2">		
				 			<form enctype="multipart/form-data" method="post" >
								<div>
									<span id="spanButtonPlaceHolder"></span>
								</div>
							</form>
							<div id="process"></div>
						</td>
			 		</tr>
			 		<tr>
			 			<td width="450px" height="300px">
			 				<img  id="target"  onload="this.style.display='block'"  style="display:none" src="" alt="" />
			 			</td>
			 			<td  width="150px" align="center"> 
			 				<div style="width: 120px; height: 150px; border:1px solid red; overflow:hidden; ">
                        		<img src="" onload="this.style.display='block'" style="display:none" id="preview" alt="Preview" class="jcrop-preview" />
                    		</div>
			 			</td>
			 		</tr>
					<tr>
						<td colspan="2">
							 <input type="button" value="保存选中区域"  onclick="save_part();" />
						</td>
					</tr>
			 </table>
			</div>
	</body>
</html>
