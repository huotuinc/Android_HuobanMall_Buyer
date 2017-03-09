package com.huotu.partnermall.image;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/7.
 */
public abstract class IDownloadResult {

    private String mFilePath;

    public IDownloadResult(String filePath) {
        this.mFilePath = filePath;
    }

    public IDownloadResult(Context context) {
        this.mFilePath = ImageFileUtils.getImageDownloadPath(context);
    }

    public String getFilePath() {
        return mFilePath;
    }

    public abstract void onResult(String filePath);

}



