package com.me.ut;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
	


public class FileUT
{

    public static boolean exist(String filePath)
    {
        return new File(filePath).exists();
    }


   
    /**
     * 
     * 指定objId和字段名称 <br>
     * 判断是否该字段对应的上传文件夹下含有文件
     * 
     * @return
     */
    public static boolean hasUpload(String fileId,
                                    String fieldName)
    {
        String path = WebPath.getUploadRootPath() + fileId + File.separatorChar
                + fieldName + File.separatorChar;

        if (isFolderAndHasFile(path))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * 指定的objid下是否含有文件夹以及文件夹下含有文件 true表示含有文件夹和文件
     */
    public static boolean isFolderAndHasFile(String fileFold)
    {
        if (isFolder(fileFold))
        {
            if ((new File(fileFold)).list().length > 0)// 文件夹下面含有文件
            {
                return true; // 指定的文件夹下含有文件
            }
            else
            {
                return false;// 指定的文件夹下没有文件
            }
        }
        return false;
    }


    public static boolean isFolder(String fileFold)
    {
        if (exist(fileFold) && (new File(fileFold)).isDirectory())
        {
            return true;
        }
        return false;
    }


    @SuppressWarnings("unchecked")
    public static String getFirstFileName(String fileFold)
    {
        // 应该是取得修改时间最晚的一个文件
        String[] fileNames = (new File(fileFold)).list();

        Hashtable hash = new Hashtable();
        for (int i = 0; i < fileNames.length; i++)
        {
            String fileName = fileNames[i];
            String f = fileFold + File.separatorChar + fileName;
            if (new File(f).exists())
            {
                File file = new File(f);
                hash.put(fileName,
                         file.lastModified());
            }
        }
        Enumeration<String> keys = hash.keys();

        while (keys.hasMoreElements())
        {
            String key = (String) keys.nextElement();
            long lastmodifyDate = Long.valueOf(hash.get(key).toString());

            if (keys.hasMoreElements())
            {
                String key2 = (String) keys.nextElement();
                long lastmodifyDate2 = Long.valueOf(hash.get(key2).toString());
            }

        }

        String file = ((new File(fileFold)).list())[0];
        return file;
    }


    /**
     * 
     * filepath通过如下方法取到 <br>
     * <br>
     * String filepath = StringUT.getUploadFiles(requestId);// <br>
     * if (!FileUT.isFolderAndHasFile(filepath)) <br>
     * { //return; <br>
     * 
     * @param filepath
     * @param fieldName
     * @return
     */
    public static String[] getAllFilesIn(String filepath,
                                         String fieldName)
    {
        String[] files = (new File(filepath + File.separatorChar + fieldName))
                .list();
        return files;
    }


    /**
     * 取得指定requestid+列名对应文件夹下的所有文件
     * 
     * @param requestId
     * @param fieldName
     * @return
     */
    public static String[] getUploadFilesName(String requestId,
                                              String fieldName)
    {
        String filepath = StringUT.getUploadFiles(requestId);//
        if (!FileUT.isFolderAndHasFile(filepath))
        {
            return new String[0];
        }

        String[] arr = getAllFilesIn(filepath,
                                     fieldName);
        if (arr == null || arr.length == 0)
        {
            return new String[0];
        }
        
        return arr;
    }


    /**
     * 简单取得文件名字
     * 
     * @param requestId
     * @param fieldName
     * @return
     */
    public static String[] getUploadFilesNameNoEncode(String requestId,
                                                      String fieldName)
    {
        String filepath = StringUT.getUploadFiles(requestId);//
        if (!FileUT.isFolderAndHasFile(filepath))
        {
            return new String[0];
        }

        String[] arr = getAllFilesIn(filepath,
                                     fieldName);
        if (arr == null || arr.length == 0)
        {
            return new String[0];
        }
        return arr;
    }


     
    public static void del(String requestId,
                           String fieldName,
                           String fileName)
    {
        try
        {
            String filePath = WebPath.getUploadRootPath() + requestId
                    + File.separatorChar + fieldName + File.separatorChar
                    + fileName;
            if (new File(filePath).exists())
            {
                new File(filePath).delete();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();// 没有就算了，不用删除
        }
    }


    /**
     * 把id1中的文件完全拷贝到id2中 <br>
     * boolean b = Files.copyDir(new File("c:/1"), new File("c:/2"));才会成功 <br>
     * c:\1则会失败
     */
    public static void copyID2ID(String id1,
                                 String id2)
    {
        String folder1 = WebPath.getUploadRootPath() + id1;
        String folder2 = WebPath.getUploadRootPath() + id2;
        try
        {
            Files.copyDir(new File(folder1),
                          new File(folder2));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    /**
     * 
     * @param fileId
     * @return
     */
    public static boolean isFolderAndHasFileByFileId(String fileId)
    {
        String path = StringUT.getUploadFiles(fileId);
        if (isFolderAndHasFile(path))
        {
            return true;
        }
        return false;
    }


    @Deprecated
    /**
     * 建议使用 getPageVO_files
     */
    private static String[] getAllFilesInByFileId(String fileId,
                                                  String fieldName)
    {
        String filepath = StringUT.getUploadFiles(fileId);
        return getAllFilesIn(filepath,
                             fieldName);
    }
    

    private static final byte[] UTF_BOM = new byte[]
    {
            (byte) 0xEF, (byte) 0xBB, (byte) 0xBF
    };


    /**
     * 判断并移除UTF-8的BOM头
     */
    public static InputStream utf8filte(InputStream in)
    {
        try
        {
            PushbackInputStream pis = new PushbackInputStream(in, 3);
            byte[] header = new byte[3];
            pis.read(header,
                     0,
                     3);
            if (header[0] != UTF_BOM[0] || header[1] != UTF_BOM[1]
                    || header[2] != UTF_BOM[2])
            {
                pis.unread(header,
                           0,
                           3);
            }
            return pis;
        }
        catch (IOException e)
        {
            throw Lang.wrapThrow(e);
        }
    }
    

    /**
     * 指定物理文件的路径，返回全部的文本内容
     * @param string
     * @return
     */
    public static String readAll(String filepath)
    {
        String out = "";
        try
        {
            out = FileUtils.readFileToString(new File(filepath));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return out;
    }
    
    
    /**
     * 
     * 读取configs.noticetemplate下的文件,[参数只需要填写文件名]
     * 
     * @param string eg:"qiyexinxi.auth.html"
     * @return
     */
    public static String readAll_configs_noticetemplate(String filepath)
    {
        filepath = WebPath.getClassRootPath() + "configs"
        + File.separatorChar + "noticetemplate" + File.separatorChar
        + filepath;
        
        String out = "";
        try
        {
            out = FileUtils.readFileToString(new File(filepath),"UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return out;
    }
    
    
    public static void main(String[] args)
    {
        String filepath = WebPath.getClassRootPath() + "configs"
                + File.separatorChar + "noticetemplate" + File.separatorChar
                + "qiyexinxi.auth.html";
        System.out.println(readAll(filepath));
        
    }

}
