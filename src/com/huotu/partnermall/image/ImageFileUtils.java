package com.huotu.partnermall.image;

import android.content.Context;
import android.os.Environment;

import com.huotu.partnermall.inner.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by Administrator on 2017/3/7.
 */
public class ImageFileUtils {
    /**
     * 本地与我们应用程序相关文件存放的根目录
     */
    private static final String ROOT_DIR_PATH = "/huotu";

    /**
     * 下载图片文件存放的目录
     */
    private static final String IMAGE_DOWNLOAD_IMAGES_PATH = "/Download/Images/";

    /**
     * 获取下载图片文件存放的目录
     *
     * @param context Context
     * @return SDCard卡或者手机内存的根路径
     */
    public static String getImageDownloadDir(Context context) {
        return getRootDir(context) + IMAGE_DOWNLOAD_IMAGES_PATH;
    }

    /**
     * 获取SDCard卡或者手机内存的根路径（优先获取SDCard卡的根路径）
     *
     * @param context Context
     * @return SDCard卡或者手机内存的根路径
     */
    public static String getRootDir(Context context) {
        String rootDir = null;
        String rootPath= "/"+context.getString(R.string.app_name);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + rootPath;
        } else {
            rootDir = context.getApplicationContext().getCacheDir().getAbsolutePath() + rootPath;
        }
        return rootDir;
    }

    /**
     * 随机生成一个文件，用于存放下载的图片
     * @param context Context
     * @return
     */
    public static String getImageDownloadPath(Context context) {
        String imageRootDir = getImageDownloadDir(context);
        File dir = new File(imageRootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + ".jpg";
        return dir + File.separator + fileName;
    }



    /**
     * 根据文件路径获取byte[]
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public static byte[] read(String path) throws IOException {
        return read(new FileInputStream(path));
    }


    /**
     * 从输入流读取数据
     *
     * @param inStream
     * @return  byte[]
     * @throws IOException
     * @throws Exception
     */
    public static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.flush();
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 将byte[]写入指定的文件
     * @param filePath 指定文件的路径
     * @param data byte[]
     * @throws IOException
     */
    public static void write(String filePath, byte[] data) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(data);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

}
