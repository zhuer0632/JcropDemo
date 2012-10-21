package com.me.ut;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * 路径辅助类
 * 
 * @author ZHU
 */
public class WebPath
{

    public static String SYS_PATH;


    // 取得tomcat所在的盘符 eg: "c:\\"
    public static String getTomcatPath()
    {
        return new File("/").getAbsolutePath();
    }



    /**
     * 取得项目的classes文件夹 eg："C:/tomcat6/webapps/v/WEB-INF/classes/"
     * 
     * @return
     */
    public static String getClassRootPath()
    {
        URL url = WebPath.class.getResource("WebPath.class");
        String path = url.getPath();
        path = path.substring(0,
                              path.lastIndexOf("classes")) + "classes"
                + File.separatorChar;
        return path;
    }
    
    /**
     * 返回的路径如："C:/tomcat6/webapps/v/WEB-INF/classes/uploadFiles/"
     * 
     * @return
     */
    public static String getUploadRootPath()
    {
        String path = getClassRootPath();
        path = path + "uploadFiles/";
        if (!(new File(path).exists()))
        {
            new File(path).mkdirs();
        }
        return path;
    }


    public static String getClassPath(Class clz)
    {
        return getClassRootPath()
                + clz.getPackage().getName().replaceAll("\\.",
                                                        "/");
    }


    public static String getPreUrl(HttpServletRequest request)
    {
        return request.getHeader("Referer");
    }


    public static String getCurPage(HttpServletRequest request)
    {
        String url = request.getRequestURL().toString();
        String path = request.getQueryString();
        return url + "?" + path;
    }


    public static String getJavaCPPFilePath()
    {
        return null;
    }

}
