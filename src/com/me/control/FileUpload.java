package com.me.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.me.ut.DateTimeUT;
import com.me.ut.FileUT;
import com.me.ut.ImageUtils;
import com.me.ut.StringUT;
import com.me.ut.WebPath;

@Controller
@RequestMapping("FileUpload")
public class FileUpload {
    
 // 上传大图,返回当前原始大图的下载地址
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "save_pic", method = RequestMethod.POST)
    public void save_pic(@RequestParam("Filedata") MultipartFile file,
                 @RequestParam("requestId") String requestId,
                 @RequestParam("fieldName") String fieldName,
                 HttpServletRequest request,
                 HttpServletResponse response)
    {

        Map map = new HashMap();

        StringUT.printErr("当前上传域：" + fieldName);
        OutputStream output = null;
        File outfile = null;
        String fileName = "";
        try
        {
            // MultipartFile是对当前上传的文件的封装a
            if (!file.isEmpty())
            {
                fileName = new String(file.getOriginalFilename()
                        .getBytes("ISO-8859-1"), "UTF-8");

                // 删除文件名中的单引号，因为有单引号的时候，文件名会被截断，js变量不能传递。
                fileName = StringUtils.replace(fileName,
                                               "'",
                                               "");

                System.out.println("上传的文件的文件名是：" + (fileName));
                String filepath = StringUT.getUploadFiles(requestId);
                filepath = filepath + fieldName + "/";
                if (!(new File(filepath).exists()))
                {
                    new File(filepath).mkdirs();
                }
                outfile = new File(filepath + fileName);
                output = new FileOutputStream(outfile);
                IOUtils.copy(file.getInputStream(),
                             output);
                // store the bytes somewhere
                // 在这里就可以对file进行处理了，可以根据自己的需求把它存到数据库或者服务器的某个文件夹

                boolean flag = processImg(filepath + fileName);
                if (!flag)
                {
                    map.put("result", "small");
                }
                else
                {
                    map.put("w", w2_);
                    map.put("h", h2_);
                    map.put("result", "success");
                }
            }
            else
            {
                StringUT.print(fieldName + "上传失败");
                //response.getWriter().write("error");
                map.put("result", "error");
            }
            String result=map.get("result").toString();
            String w=String.valueOf(map.get("w"));
            String h=String.valueOf(map.get("h"));
            response.getWriter().write(""+result+"&"+w+"&"+h+"");
        }
        catch (Exception e)
        {
            StringUT.printErr(e);
            // 此处一定要关闭
            if (output != null)
            {
                try
                {
                    output.close();
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
        finally
        {
            // 此处一定要关闭
            if (output != null)
            {
                try
                {
                    output.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
        
    }
    

    // 图床的大小为w:450 h:300;如果超出了图床的大小就缩小
    private static double w_ = 450.0;
    private static double h_ = 300.0;

    //实际的宽度和高度
    private static double w2_=0.0;
    private static double h2_=0.0;

    private boolean processImg(String filePath)
    {
        int h = ImageUtils.getH(filePath);
        int w = ImageUtils.getW(filePath);
        
        if (w < 120 || h < 150)
        {
            return false;
        }
        else
        {
            // 仅仅宽度太大
            if (w > w_ && h <= h_)
            {
                ImageUtils.scale(filePath,
                                 filePath,
                                 w_ / w);
            }

            // 仅仅高度太大
            if (w <= w_ && h > h_)
            {
                ImageUtils.scale(filePath,
                                 filePath,
                                 h_ / h);
            }

            // 宽度高度都太大
            if (w > w_ && h > h_)
            {
                double scale = 0.0;
                if (w_ / w < h_ / h)
                {
                    scale = w_ / w;
                }
                else
                {
                    scale = h_ / h;
                }
                ImageUtils.scale(filePath,
                                 filePath,
                                 scale);
            }
            
            //重新获取一次，用于设置页面上target和preview中的宽度和高度的属性(一定要设置，不然的话会可能发生自动缩放，很难看)
            h2_= ImageUtils.getH(filePath);
            w2_= ImageUtils.getW(filePath);
            return true;
        }

    }


    

    @RequestMapping("upload_photo")
    public ModelAndView upload_photo(
	    @RequestParam("requestId") String requestId,
	    @RequestParam("fieldName") String fieldName) {
	ModelAndView mod = new ModelAndView();
	mod.addObject("requestId", requestId);
	mod.addObject("fieldName", fieldName);
	mod.setViewName("employees/upload_photo");
	return mod;
    }

    // 生成缩略图，并返回下载地址
    @SuppressWarnings("unchecked")
    @RequestMapping("get_thumb")
    public @ResponseBody
    Map get_thumb(@RequestParam("requestId") String requestId,
	    @RequestParam("fieldName") String fieldName,
	    @RequestParam("file_name") String fileName,
	    @RequestParam("x") double x, @RequestParam("y") double y,
	    @RequestParam("w") double w, @RequestParam("h") double h,
	    @RequestParam("r") double r// 缩放的比例
    ) 
    {
	Map map = new HashMap();
	String filepath = WebPath.getUploadRootPath() + File.separatorChar
		+ requestId + File.separatorChar + fieldName
		+ File.separatorChar;
	fileName = StringUT.Base64_decode(fileName, "UTF-8");
	String srcImageFile = filepath + fileName;

	// 可能发生缩放了
	ImageUtils.scale(srcImageFile, srcImageFile, r);
	String rnd = String.valueOf(new Date().getTime());
	String result = filepath + "thumb" + rnd + ".jpg";

	ImageUtils.cut(srcImageFile, result, (int) x, (int) y, (int) w,// 固定值120
		(int) h);// 固定值150

	File f = new File(result);
	f.setLastModified(DateTimeUT.getNowNum() + 1000 * 60);
	
	String fileName_ = "thumb" + rnd + ".jpg";
	fileName_ = StringUT.Base64_encode(fileName_, "UTF-8");
	map.put("result", WebPath.SYS_PATH + "/FileUpload/downIMG.do?requestId="
		+ requestId + "&fieldName=" + fieldName + "&fileName="
		+ fileName_ + "");
	return map;
    }
    
    @RequestMapping("downIMG")
    public ModelAndView downIMG(@RequestParam("requestId") String requestId,
                                @RequestParam("fieldName") String fieldName,
                                @RequestParam("fileName") String fileName,
                                HttpServletRequest request,
                                HttpServletResponse response)
    {

        java.io.BufferedInputStream bis = null;
        java.io.BufferedOutputStream bos = null;

        String folePath = StringUT.getUploadFiles();

        String downLoadPath = folePath + "/" + requestId + "/" + fieldName
                + "/" + StringUT.Base64_decode(fileName,
                                               "UTF-8");
        System.out.println(downLoadPath);

        // 不存在返回异常
        if (!(new File(downLoadPath).exists()))
        {
            throw new RuntimeException("指定的文件不存在");
        }

        try
        {
            long fileLength = new File(downLoadPath).length();
            response.setContentType("application/x-msdownload;");

            // 下面三个if中的fileName必须直接来自输入参数，没做任何处理。
            fileName = StringUT.Base64_decode(fileName,
                                              "UTF-8");
            fileName = StringUT.UTF8_ISO(fileName);
            String head = "";
            if (StringUT.isIE(request))
            {
                fileName = URLEncoder.encode(StringUT.ISO_UTF8(fileName),
                                             "UTF-8").replace("+",
                                                              "%20");
                head = "attachment; filename=" + fileName;
            }
            else if (StringUT.isChrome(request))
            {
                head = "attachment; filename=" + fileName;
            }
            else if (StringUT.isFirefox(request))
            {
                head = "attachment;filename=\"" + fileName + "\"";
            }
            // response.setHeader("Content-disposition",
            // head);// 下载文件的时候
            response.setContentType("image/jpeg");
            response.setHeader("Content-Length",
                               String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff,
                                               0,
                                               buff.length)))
            {
                bos.write(buff,
                          0,
                          bytesRead);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bis != null)
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            if (bos != null)
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
        return null;
    }

    
    
    // 删除文件
    @SuppressWarnings("unchecked")
    @RequestMapping("/delFile")
    public @ResponseBody
    Map delFile(@RequestBody String queryIn)
    {
        Map map = StringUT.url2Map(queryIn);

        try
        {
            String requestId = String.valueOf(map.get("requestId"));
            String fieldName = String.valueOf(map.get("fieldName"));
            String fileName = String.valueOf(map.get("fileName"));

            FileUT.del(requestId,
                       fieldName,
                       fileName);
            StringUT.print("删除了文件：" + fileName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return map;
    }

}