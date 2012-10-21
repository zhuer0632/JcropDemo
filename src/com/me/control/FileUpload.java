package com.me.control;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.me.ut.ImageUtils;
import com.me.ut.StringUT;
import com.me.ut.WebPath;
import com.sun.imageio.plugins.common.ImageUtil;


@Controller
@RequestMapping("FileUpload")
public class FileUpload
{

    // 上传大图,返回当前原始大图的下载地址
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/save_pic", method = RequestMethod.POST )
    public @ResponseBody
    Map save_pic(@RequestParam("Filedata") MultipartFile file,
                 @RequestParam("parent_page_id") String parent_page_id,
                 @RequestParam("fieldName") String fieldName)
    {
        Map out = new HashMap();
        System.out.println("--\r\n");
        System.out.println("parent_page_id=" + parent_page_id);// 用于表示跟那个表单相关联
        System.out.println("--\r\n");

        String filename = file.getOriginalFilename();
        OutputStream output = null;
        File outfile = null;
        try
        {
            filename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
            String folderpath = WebPath.getUploadRootPath()
                    + File.separatorChar + parent_page_id + File.separatorChar;
            if (!(new File(folderpath).exists()))
            {
                new File(folderpath).mkdirs();
            }

            String filePath = folderpath + filename;

            outfile = new File(filePath);
            output = new FileOutputStream(outfile);
            IOUtils.copy(file.getInputStream(),
                         output);
            System.out.println(filename);
            processImg(filePath);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
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
        return out;
    }


    // 图床的大小为w:450 h:300;如果超出了图床的大小就缩小
    private static double w_ = 600.0;
    private static double h_ = 300.0;
    private void processImg(String filePath)
    {
        int h = ImageUtils.getH(filePath);
        int w = ImageUtils.getW(filePath);
        
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
    }


    // 生成缩略图，并返回下载地址
    @SuppressWarnings("unchecked")
    @RequestMapping("get_thumb")
    public @ResponseBody
    Map get_thumb(@RequestParam("request_id") String requestid,
                  @RequestParam("file_name") String fileName,
                  @RequestParam("x") double x,
                  @RequestParam("y") double y,
                  @RequestParam("w") double w,
                  @RequestParam("h") double h,
                  @RequestParam("r") double r// 缩放的比例
    )
    {
        Map map = new HashMap();
        String filepath = WebPath.getUploadRootPath() + File.separatorChar
                + requestid + File.separatorChar;
        String srcImageFile = filepath + fileName;

        // 可能发生缩放了
        ImageUtils.scale(srcImageFile,
                         srcImageFile,
                         r);
        String rnd = String.valueOf(new Date().getTime());
        String result = filepath + "thumb" + rnd + ".jpg";
        
        ImageUtils.cut(srcImageFile,
                       result,
                       (int) x,
                       (int) y,
                       (int) w,// 固定值120
                       (int) h);// 固定值150
        map.put("result",
                WebPath.SYS_PATH + "/FileUpload/download/" + requestid
                        + "/thumb" + rnd + ".jpg.do?time=" + rnd);
        return map;
    }


    // public ModelAndView download
    @RequestMapping(value = "/download/{requestid}/{fileName}", method = RequestMethod.GET)
    public ModelAndView download(@PathVariable("requestid") String requestid,
                                 @PathVariable("fileName") String fileName,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
                                                              throws Exception
    {

        // [对于get方法下面两行方法无效，只能通过手工转码]
        String referURL = request.getHeader("Referer");

        request.setCharacterEncoding("UTF-8");
        request.setAttribute("content-type",
                             "text/html;charset=UTF-8");
        
        java.io.BufferedInputStream bis = null;
        java.io.BufferedOutputStream bos = null;
        
        String folePath = StringUT.getUploadFiles();
        
        // 现在全用base64进行编码传递，因为前端js对url编码支持不好
        // fileName = Base64UT.decode(fileName);
        // fileName = URLDecoder.decode(fileName, "UTF-8");
        
        fileName=new String(fileName.getBytes("ISO-8859-1"),"UTF-8");
        
        String downLoadPath = folePath + "/" + requestid + "/" + fileName;
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
            response.setHeader("Content-disposition",
                               "attachment; filename="
                                       + new String(
                                                    fileName.getBytes(),
                                                        "ISO8859-1"));// 下载文件的时候
            // 空格被替换成了_
            // ,是因为ie的问题
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
                bis.close();
            if (bos != null)
                bos.close();
        }
        // 返回空拦截器会报错
        return null;
    }

}
