package com.bytedance.apm.impl.net;

import com.bytedance.services.apm.api.HttpResponse;
import com.bytedance.services.apm.api.IHttpService;
import defpackage.AbstractC0539Jv0;
import defpackage.C1342Zh0;
import defpackage.C2133f81;
import defpackage.C3385o61;
import defpackage.C4781y51;
import defpackage.Fa1;
import defpackage.InterfaceC2819k41;
import java.io.File;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class UserHttpServiceImpl implements IHttpService {
    private static String METHOD_GET = "GET";
    private static String METHOD_POST = "POST";
    private InterfaceC2819k41 iNetworkClient;

    public UserHttpServiceImpl(InterfaceC2819k41 interfaceC2819k41) {
        this.iNetworkClient = interfaceC2819k41;
    }

    private HttpResponse changeToHttpResponse(C3385o61 c3385o61) {
        return new HttpResponse(c3385o61.a, null, c3385o61.b);
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public Fa1 buildMultipartUpload(String str, String str2, boolean z) {
        return new C2133f81(str, str2, null, z);
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public HttpResponse doGet(String str, Map<String, String> map) {
        ((C4781y51) this.iNetworkClient).a.c.getNetworkClient().getClass();
        return changeToHttpResponse(null);
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public HttpResponse doPost(String str, byte[] bArr, Map<String, String> map) {
        C1342Zh0 c1342Zh0B = ((C4781y51) this.iNetworkClient).a.c.getNetworkClient().b(str, bArr, map);
        return changeToHttpResponse(new C3385o61(c1342Zh0B.a, c1342Zh0B.b));
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public HttpResponse uploadFiles(String str, List<File> list, Map<String, String> map) {
        return AbstractC0539Jv0.f(str, list, map);
    }

    @Override // com.bytedance.services.apm.api.IHttpService
    public Fa1 buildMultipartUpload(String str, String str2, Map<String, String> map, boolean z) {
        return new C2133f81(str, str2, map, z);
    }
}
