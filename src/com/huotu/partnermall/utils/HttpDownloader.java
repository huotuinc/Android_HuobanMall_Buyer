package com.huotu.partnermall.utils;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2015/10/28.
 */
public
class HttpDownloader extends AsyncTask<String, Void, Integer> {


    private URL url = null;

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     */
    public InputStream getInputStreamFromUrl(String urlStr)
            throws MalformedURLException, IOException {
        url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConn.getInputStream();
        return inputStream;
    }

    @Override
    protected
    Integer doInBackground ( String... params ) {
        InputStream inputStream = null;
        try {
            FileUtils fileUtils = new FileUtils();

            fileUtils.isFileExist("buyer" + File.separator + "update.zip");
            inputStream = getInputStreamFromUrl(params[0]);
            File resultFile = fileUtils.writeToSDFromInput("buyer","update.zip", inputStream);
            if (resultFile == null) {
               return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    protected
    void onPostExecute ( Integer integer ) {
        super.onPostExecute ( integer );
        if(0 == integer)
        {
            String dir = Environment.getExternalStorageDirectory ( ) + File.separator;
            //下载成功后解压zip文件
            File zipFile = new File ( dir + "buyer" + File.separator + "update.zip" );
            try {
                FileZipUtil.getInstance ().upZipFile ( zipFile , dir + "buyer" );
            }
            catch ( IOException e ) {
                e.printStackTrace ( );

            }
        }

    }
}
