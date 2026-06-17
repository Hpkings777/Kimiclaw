package com.bytedance.apm.impl;

import com.bytedance.retrofit2.client.Header;
import com.bytedance.retrofit2.mime.TypedByteArray;
import com.bytedance.services.apm.api.HttpResponse;
import com.bytedance.services.apm.api.IHttpService;
import com.bytedance.ttnet.utils.RetrofitUtils;
import defpackage.AbstractC0539Jv0;
import defpackage.AbstractC4671xI0;
import defpackage.C3449oa1;
import defpackage.Fa1;
import defpackage.InterfaceC3953s91;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DefaultTTNetImpl implements IHttpService {
    private List<Header> convertHeaderMap(Map<String, String> map) {
        ArrayList arrayList = new ArrayList();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry != null) {
                    arrayList.add(new Header(entry.getKey(), entry.getValue()));
                }
            }
        }
        return arrayList;
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[8192];
        if (inputStream == null) {
            return new byte[0];
        }
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
        return new C3449oa1(str);
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public HttpResponse doGet(String str, Map<String, String> map) {
        URL url = new URL(str);
        throw AbstractC4671xI0.e(RetrofitUtils.createSsService(url.getProtocol() + "://" + url.getHost(), InterfaceC3953s91.class));
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public HttpResponse doPost(String str, byte[] bArr, Map<String, String> map) {
        URL url = new URL(str);
        if (RetrofitUtils.createSsService(url.getProtocol() + "://" + url.getHost(), InterfaceC3953s91.class) != null) {
            throw new ClassCastException();
        }
        convertHeaderMap(map);
        new TypedByteArray("application/json; charset=utf-8", bArr, new String[0]);
        throw null;
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public HttpResponse uploadFiles(String str, List<File> list, Map<String, String> map) {
        return AbstractC0539Jv0.f(str, list, map);
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public Fa1 buildMultipartUpload(String str, String str2, Map<String, String> map, boolean z) {
        return new C3449oa1(str);
    }
}
