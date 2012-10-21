<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#assign path="${request.getContextPath()}">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>上传页面</title>
		<link href="${path}/demo_files01/jquery.Jcrop.css" rel="stylesheet" type="text/css" />
		
		<script src="${path}/js/jquery-1.7.2.js" type="text/javascript"></script>
		<script src="${path}/js/common.js" type="text/javascript"></script>
		<script src="${path}/art_resource/jquery.artDialog.js?skin=blue"></script>
		<script src="${path}/art_resource/iframeTools.js"></script>
			
		<script src="${path}/swf_resource/swfupload.js"></script>
		<script src="${path}/swf_resource/swfupload.queue.js"></script>
		<script src="${path}/swf_resource/fileprogress.js"></script>
		<script src="${path}/swf_resource/handlers.js"></script>
		
		<script src="${path}/demo_files01/jquery.Jcrop.js" type="text/javascript"></script>
		<script>
	$(document).ready(function() {
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
                minSize: [120, 150], //设置最小选中区域
                //maxSize:[100,150],
                setSelect: [10, 10, 130, 160] //实际选中的区域

            }, function () {

                var bounds = this.getBounds();
                boundx = bounds[0]; //原始大图的宽度[我们所看到的]
                boundy = bounds[1]; //原始大图的高度[我们所看到的]
                jcrop_api = this;
                
            });
	}
	
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
            $("#x").text(rx * c.x);
            $("#y").text(ry * c.y);
            $("#w").text("120");
            $("#h").text("150");
            $("#r").text(rx);
        };
        
        	
	function closediv()
	{
			art.dialog.close();
	}
	
	function save_all()
	{
		//暂停一会再关闭
		window.setTimeout("closediv()",500);
	}
	
	function save_part()
	{
		//取得页面上的参数：如果参数太多，可以用 var args=getPostDatas($(document));[common.js]
		var x=$("#x").text();
		var y=$("#y").text();
		var w=$("#w").text();
		var h=$("#h").text();
		var r=$("#r").text();
		var file_name=$("#file_name").text();
		var request_id=$("#request_id").text();
		var args="x="+x+"&y="+y+"&w="+w+"&h="+h+"&r="+r+"&file_name="+file_name+"&request_id="+request_id+"";
		
		var result=getJson(args,"${path}/FileUpload/get_thumb.do");
		//alert(result['result']);
		
		//设置回父窗口，然后关闭当前页面
		var origin=artDialog.open.origin;
		var returnElement = origin.document.getElementById('returndata');
		returnElement.src=result['result'];
		art.dialog.tips("数据取得成功(三秒后关闭)");
		
		//暂停一会再关闭
		window.setTimeout("closediv()",3000);
	}
	
	//创建上传按钮
	var  swf;
	function  create_swf()
	{
		var settings = {
				flash_url : "${path}/swf_resource/swfupload.swf",
				upload_url: "${path}/FileUpload/save_pic.do",
				post_params : {
							"parent_page_id" : $("#parent_page_id").val(), 
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
				button_image_url: "${path}/swf_resource/TestImageNoText_65x29.png",
				button_width: "65",
				button_height: "29",
				button_placeholder_id: "spanButtonPlaceHolder",
				button_text: '<span class="theFont">上传</span>',
				button_text_style: ".theFont {width:50px; height:25px; font-size:12px; text-align:center;vertical-align: middle; font-family:'微软雅黑', ''黑体; color:#1c527a; }}",
				button_text_left_padding: 12,
				button_text_top_padding: 3,
				
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
	$("#process").text(file.name+"文件正在上传");
	//try {
		//var progress = new FileProgress(file, this.customSettings.progressTarget);
		//progress.setStatus("Uploading...");
		//progress.toggleCancel(true, this);
	//}
	//catch (ex) {}
	return true;
}

//上传完成 
function uploadComplete_(file) {
	//alert(file.name+"文件上传完成");
	$("#process").html(file.name+"文件上传完成    ");	
	//待上传队列是否为0,[是否已经全部上传完毕]
	if (this.getStats().files_queued === 0) 
	{	
		//alert("全部完成了");
		//document.getElementById(this.customSettings.cancelButtonId).disabled = true;
		//上传完成后，再次点击上传可以如果参数变化，可以通过下面的方法更改需要传递的新参数。
		//this.setPostParams({"requestid" : "000","fieldName":"111"});
	}
	var  downurl="${path}/FileUpload/download/"+$("#parent_page_id").val()+"/"+file.name+".do";
	$("#process").append("<a href="+downurl+">下载地址</a>");
	$("#target").attr("src",downurl);
	$("#preview").attr("src",downurl);
	
	$("#request_id").text($("#parent_page_id").val());
	$("#file_name").text(file.name);
	
	create_thumb();
}
	
	
</script>
 
	</head>
	<body>
		${path}
		<p>
			如果需要更改art的背景颜色-可以更改blue.css中的line39 .aui_inner
		</p>
		<div>
			from 服务器
			<br>
				parent_page_id<input id="parent_page_id" name="parent_page_id" value="${parent_page_id}">
			<br>
			
		</div>
		<br>
			<div>
			 <table>
			 		<tr>
			 			<td colspan="2">
			 			 	<span id="x"></span>
			 			 	<span id="y"></span>
			 			 	<span id="w"></span>
			 			 	<span id="h"></span>
			 			 	<span id="r"></span>
			 			 	<span id="file_name"></span>
			 			 	<span id="request_id"></span>
			 			</td>
			 		</tr>
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
			 				<img  id="target"  src="" alt="" />
			 			</td>
			 			<td>
			 				<div style="width: 120px; height: 150px; border:1px solid red; overflow:hidden; ">
                        		<img src="" id="preview" alt="Preview" class="jcrop-preview" />
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
