package com.bytedance.apm.impl;

import android.text.TextUtils;
import com.bytedance.services.apm.api.IMonitorLogManager;
import defpackage.AbstractC0539Jv0;
import defpackage.AbstractC2124f51;
import defpackage.AbstractC4017sd1;
import defpackage.AbstractC4912z21;
import defpackage.C0967Sc;
import defpackage.C1574b81;
import defpackage.C2261g41;
import defpackage.K91;
import defpackage.O31;
import defpackage.R21;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class MonitorLogManagerImpl implements IMonitorLogManager {
    private static R21 getLogStoreByType(String str) {
        return TextUtils.equals(str, "network") ? (R21) ((HashMap) AbstractC2124f51.a.b).get(C2261g41.class) : (R21) ((HashMap) AbstractC2124f51.a.b).get(C1574b81.class);
    }

    private static String packLog(JSONArray jSONArray, long j) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("data", jSONArray);
            JSONObject jSONObjectD = AbstractC4017sd1.d();
            if (jSONObjectD == null) {
                return "";
            }
            JSONObject jSONObject2 = new JSONObject(jSONObjectD.toString());
            AbstractC0539Jv0.R(jSONObject2, AbstractC4912z21.a.c(j));
            jSONObject2.put("debug_fetch", 1);
            jSONObject.put("header", jSONObject2);
            return jSONObject.toString();
        } catch (Exception unused) {
            return "";
        }
    }

    @Override // com.bytedance.services.apm.api.IMonitorLogManager
    public void deleteLegacyLogByIds(String str, String str2) {
        R21 logStoreByType = getLogStoreByType(str);
        if (logStoreByType != null) {
            logStoreByType.k(str2);
        }
    }

    @Override // com.bytedance.services.apm.api.IMonitorLogManager
    public void getLegacyLog(long j, long j2, String str, K91 k91) {
        List<C1574b81> listE;
        if (k91 == null || TextUtils.isEmpty(str)) {
            return;
        }
        R21 logStoreByType = getLogStoreByType(str);
        if (logStoreByType == null) {
            k91.a();
            return;
        }
        synchronized (logStoreByType) {
            if (TextUtils.isEmpty(str)) {
                listE = Collections.EMPTY_LIST;
            } else {
                try {
                    String[] strArr = {String.valueOf(j), String.valueOf(j2), str};
                    String[] strArrSplit = "0,100".split(",");
                    listE = logStoreByType.e("timestamp >= ? AND timestamp <= ?  AND type2 = ? ", strArr, "_id ASC " + (strArrSplit.length == 2 ? " LIMIT " + strArrSplit[1] + " OFFSET " + strArrSplit[0] : ""), logStoreByType);
                } catch (Throwable unused) {
                    listE = Collections.EMPTY_LIST;
                }
            }
        }
        if (AbstractC0539Jv0.W(listE)) {
            k91.a();
            return;
        }
        JSONArray jSONArray = new JSONArray();
        LinkedList linkedList = new LinkedList();
        long j3 = -1;
        for (C1574b81 c1574b81 : listE) {
            try {
                if (j3 == -1) {
                    j3 = c1574b81.e;
                } else if (c1574b81.e != j3) {
                    break;
                }
                jSONArray.put(c1574b81.d);
                linkedList.add(Long.valueOf(c1574b81.a));
            } catch (Exception unused2) {
            }
        }
        packLog(jSONArray, j3);
        AbstractC0539Jv0.j(",", linkedList);
        k91.a();
    }

    @Override // com.bytedance.services.apm.api.IMonitorLogManager
    public List<JSONObject> getRecentUiActionRecords() {
        return (LinkedList) ((C0967Sc) O31.g().b).c;
    }
}
