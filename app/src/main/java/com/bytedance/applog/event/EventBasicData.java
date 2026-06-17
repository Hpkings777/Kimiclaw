package com.bytedance.applog.event;

import defpackage.AbstractC2097ev0;
import defpackage.T00;
import defpackage.Zd1;

/* JADX INFO: loaded from: classes.dex */
@T00
public class EventBasicData {
    public final String abSdkVersion;
    public final long eventCreateTime;
    public final long eventIndex;
    public final String sessionId;
    public final String ssid;
    public final String uuid;
    public final String uuidType;

    public EventBasicData(Zd1 zd1) {
        this.eventIndex = zd1.d;
        this.eventCreateTime = zd1.c;
        this.sessionId = zd1.e;
        this.uuid = zd1.g;
        this.uuidType = zd1.h;
        this.ssid = zd1.i;
        this.abSdkVersion = zd1.j;
    }

    public String getAbSdkVersion() {
        return this.abSdkVersion;
    }

    public long getEventCreateTime() {
        return this.eventCreateTime;
    }

    public long getEventIndex() {
        return this.eventIndex;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getSsid() {
        return this.ssid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getUuidType() {
        return this.uuidType;
    }

    public String toString() {
        StringBuilder sbE = AbstractC2097ev0.e("EventBasisData{eventIndex=");
        sbE.append(this.eventIndex);
        sbE.append(", eventCreateTime=");
        sbE.append(this.eventCreateTime);
        sbE.append(", sessionId='");
        sbE.append(this.sessionId);
        sbE.append('\'');
        sbE.append(", uuid='");
        sbE.append(this.uuid);
        sbE.append('\'');
        sbE.append(", uuidType='");
        sbE.append(this.uuidType);
        sbE.append('\'');
        sbE.append(", ssid='");
        sbE.append(this.ssid);
        sbE.append('\'');
        sbE.append(", abSdkVersion='");
        sbE.append(this.abSdkVersion);
        sbE.append('\'');
        sbE.append('}');
        return sbE.toString();
    }
}
