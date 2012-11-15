<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#assign path="${request.getContextPath()}">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>先用js设置图片后再初始化jcrop</title>
		<link href="${path}/demo_files01/jquery.Jcrop.css" rel="stylesheet" type="text/css" />

		  <script src="${path}/js/jquery-1.7.2.js" type="text/javascript"></script>
		  <script src="${path}/demo_files03/jquery.Jcrop.js" type="text/javascript"></script>
		  <script type="text/javascript">
        var jcrop_api, boundx, boundy;
        
        //ready
        $(document).ready(function () {
        
        	setImg();
        	showJcrop();
        });
        
        function setImg()
        {
        	$("#preview").attr("src","${path}/demo_files03/lh.jpg");
        	$("#target").attr("src","${path}/demo_files03/lh.jpg");
        }
        
        function  showJcrop()
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
        
        //ready over
       	
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
        };
    </script>
	</head>
	<body>
		<div style="border:1px solid red; margin: 20px; padding:20px; ">
		  <table>
            <tr>
 	          <td>
                    <img src=""  onload="this.style.display='block'"  id="target" alt="Flowers" />
                </td>
                <td>
                    <div style="width: 120px; height: 150px; border:1px solid red; overflow:hidden; ">
                        <img src=""  onload="this.style.display='block'"  id="preview" alt="Preview" class="jcrop-preview" />
                    </div>
                </td>
                
                
            </tr>
        </table>
		</div>
	</body>
</html>
