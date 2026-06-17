package com.bytedance.apm.net;

import android.text.TextUtils;
import com.bytedance.services.apm.api.HttpResponse;
import com.bytedance.services.apm.api.IHttpService;
import defpackage.AbstractC0539Jv0;
import defpackage.AbstractC4017sd1;
import defpackage.C3665q61;
import defpackage.C41;
import defpackage.Fa1;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/* JADX INFO: loaded from: classes.dex */
public class DefaultHttpServiceImpl implements IHttpService {
    private static String METHOD_GET = "GET";
    private static String METHOD_POST = "POST";

    private HttpResponse doRequest(String str, byte[] bArr, String str2, Map<String, String> map) {
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        byte[] byteArray;
        if (str2 == null) {
            throw new IllegalArgumentException("request method is not null");
        }
        try {
            httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            try {
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);
                if (map != null && !map.isEmpty()) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (entry != null) {
                            httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
                        }
                    }
                }
                if (!TextUtils.isEmpty(AbstractC4017sd1.w)) {
                    httpURLConnection.setRequestProperty("aid", AbstractC4017sd1.a());
                    httpURLConnection.setRequestProperty("x-auth-token", AbstractC4017sd1.w);
                }
                if (TextUtils.equals(str2, METHOD_POST)) {
                    httpURLConnection.setDoOutput(true);
                } else {
                    httpURLConnection.setDoOutput(false);
                }
                httpURLConnection.setRequestMethod(str2);
                if (bArr != null && bArr.length > 0) {
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.write(bArr);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                }
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != 200) {
                    HttpResponse httpResponse = new HttpResponse(responseCode, null);
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception unused) {
                    }
                    return httpResponse;
                }
                inputStream = httpURLConnection.getInputStream();
                try {
                    String contentEncoding = httpURLConnection.getContentEncoding();
                    if (TextUtils.isEmpty(contentEncoding) || !contentEncoding.equalsIgnoreCase("gzip")) {
                        byteArray = toByteArray(inputStream);
                    } else {
                        GZIPInputStream gZIPInputStream = new GZIPInputStream(inputStream);
                        byteArray = toByteArray(gZIPInputStream);
                        gZIPInputStream.close();
                    }
                    Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
                    HashMap map2 = new HashMap();
                    for (String str3 : headerFields.keySet()) {
                        List<String> list = headerFields.get(str3);
                        if (list != null && !AbstractC0539Jv0.W(list)) {
                            map2.put(str3, list.get(0));
                        }
                    }
                    HttpResponse httpResponse2 = new HttpResponse(responseCode, map2, byteArray);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception unused2) {
                        }
                    }
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception unused3) {
                    }
                    return httpResponse2;
                } catch (Throwable th) {
                    th = th;
                    try {
                        String str4 = C41.a;
                        th.getMessage();
                        AbstractC0539Jv0.l(th, 10);
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Exception unused4) {
                            }
                        }
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Exception unused5) {
                            }
                        }
                        return null;
                    } finally {
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                inputStream = null;
            }
        } catch (Throwable th3) {
            th = th3;
            httpURLConnection = null;
            inputStream = null;
        }
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[8192];
        while (true) {
            int i = inputStream.read(bArr);
            if (-1 == i) {
                inputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, i);
        }
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public Fa1 buildMultipartUpload(String str, String str2, boolean z) {
        return new C3665q61(str, str2, null, z);
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public HttpResponse doGet(String str, Map<String, String> map) {
        return doRequest(str, null, METHOD_GET, map);
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public HttpResponse doPost(String str, byte[] bArr, Map<String, String> map) {
        return doRequest(str, bArr, METHOD_POST, map);
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public HttpResponse uploadFiles(String str, List<File> list, Map<String, String> map) {
        return AbstractC0539Jv0.f(str, list, map);
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public Fa1 buildMultipartUpload(String str, String str2, Map<String, String> map, boolean z) {
        return new C3665q61(str, str2, map, z);
    }
}
