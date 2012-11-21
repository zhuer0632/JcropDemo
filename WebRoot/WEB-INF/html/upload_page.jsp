<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#assign path="${request.getContextPath()}">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>上传页面</title>
		<link href="${path}/demo_files02/jquery.Jcrop.css" rel="stylesheet" type="text/css" />
		<link href="${path}/swf_resource/upload.css" rel="stylesheet" type="text/css" />
		
		<script src="${path}/js/jquery-1.7.2.js" ></script>
		<script src="${path}/js/common.js" ></script>
		<script src="${path}/art_resource/jquery.artDialog.js?skin=blue"></script>
		<script src="${path}/art_resource/iframeTools.js"></script>
		
		<script src="${path}/swf_resource/swfupload.js"></script>
		<script src="${path}/swf_resource/swfupload.queue.js"></script>
		<script src="${path}/swf_resource/fileprogress.js"></script>
		<script src="${path}/swf_resource/handlers.js"></script>
		<script src="${path}/swf_resource/upload.js"></script>
		<script src="${path}/demo_files02/jquery.Jcrop.js" ></script>
		
	<script>
	
	$(document).ready(function() 
	{
		//alert("我是弹出的子页面");
		create_swf();
		$("#btn_save").hide();
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
					 alert("图片最小不能小于120*150(w*h)");
					 closediv();
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
			
		//设置回父窗口，然后关闭当前页面
		var origin=artDialog.open.origin;
		var returnElement = origin.document.getElementById('returndata');
		
		$(returnElement).attr("src",result['result']);
		$(returnElement).attr("style","");
		
		art.dialog.tips("头像设置成功");
		
		//暂停一会再关闭
		window.setTimeout("closediv()",500);
	}
	
	//创建上传按钮
	function  create_swf()
	{
		var fileId="${fileId}";
		//var fieldName="${fieldName}";//此处不用传递
	 	createSwf(fileId, "photo", "上传照片", $("#fileList_photo"), "${path}", 100);
	}
	

function uploadSuccess_(file,data)
{
	 //alert(file.name+"文件上传完成");
	$("#process").html(file.name+"  文件上传完成");	
	
	var requestid=$("#requestid").val();
	var fieldName=$("#fieldName").val();
	var fileName=hz_encode(replace_(file.name));
	var downurl="${path}/FileUpload/downIMG.do?requestid="+requestid+"&fieldName="+fieldName+"&fileName="+fileName+"";
					 
	$("#target").attr("src",downurl);
	$("#preview").attr("src",downurl);
	$("#file_name").val(fileName);
	
	create_thumb();
	$("form").hide();
	$("#btn_save").show();
	
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
						<td colspan="2" height="100">
							<!--   字段名是: photo  -->
					        <div id="outdiv_photo" class="divUpload">
							</div>
							<div id="fileList_photo" class="divFileList">
							</div>
							<div id="porgress_photo">
							</div>
						</td>
			 		</tr>
			 		<tr>
			 			<td width="450px" height="300px">
			 				<img  id="target"   src="${path}/demo_files02/target.gif"   />
			 			</td>
			 			<td  width="150px" align="center"> 
			 				<div style="width: 120px; height: 150px; border:1px solid red; overflow:hidden; ">
                        		<img src="${path}/demo_files02/preview.gif"   id="preview"  class="jcrop-preview" />
                    		</div>
			 			</td>
			 		</tr>
					<tr>
						<td colspan="2">
							 <input type="button" id="btn_save" value="保存选中区域"  onclick="save_part();" />
						</td>
					</tr>
			 </table>
			</div>
	</body>
</html>
