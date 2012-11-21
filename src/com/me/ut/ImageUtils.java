package com.me.ut;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * 图片处理工具类
 * 
 * @author Administrator
 */
public class ImageUtils {

    /**
     * 几种常见的图片格式
     */
    public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
    public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
    public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
    public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
    public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
    public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop

    /**
     * 程序入口：用于测试
     * 
     * @param args
     */
    public static void main(String[] args) {

    }

    /**
     * 缩放图像（按比例缩放）
     * 
     * @param srcImageFile
     *            源图像文件地址
     * @param result
     *            缩放后的图像地址
     * @param scale
     *            缩放比例 [如果是缩小，传过来的参数肯定是小于1的，放大就是大于1的]
     */
    public final static void scale(String srcImageFile, String result,
	    double scale) {
	FileInputStream is = null;
	Graphics g = null;
	try {

	    int width = getW(srcImageFile); // 得到源图宽
	    int height = getH(srcImageFile); // 得到源图长
	    is = new FileInputStream(srcImageFile);
	    BufferedImage src = ImageIO.read(is); // 读入文件

	    width = (int) Math.round(width * scale);
	    height = (int) Math.round(height * scale);

	    Image image = src.getScaledInstance(width, height,
		    Image.SCALE_DEFAULT);
	    BufferedImage tag = new BufferedImage(width, height,
		    BufferedImage.TYPE_INT_RGB);
	    g = tag.getGraphics();
	    g.drawImage(image, 0, 0, null); // 绘制缩小后的图
	    ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {

	    try {
		is.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    g.dispose();
	}
    }

    /**
     * 对图片裁剪，并把裁剪完并把新图片保存 。
     */
    public static void cut(String src_pic, String target_pic, int x, int y,
	    int w, int h)

    {

	FileInputStream is = null;
	ImageInputStream iis = null;

	try {
	    // 读取图片文件
	    is = new FileInputStream(src_pic);

	    /*
	     * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 声称能够解码指定格式。
	     * 参数：formatName - 包含非正式格式名称 . （例如 "jpeg" 或 "tiff"）等 。
	     */
	    Iterator<ImageReader> it = ImageIO
		    .getImageReadersByFormatName(IMAGE_TYPE_JPG);
	    ImageReader reader = it.next();
	    // 获取图片流
	    iis = ImageIO.createImageInputStream(is);

	    /*
	     * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索’。
	     * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
	     */
	    reader.setInput(iis, true);

	    /*
	     * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
	     * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 将从其 ImageReader 实现的
	     * getDefaultReadParam 方法中返回 ImageReadParam 的实例。
	     */
	    ImageReadParam param = reader.getDefaultReadParam();

	    /*
	     * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
	     * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
	     */
	    Rectangle rect = new Rectangle(x, y, w, h);

	    // 提供一个 BufferedImage，将其用作解码像素数据的目标。
	    param.setSourceRegion(rect);

	    /*
	     * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
	     * BufferedImage 返回。
	     */
	    BufferedImage bi = reader.read(0, param);

	    // 保存新图片
	    ImageIO.write(bi, IMAGE_TYPE_JPG, new File(target_pic));
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (is != null)
		try {
		    is.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    if (iis != null)
		try {
		    iis.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

    }

    public static int getH(String filePath) {
	int h = 0;
	InputStream is = null;
	try {
	    is = new FileInputStream(filePath);

	    BufferedImage buff = ImageIO.read(is);
	    int w = buff.getWidth(); // 得到图片的宽度
	    h = buff.getHeight(); // 得到图片的高度
	    System.out.println(filePath + "==" + w + ":" + h);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		is.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return h;
    }

    public static int getW(String filePath) {
	int w = 0;
	InputStream is = null;
	try {
	    is = new FileInputStream(filePath);

	    BufferedImage buff = ImageIO.read(is);
	    w = buff.getWidth(); // 得到图片的宽度
	    int h = buff.getHeight(); // 得到图片的高度
	    System.out.println(filePath + "==" + w + ":" + h);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		is.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    } // 关闭Stream
	}
	return w;
    }

}