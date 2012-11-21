


/**
*
*  Base64 encode / decode
*	
*	var s="SDK license - 副本";
*	var b = new Base64();  
*	var str = b.encode(s);  
*   alert("base64 encode:" + str);  
*   str = b.decode(str);  
*   alert("base64 decode:" + str);  
*
*  @author haitao.tu
*  @date   2010-04-26
*  @email  tuhaitao@foxmail.com
*
*/
 
function Base64() {
 
	// private property
	_keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
 
	// public method for encoding
	this.encode = function (input) {
		var output = "";
		var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		var i = 0;
		input = _utf8_encode(input);
		while (i < input.length) {
			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
			chr3 = input.charCodeAt(i++);
			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;
			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			} else if (isNaN(chr3)) {
				enc4 = 64;
			}
			output = output +
			_keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
			_keyStr.charAt(enc3) + _keyStr.charAt(enc4);
		}
		return output;
	}
 
	// public method for decoding
	this.decode = function (input) {
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;
		input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
		while (i < input.length) {
			enc1 = _keyStr.indexOf(input.charAt(i++));
			enc2 = _keyStr.indexOf(input.charAt(i++));
			enc3 = _keyStr.indexOf(input.charAt(i++));
			enc4 = _keyStr.indexOf(input.charAt(i++));
			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;
			output = output + String.fromCharCode(chr1);
			if (enc3 != 64) {
				output = output + String.fromCharCode(chr2);
			}
			if (enc4 != 64) {
				output = output + String.fromCharCode(chr3);
			}
		}
		output = _utf8_decode(output);
		return output;
	}
 
	// private method for UTF-8 encoding
	_utf8_encode = function (string) {
		string = string.replace(/\r\n/g,"\n");
		var utftext = "";
		for (var n = 0; n < string.length; n++) {
			var c = string.charCodeAt(n);
			if (c < 128) {
				utftext += String.fromCharCode(c);
			} else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			} else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}
 
		}
		return utftext;
	}
 
	// private method for UTF-8 decoding
	_utf8_decode = function (utftext) {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;
		while ( i < utftext.length ) {
			c = utftext.charCodeAt(i);
			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			} else if((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i+1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			} else {
				c2 = utftext.charCodeAt(i+1);
				c3 = utftext.charCodeAt(i+2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
		}
		return string;
	}
}




function createSwf(fileId,fieldName,btnTxt,fileListObj,sysPath, btnbgWidth)
{
	if(fileId==undefined||fileId.length==0)
	{
		alert("文件ID为空,上传插件初始化失败");
		return;
	}
	
	if(!btnbgWidth){
		btnbgWidth=120;			
	}
	$("#outdiv_"+fieldName).html(
		"<form id=\"form_"+fieldName+"\"   method=\"post\"" +
		"							enctype=\"multipart/form-data\">" +
		"	<div style=\"float: left;\">" +
		"		<span id=\"span_"+fieldName+"\"></span>" +
		"		<input id=\"cancel_"+fieldName+"\" type=\"button\" value=\"Cancel All Uploads\"" +
		"			onclick=\"swfu_"+fieldName+".cancelQueue();\" disabled=\"disabled\"" +
		"			style=\"font-size: 8pt; height: 29px; display: none;\" />" +
		"	</div>" +
		"	" +
		"	<div style=\"float: left;\">" +
		"		<div id=\"status_"+fieldName+"\"" +
		"			style=\"float: left; text-align: left;\">" +
		"		</div>" +
		"	</div>" +
		"</form>"
	);
	
	var settings  = {
			            flash_url : sysPath+"/swf_resource/swfupload.swf",
			            upload_url : sysPath+"/FileUpload/save_pic.do;jsessionid=${jsessionid}",
			            post_params : {
							"requestid" : fileId, 
							"fieldName":fieldName  
						},
			            file_size_limit : "0",
			            file_types : "*.*",
			            file_types_description : "All Files",
			            file_upload_limit : 100,
			            file_queue_limit : 0,
			            custom_settings : {
			                progressTarget : "porgress_"+fieldName,
			                cancelButtonId : "cancel_"+fieldName,
			                divStatus:"status_"+fieldName
			            },
			            button_width:btnbgWidth,
			            button_text:"<span class='button_swf'>"+btnTxt+"</span>",
			            button_image_url: sysPath+"/swf_resource/swfbg"+btnbgWidth+".gif",
			            button_text_style: ".button_swf {width:"+btnbgWidth+"px; height:25px; font-size:12px; text-align:center;vertical-align: middle; font-family:'微软雅黑', ''黑体; color:#1c527a; }",
			            button_placeholder_id: "span_"+fieldName ,
			            
			            file_queue_error_handler : fileQueueError,
						file_dialog_complete_handler : fileDialogComplete,
						upload_start_handler : uploadStart,
						upload_progress_handler : uploadProgress,
						upload_error_handler : uploadError,
						upload_success_handler : function(file, data)
						{
//							var href="javascript:delFile('"+fileId+"','"+fieldName+"','"+replace_(file.name)+"','"+sysPath+"')";
//							if($(fileListObj).html().indexOf(href)<0)
//							{
// 								$(fileListObj).prepend(
//										"<div><a href=\""+sysPath+"/FileUpload/downIMG.do?requestid="+fileId+"&fieldName="+fieldName+"&fileName="+hz_encode(replace_(file.name))+"&d="+getTime()+"\">"+file.name+"</a>\n" +
//										"--\n" +
//										"<a  href=\"javascript:delFile('"+fileId+"','"+fieldName+"','"+replace_(file.name)+"','"+sysPath+"')\">删除</a>\n" +
//										"<br/></div>"
//								);
//							}
							
							try {
								var progress = new FileProgress(file, this.customSettings.progressTarget);
								progress.setComplete();
								progress.setStatus("上传完成");
								progress.toggleCancel(false);
								} catch (ex) {
								this.debug(ex);
							}
							
							
							
						},
						upload_complete_handler : uploadComplete,
						queue_complete_handler : queueComplete 	// Queue plugin event
		 };
		 return new SWFUpload(settings);
}

//encodeURIComponent 该方法不会对 ASCII 字母和数字进行编码，也不会对这些 ASCII 标点符号进行编码： - ! ~ * ' ( )  【.和_不用处理】
function hz_encode(fileName)
{	
	var base=new Base64();
	var out=base.encode(fileName);
	out=encodeURIComponent(out);//encodeURIComponent,tomcat自动帮助解开
	return out;
}

//删除单引号
function replace_(fileName)
{
	return replace(fileName,"'","");
}
function getTime()
{
	var d=new Date();
	return d.getTime();
}

//把srcStr中的targetStr替换成replaceStr
function replace(srcStr, targetStr, replaceStr) {
    var regS = new RegExp(targetStr, "gi");
    var s = srcStr.replace(regS, replaceStr); // 全部替换
    return s;
}
// 删除文件
function delFile(fileid, fieldName, fileName, sysPath) {
    $.ajax({
        type: "POST",
        async: false,
        url: sysPath + "/FileUpload/delFile.do",
        data: {
            requestId: fileid,
            fieldName: fieldName,
            fileName: fileName
        },
        success: function (data) {
            // data 有三个字段， fieldName,fileName,requestId

        	//encodeURIComponent
        	// requestid=fd570108de1d4ef6ba532bb8a2a84a2c&fieldName=bizLicense&fileName=%E5%A4%8F%E6%98%95%C2%B7%E6%B7%B1%E5%85%A5%E6%B5%85%E5%87%BAHibernate.pdf
        	var str = "requestid="+data.requestId + "&fieldName=" + data.fieldName + "&fileName=" +hz_encode(replace_(data.fileName));
            // var s=$("a[href$='"+str+"']").attr("href");
            $("div[id^='fileList']>div>a[href*=\"" + str + "\"]")
							.parent().remove();
        }
    });
}

