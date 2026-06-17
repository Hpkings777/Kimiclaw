package com.bytedance.applog.oneid;

import defpackage.AbstractC2097ev0;
import defpackage.AbstractC4671xI0;
import defpackage.T00;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes.dex */
@T00
public final class IDBindResult {
    public final String failedIdList;
    public final String newSsid;

    public IDBindResult(String str, String str2) {
        this.newSsid = str;
        this.failedIdList = str2;
    }

    public static /* synthetic */ IDBindResult copy$default(IDBindResult iDBindResult, String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str = iDBindResult.newSsid;
        }
        if ((i & 2) != 0) {
            str2 = iDBindResult.failedIdList;
        }
        return iDBindResult.copy(str, str2);
    }

    public final String component1() {
        return this.newSsid;
    }

    public final String component2() {
        return this.failedIdList;
    }

    public final IDBindResult copy(String str, String str2) {
        return new IDBindResult(str, str2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IDBindResult)) {
            return false;
        }
        IDBindResult iDBindResult = (IDBindResult) obj;
        return Intrinsics.areEqual(this.newSsid, iDBindResult.newSsid) && Intrinsics.areEqual(this.failedIdList, iDBindResult.failedIdList);
    }

    public final String getFailedIdList() {
        return this.failedIdList;
    }

    public final String getNewSsid() {
        return this.newSsid;
    }

    public int hashCode() {
        String str = this.newSsid;
        int iHashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.failedIdList;
        return iHashCode + (str2 != null ? str2.hashCode() : 0);
    }

    public String toString() {
        StringBuilder sbE = AbstractC2097ev0.e("IDBindResult(newSsid=");
        sbE.append(this.newSsid);
        sbE.append(", failedIdList=");
        return AbstractC4671xI0.p(sbE, this.failedIdList, ")");
    }
}
