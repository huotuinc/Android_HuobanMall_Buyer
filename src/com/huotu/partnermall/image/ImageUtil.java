package com.huotu.partnermall.image;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.huotu.partnermall.config.Constants;

public class ImageUtil {
      
	public static void saveInputStreanToFile(InputStream in, String fullPath){
		String path = fullPath.substring(0, fullPath.lastIndexOf("/"));
		File dir = new File(path);
		if (!dir.exists())
			dir.mkdirs();
		File file = new File(fullPath);
		if(!file.exists()){
			FileOutputStream foutput = null ;
			BufferedOutputStream bos = null;

			try {
				foutput = new FileOutputStream(file);
				bos = new BufferedOutputStream(foutput);
				int b;
				while ((b = in.read()) != -1) {
					bos.write(b);
					bos.flush();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(in != null)
					in.close();
					if(bos != null)
					bos.close();
					if(foutput != null)
					foutput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}



	}
	/**
	 * 一种挺有效的方法，规避BitmapFactory.decodeStream或者decodeFile函数，使用BitmapFactory.decodeFileDescriptor
	 * @param path
	 * @return
	 */
	public static  Bitmap readBitmapByPath(String fullPath)   {
		if(TextUtils.isEmpty(fullPath))
			return null;
	    BitmapFactory.Options bfOptions=new BitmapFactory.Options();
	    bfOptions.inDither=false;
	    bfOptions.inPurgeable=true;
	    bfOptions.inInputShareable=true;
	    bfOptions.inTempStorage=new byte[32 * 1024];

	    File file=new File(fullPath);
	    FileInputStream fs=null;
	    try {
	    	fs = new FileInputStream(file);
	        if(fs!=null)
	        	return BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally{
	        if(fs!=null) {
	                try {
						fs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	        }
	    }
	    return null;
	}
	/**
	 * //取得文件夹大小
	 * @param f
	 * @return
	 */
    public static long getFileSize(String path ){
    	long size = 0;
    	File file  = new File(path);
    	if(!file.exists())
    		return size;

        File flist[] = file.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory()){
                size = size + getFileSize(flist[i].getAbsolutePath());
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }
	public static void deleteFileByPath(String path){
		if(path !=null){
			File imgDir = new File(path);
			if(!imgDir.exists()){
				return;
			}else{
				if(imgDir.isDirectory()){
					File[] files = imgDir.listFiles();
			        if (files != null)
			        	for(int i = 0; i < files.length; i ++){
			        		if(files[i].isDirectory())
			        			deleteFileByPath(files[i].getAbsolutePath());
			        		else
			        			files[i].delete();
			        	}

			        imgDir.delete();
				}
			}
		}
	}
	public static void checkFile(final String path, final List<String> imageUrls) {
		(new Thread(){
			public void run(){
				if(path !=null ){
					File imgDir = new File(path);
					if(!imgDir.exists()){
						return;
					}else{
						if(imgDir.isDirectory()){
							File[] files = imgDir.listFiles();
					        if (files != null) {
					        	for(int i = 0; i < files.length; i ++) {
					        		boolean needDelete = true;
					        		for(int m = 0; m < imageUrls.size(); m ++){
					        			String fileName = imageUrls.get(m).substring(imageUrls.get(m).lastIndexOf("/") + 1);
					        			if(files[i].getName().equals(fileName)){
					        				needDelete = false;
					        			}
					        		}

					        		if(needDelete){
					        			files[i].delete();
					        		}
						        }
					        }
						}
					}
				}
			}
		}).start();

	}

	/*
     * 得到图片字节流 数组大小
     * */
	public static byte[] readStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len=inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}


	public static Drawable getBitmap( Resources resources , int resId){
		InputStream inputStream = resources.openRawResource(resId);
		BitmapFactory.Options options =new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeStream( inputStream , null , options );
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;
		int scale = 1;

		//图片实际的宽与高，根据默认最大大小值，得到图片实际的缩放比例
		while ((imageHeight / scale > Constants.SCREEN_HEIGHT )
				|| (imageWidth / scale > Constants.SCREEN_WIDTH)) {
			scale *= 2;
		}

		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;

		Bitmap bmp=null;
		BitmapDrawable drawable=null;
		byte[] buffer;
		try {
			buffer = readStream(inputStream);
			if( buffer!=null ) {
				bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
				drawable = new BitmapDrawable(resources, bmp);
			}
		}catch (Exception ex){
		}

		if( inputStream!=null ){
			try {
				inputStream.close();
			}catch (IOException ex){}
		}

		if( bmp !=null && bmp.isRecycled() ==false ){
			bmp.recycle();
		}
		return drawable;
	}
}
