package com.bytedance.services.apm.api;

import com.bytedance.news.common.service.manager.IService;
import defpackage.Fa1;
import java.io.File;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface IHttpService extends IService {
    Fa1 buildMultipartUpload(String str, String str2, Map<String, String> map, boolean z);

    Fa1 buildMultipartUpload(String str, String str2, boolean z);

    HttpResponse doGet(String str, Map<String, String> map);

    HttpResponse doPost(String str, byte[] bArr, Map<String, String> map);

    HttpResponse uploadFiles(String str, List<File> list, Map<String, String> map);
}
