package com.me.ut;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.nutz.lang.Lang;
import org.springframework.web.servlet.ModelAndView;


public class StringUT
{

    private static Logger logger = Logger.getLogger(StringUT.class);


    /**
     * D:/tomcat6/webapps/v/WEB-INF/classes/uploadFiles/13e53d
     * b657ab419c876413024f54f389/
     * 
     * @param requestId
     * @return
     */
    public static String getUploadFiles(String requestId)
    {
        return WebPath.getClassRootPath() + "uploadFiles/" + requestId + "/";
    }


    /**
     * 返回文件路径--包含字段名称--以/结尾
     * 
     * @param files
     * @param fieldName
     * @return
     */
    public static String getUploadFilesPath(String files,
                                            String fieldName)
    {
        String path = getUploadFiles(files) + fieldName + "/";
        return path;
    }


    /**
     * 
     * @param queryin
     *            =assessYear=2011&requestId1=db5348b94ff84e2bad408bf809850f2c&
     *            authorizeNum
     *            =101&validEndDate=2012-01-02&ID=basicinfo_id&validBeginDate
     *            =%E4%B8%AD%E6%96%87%E6%B5%8B%E8%AF%95
     * @return
     */
    public static Map<String, String> url2Map(String queryin)
    {
        Map<String, String> map = new HashMap<String, String>();
        if (queryin == null || queryin.trim().length() == 0)
        {
            return map;
        }

        // 先拆开 ，再decode
        String[] arr = queryin.split("&");
        for (int i = 0; i < arr.length; i++)
        {
            if (StringUT.isEmpty(arr[i]))
            {
                continue;
            }

            String[] item = arr[i].toString().split("=");
            if (item.length != 2)//等于1的情况
            {
                map.put(item[0],"");
            }
            else
            {
                try
                {
                    map.put(item[0],
                            URLDecoder.decode(item[1],
                                              "UTF-8"));
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
            
        }
        return map;
    }

    
    public static Map<String, String> url2MapDecodeAll(String queryin)
    {
        Map<String, String> map = new HashMap<String, String>();
        if (queryin == null || queryin.trim().length() == 0)
        {
            return map;
        }

        // 先拆开 ，再decode
        String[] arr = queryin.split("&");
        for (int i = 0; i < arr.length; i++)
        {
            if (StringUT.isEmpty(arr[i]))
            {
                continue;
            }

            String[] item = arr[i].toString().split("=");
            if (item.length != 2)//等于1的情况
            {
                map.put(item[0],"");
            }
            else
            {
                try
                {
                    map.put( URLDecoder.decode(item[0], "UTF-8"),
                            URLDecoder.decode(item[1],
                                              "UTF-8"));
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
            
        }
        return map;
    }
    
    

    /**
     * 返回路劲是：C:/tomcat6/webapps/v/WEB-INF/classes/uploadFiles/
     * 
     * @return
     */
    public static String getUploadFiles()
    {
        return WebPath.getClassRootPath() + "uploadFiles"+File.separatorChar;
    }


    public static void print(Object err)
    {
        String str = "";
        if (err != null)
        {
            str = err.toString();
        }
        System.err.println("\r\n---------------\r\n" + str + "\r\n---------------");
    }


    public static void printErr(Throwable e)
    {
        String str = Lang.getStackTrace(e);
        if (str != null)
        {
            System.err.println("\r\n---------------\r\n" + str + "\r\n---------------");
        }
    }


    public static void printErr(List list)
    {
        if (list != null && list.size() > 0)
        {
            System.err.println("\r\n---------------");
            for (int i = 0; i < list.size(); i++)
            {
                System.err.print("[" + list.get(i) + "]  ");
            }
            System.err.println("\r\n---------------");
        }
        else
        {
            System.err.println("\r\n---------------\r\n" + null + "\r\n---------------");

        }
    }


    public static void printErr(String str)
    {
        if (str != null)
        {
            System.err.println("\r\n---------------\r\n" + str + "\r\n---------------");
        }
    }


    /**
     * 取得32 位UUID(不含有-)
     * 
     * @return
     */
    public static String getUUID()
    {
        return UUID.randomUUID().toString().replace("-",
                                                    "");
    }


    private static String errDesc = "";
    static
    {
        errDesc = "";// 每次进入该类的时候都清空该变量
    }


    /**
     * 取得异常描述信息
     */
    public static String getExceptionDesc(Throwable e)
    {
        return Lang.getStackTrace(e);
    }


    /**
     * 
     * 接受的参数类型可以是 8种基本数据类型,List,Map
     * 
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj)
    {
        if (obj == null)
            return true;

        if (obj instanceof StringBuilder)
        {
            obj = ((StringBuilder) obj).toString();
        }

        if (obj instanceof String)
        {
            return ((String) obj).trim().length() == 0;
        }

        if (obj.getClass().isArray())
            return Array.getLength(obj) == 0;
        if (obj instanceof Collection<?>)
            return ((Collection<?>) obj).isEmpty();
        if (obj instanceof Map<?, ?>)
            return ((Map<?, ?>) obj).isEmpty();
        return false;
    }


    /**
     * List<String>-->String 中间以,分割 每个字符用单引号包含 <br>
     * sql where not in 用 <br>
     * 返回结果如：'xx','bb','cc'
     * 
     * @return
     */
    public static String convertList2Str(List<String> list)
    {
        if (list == null || list != null && list.size() == 0)
        {
            return "";
        }
        String out = "";

        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i) == null)
            {
                continue;
            }
            if (i == (list.size() - 1))
            {
                out += "'" + list.get(i) + "'";
            }
            else
            {
                out += "'" + list.get(i) + "',";
            }
        }
        return out;
    }


    public static String getV(Map map,
                              String key)
    {
        if (map == null || key == null)
        {
            return null;
        }
        if (StringUT.isEmpty(map.get(key)))
        {
            return null;
        }
        else
        {
            return map.get(key).toString();
        }
    }


    /**
     * 
     * 使用str填充stringBuilder
     * 
     * @param stringBuilder
     * @param str
     */
    public static void fillStringBuilder(StringBuilder stringBuilder,
                                         String str)
    {
        if (stringBuilder == null)
        {
            return;
        }
        stringBuilder = stringBuilder.delete(0,
                                             stringBuilder.toString().length());
        stringBuilder = stringBuilder.append(str);

        logger.debug("");

    }


    public static void main(String[] args)
    {
        StringBuilder stringBuilder = new StringBuilder();
        fillStringBuilder(stringBuilder,
                          "adc");
        System.out.println("1");
        System.out.println(stringBuilder);
        System.out.println("2");
    }




    /**
     * 判断非空的字符串是否是数字
     * 
     * @param text
     * @return
     */
    public static boolean isInt(String text)
    {
        try
        {
            int i = Integer.valueOf(text.trim().toString());
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    

    public static String getTransValueFromGBK( String strSrc )
    {
        if( strSrc == null || strSrc.trim().length() == 0 )
        {
            return null;
        }
        
        try
        {
            String strResult = new String( strSrc.getBytes( "GBK" ), "UTF-8" );
            
            return strResult;
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        
        return null;
        
    }
    
    public static String getTransValueFrom8859( String strSrc )
    {
        if( strSrc == null || strSrc.trim().length() == 0 )
        {
            return null;
        }
        
        try
        {
            return new String( strSrc.getBytes( "ISO-8859-1" ), "UTF-8" );
            
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        
        return null;
        
    }
    
    public static float parseToFloat( String strFloat )
    {
        if( null == strFloat || strFloat.trim().length() == 0 )
            return 0;
        try
        {
            return Float.parseFloat( strFloat );
        }
        catch( Exception e )
        {
            
        }
        
        return 0;
    }


    public static Boolean getBoolean(Object obj)
    {
        if (isEmpty(obj))
        {
            throw new RuntimeException("参数不能为空");
        }
        if (String.valueOf(obj).equals("1"))
        {
            return true;
        }
        else if (String.valueOf(obj).equals("true"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public static String Base64_decode(String input, String code) {
        String out="";
        byte[] bs = Base64.decodeBase64(input);
        try
        {
            out= new String(bs,code);
        }
        catch (UnsupportedEncodingException e)
        {   
            e.printStackTrace();
        }
        return out;
    }


    public static String UTF8_ISO(String str)
    {

        if (isEmpty(str))
        {
            return "";
        }

        String out = "";
        try
        {
            out = new String(str.getBytes("UTF-8"), "ISO-8859-1");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return out;
    }

    public static boolean isIE(HttpServletRequest request)
    {

        boolean out = false;

        // chrome:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML,
        // like Gecko) Chrome/22.0.1229.94 Safari/537.4
        // IE:Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64;
        // Trident/4.0; GTB7.4; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729;
        // .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E;
        // InfoPath.3)
        String ua = request.getHeader("User-Agent");
        if (ua.toLowerCase().contains("msie"))
        {
            out = true;
        }
        return out;
    }


    public static boolean isChrome(HttpServletRequest request)
    {

        boolean out = false;

        // chrome:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML,
        // like Gecko) Chrome/22.0.1229.94 Safari/537.4
        // IE:Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64;
        // Trident/4.0; GTB7.4; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729;
        // .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E;
        // InfoPath.3)
        // FF:Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101
        // Firefox/16.0
        String ua = request.getHeader("User-Agent");
        if (ua.toLowerCase().contains("chrome"))
        {
            out = true;
        }
        return out;
    }


    public static boolean isFirefox(HttpServletRequest request)
    {
        boolean out = false;
        // chrome:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML,
        // like Gecko) Chrome/22.0.1229.94 Safari/537.4
        // IE:Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64;
        // Trident/4.0; GTB7.4; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729;
        // .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E;
        // InfoPath.3)
        // FF:Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101
        // Firefox/16.0
        String ua = request.getHeader("User-Agent");
        if (ua.toLowerCase().contains("firefox"))
        {
            out = true;
        }
        return out;
    }
    
    public static String Base64_encode(String input,String code)
    {
        String s="";
        try
        {
            s = Base64.encodeBase64String(input.getBytes(code));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return s;
    }


    /**
     * 
     * @param bizName
     * @return
     */
    public static String ISO_UTF8(String str)
    {
        if (isEmpty(str))
        {
            return "";
        }

        String out = "";
        try
        {
            out = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return out;
    }

    
    
}
