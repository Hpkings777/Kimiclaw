package com.bytedance.applog.exposure;

import defpackage.AbstractC2097ev0;
import defpackage.T00;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@T00
public final class ViewExposureParam {
    public final JSONObject exposureParam;

    /* JADX WARN: Multi-variable type inference failed */
    public ViewExposureParam() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public static /* synthetic */ ViewExposureParam copy$default(ViewExposureParam viewExposureParam, JSONObject jSONObject, int i, Object obj) {
        if ((i & 1) != 0) {
            jSONObject = viewExposureParam.exposureParam;
        }
        return viewExposureParam.copy(jSONObject);
    }

    public final JSONObject component1() {
        return this.exposureParam;
    }

    public final ViewExposureParam copy(JSONObject exposureParam) {
        Intrinsics.checkParameterIsNotNull(exposureParam, "exposureParam");
        return new ViewExposureParam(exposureParam);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            return (obj instanceof ViewExposureParam) && Intrinsics.areEqual(this.exposureParam, ((ViewExposureParam) obj).exposureParam);
        }
        return true;
    }

    public final JSONObject getExposureParam() {
        return this.exposureParam;
    }

    public int hashCode() {
        JSONObject jSONObject = this.exposureParam;
        if (jSONObject != null) {
            return jSONObject.hashCode();
        }
        return 0;
    }

    public String toString() {
        StringBuilder sbE = AbstractC2097ev0.e("ViewExposureParam(exposureParam=");
        sbE.append(this.exposureParam);
        sbE.append(")");
        return sbE.toString();
    }

    public ViewExposureParam(JSONObject exposureParam) {
        Intrinsics.checkParameterIsNotNull(exposureParam, "exposureParam");
        this.exposureParam = exposureParam;
    }

    public /* synthetic */ ViewExposureParam(JSONObject jSONObject, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new JSONObject() : jSONObject);
    }
}
