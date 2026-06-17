package com.tencent.mmkv;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import defpackage.AbstractC2039eV;
import defpackage.C0317Fo0;

/* JADX INFO: loaded from: classes.dex */
public class MMKVContentProvider extends ContentProvider {
    public static Uri a;

    public final Bundle a(String str, int i, int i2, String str2) {
        MMKV mmkvL = MMKV.l(getContext(), str, i, i2, str2);
        C0317Fo0 c0317Fo0 = new C0317Fo0(mmkvL);
        StringBuilder sbU = AbstractC2039eV.u(str, " fd = ");
        sbU.append(mmkvL.ashmemFD());
        sbU.append(", meta fd = ");
        sbU.append(mmkvL.ashmemMetaFD());
        Log.i("MMKV", sbU.toString());
        Bundle bundle = new Bundle();
        bundle.putParcelable("KEY", c0317Fo0);
        return bundle;
    }

    @Override // android.content.ContentProvider
    public final Bundle call(String str, String str2, Bundle bundle) {
        if (str.equals("mmkvFromAshmemID") && bundle != null) {
            try {
                return a(str2, bundle.getInt("KEY_SIZE"), bundle.getInt("KEY_MODE"), bundle.getString("KEY_CRYPT"));
            } catch (Exception e) {
                Log.e("MMKV", e.getMessage());
            }
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public final int delete(Uri uri, String str, String[] strArr) {
        throw new UnsupportedOperationException("Not implement in MMKV");
    }

    @Override // android.content.ContentProvider
    public final String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("Not implement in MMKV");
    }

    @Override // android.content.ContentProvider
    public final boolean onCreate() {
        return getContext() != null;
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        throw new UnsupportedOperationException("Not implement in MMKV");
    }

    @Override // android.content.ContentProvider
    public final int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new UnsupportedOperationException("Not implement in MMKV");
    }
}
