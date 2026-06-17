package com.bytedance.applog.encryptor;

/* JADX INFO: loaded from: classes.dex */
public abstract class EncryptorUtil {
    static {
        try {
            System.loadLibrary("EncryptorP");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

    public static byte[] a(int i, byte[] bArr) {
        if (bArr != null && i > 0) {
            try {
                if (bArr.length == i) {
                    return ttEncrypt(bArr, i);
                }
            } catch (Throwable unused) {
            }
        }
        return null;
    }

    private static native byte[] ttEncrypt(byte[] bArr, int i);
}
