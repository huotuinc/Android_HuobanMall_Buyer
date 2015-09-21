package com.huotu.partnermall.utils;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

/**
 * 买家版自定义的volley网络请求时间设置
 */
public
class MallRetryPolicy implements RetryPolicy {
    private int mCurrentTimeoutMs;
    private int mCurrentRetryCount;
    private final int mMaxNumRetries;
    private final float mBackoffMultiplier;
    //设置10秒超时
    public static final int DEFAULT_TIMEOUT_MS = 10000;
    public static final int DEFAULT_MAX_RETRIES = 1;
    public static final float DEFAULT_BACKOFF_MULT = 1.0F;

    public MallRetryPolicy() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    public MallRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
        this.mCurrentTimeoutMs = initialTimeoutMs;
        this.mMaxNumRetries = maxNumRetries;
        this.mBackoffMultiplier = backoffMultiplier;
    }

    public int getCurrentTimeout() {
        return this.mCurrentTimeoutMs;
    }

    public int getCurrentRetryCount() {
        return this.mCurrentRetryCount;
    }

    public void retry(VolleyError error) throws VolleyError {
        ++this.mCurrentRetryCount;
        this.mCurrentTimeoutMs = (int)((float)this.mCurrentTimeoutMs + (float)this.mCurrentTimeoutMs * this.mBackoffMultiplier);
        if(!this.hasAttemptRemaining()) {
            throw error;
        }
    }

    protected boolean hasAttemptRemaining() {
        return this.mCurrentRetryCount <= this.mMaxNumRetries;
    }
}
