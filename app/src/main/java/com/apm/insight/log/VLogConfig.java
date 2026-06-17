package com.apm.insight.log;

import android.content.Context;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class VLogConfig {
    private String bufferDirPath;
    private boolean compress;
    private Context context;
    private boolean encrypt;
    private int level;
    private String logDirPath;
    private int logFileExpDays;
    private boolean mainThreadSpeedUp;
    private int maxDirSize;
    private boolean offloadMainThreadWrite;
    private int perSize;
    private String pubKey;
    private float subProcessMaxDirSizeRatio;

    public static class Builder {
        private String bufferDirPath;
        private Context context;
        private String logDirPath;
        private int logFileExpDays = 14;
        private int maxDirSize = 20971520;
        private float subProcessMaxDirSizeRatio = 0.1f;
        private int perSize = 2097152;
        private boolean compress = true;
        private boolean encrypt = true;
        private int level = 2;
        private String pubKey = "44817d17adcfd1bc735c022b368acfe0465c4bdbc5c77ca8efd6b578dad1177a65f83813d3f3da839778719efbb83d982737c55597b1a074f105d828a8163b42";
        private boolean mainThreadSpeedUp = true;
        private boolean offloadMainThreadWrite = false;

        public Builder(Context context) {
            if (context == null) {
                throw new RuntimeException("context must not be null");
            }
            Context applicationContext = context.getApplicationContext();
            this.context = applicationContext != null ? applicationContext : context;
        }

        public VLogConfig build() {
            VLogConfig vLogConfig = new VLogConfig();
            vLogConfig.setContext(this.context);
            vLogConfig.setLogFileExpDays(this.logFileExpDays);
            vLogConfig.setMaxDirSize(this.maxDirSize);
            vLogConfig.setSubProcessMaxDirSizeRatio(this.subProcessMaxDirSizeRatio);
            vLogConfig.setPerSize(this.perSize);
            vLogConfig.setBufferDirPath(TextUtils.isEmpty(this.bufferDirPath) ? c.d(this.context) : this.bufferDirPath);
            vLogConfig.setLogDirPath(TextUtils.isEmpty(this.logDirPath) ? c.c(this.context).getAbsolutePath() : this.logDirPath);
            vLogConfig.setCompress(this.compress);
            vLogConfig.setEncrypt(this.encrypt);
            vLogConfig.setLevel(this.level);
            vLogConfig.setPubKey(this.pubKey);
            vLogConfig.setMainThreadSpeedUp(this.mainThreadSpeedUp);
            vLogConfig.setOffloadMainThreadWrite(this.offloadMainThreadWrite);
            return vLogConfig;
        }

        public Builder setBufferDirPath(String str) {
            this.bufferDirPath = str;
            return this;
        }

        public Builder setCompress(boolean z) {
            this.compress = z;
            return this;
        }

        public Builder setEncrypt(boolean z) {
            this.encrypt = z;
            return this;
        }

        public Builder setLevel(int i) {
            this.level = i;
            return this;
        }

        public Builder setLogDirPath(String str) {
            this.logDirPath = str;
            return this;
        }

        public Builder setLogFileExpDays(int i) {
            this.logFileExpDays = i;
            return this;
        }

        public Builder setMainThreadSpeedUp(boolean z) {
            this.mainThreadSpeedUp = z;
            return this;
        }

        public Builder setMaxDirSize(int i) {
            this.maxDirSize = i;
            return this;
        }

        public Builder setOffloadMainThreadWrite(boolean z) {
            this.offloadMainThreadWrite = z;
            return this;
        }

        public Builder setPerSize(int i) {
            this.perSize = i;
            return this;
        }

        public Builder setPubKey(String str) {
            this.pubKey = str;
            return this;
        }

        public Builder setSubProcessMaxDirSizeRatio(float f) {
            this.subProcessMaxDirSizeRatio = f;
            return this;
        }
    }

    public String getBufferDirPath() {
        return this.bufferDirPath;
    }

    public Context getContext() {
        return this.context;
    }

    public int getLevel() {
        return this.level;
    }

    public String getLogDirPath() {
        return this.logDirPath;
    }

    public int getLogFileExpDays() {
        return this.logFileExpDays;
    }

    public int getMaxDirSize() {
        return this.maxDirSize;
    }

    public int getPerSize() {
        return this.perSize;
    }

    public String getPubKey() {
        return this.pubKey;
    }

    public float getSubProcessMaxDirSizeRatio() {
        return this.subProcessMaxDirSizeRatio;
    }

    public boolean isCompress() {
        return this.compress;
    }

    public boolean isEncrypt() {
        return this.encrypt;
    }

    public boolean isMainThreadSpeedUp() {
        return this.mainThreadSpeedUp;
    }

    public boolean isOffloadMainThreadWrite() {
        return this.offloadMainThreadWrite;
    }

    public void setBufferDirPath(String str) {
        this.bufferDirPath = str;
    }

    public void setCompress(boolean z) {
        this.compress = z;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setEncrypt(boolean z) {
        this.encrypt = z;
    }

    public void setLevel(int i) {
        this.level = i;
    }

    public void setLogDirPath(String str) {
        this.logDirPath = str;
    }

    public void setLogFileExpDays(int i) {
        this.logFileExpDays = i;
    }

    public void setMainThreadSpeedUp(boolean z) {
        this.mainThreadSpeedUp = z;
    }

    public void setMaxDirSize(int i) {
        this.maxDirSize = i;
    }

    public void setOffloadMainThreadWrite(boolean z) {
        this.offloadMainThreadWrite = z;
    }

    public void setPerSize(int i) {
        this.perSize = i;
    }

    public void setPubKey(String str) {
        this.pubKey = str;
    }

    public void setSubProcessMaxDirSizeRatio(float f) {
        this.subProcessMaxDirSizeRatio = f;
    }

    private VLogConfig() {
    }
}
