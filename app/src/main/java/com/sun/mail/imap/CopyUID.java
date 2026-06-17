package com.sun.mail.imap;

import com.sun.mail.imap.protocol.UIDSet;

/* JADX INFO: loaded from: classes.dex */
public class CopyUID {
    public UIDSet[] dst;
    public UIDSet[] src;
    public long uidvalidity;

    public CopyUID(long j, UIDSet[] uIDSetArr, UIDSet[] uIDSetArr2) {
        this.uidvalidity = j;
        this.src = uIDSetArr;
        this.dst = uIDSetArr2;
    }
}
