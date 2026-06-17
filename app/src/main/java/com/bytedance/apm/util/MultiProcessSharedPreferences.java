package com.bytedance.apm.util;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Pair;
import defpackage.AbstractC0539Jv0;
import defpackage.C4157td1;
import defpackage.SharedPreferencesEditorC2900ke0;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class MultiProcessSharedPreferences extends ContentProvider implements SharedPreferences {
    public static final /* synthetic */ int b = 0;
    public ContentResolver a;

    public static void a(SharedPreferences sharedPreferences, Bundle bundle) {
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        if (bundle.getBoolean("clear")) {
            editorEdit.clear();
        }
        ArrayList<C4157td1> parcelableArrayList = bundle.getParcelableArrayList("edit");
        if (parcelableArrayList == null) {
            return;
        }
        for (C4157td1 c4157td1 : parcelableArrayList) {
            Object obj = c4157td1.b;
            String str = c4157td1.a;
            if (obj == null) {
                editorEdit.remove(str);
            } else if (obj instanceof String) {
                editorEdit.putString(str, (String) obj);
            } else if (obj instanceof Integer) {
                editorEdit.putInt(str, ((Integer) obj).intValue());
            } else if (obj instanceof Long) {
                editorEdit.putLong(str, ((Long) obj).longValue());
            } else if (obj instanceof Float) {
                editorEdit.putFloat(str, ((Float) obj).floatValue());
            } else if (obj instanceof Boolean) {
                editorEdit.putBoolean(str, ((Boolean) obj).booleanValue());
            } else if (obj instanceof String[]) {
                editorEdit.putStringSet(str, new HashSet(Arrays.asList((String[]) obj)));
            }
        }
        editorEdit.commit();
    }

    public static void b(Object obj, String str) {
        Bundle bundle = new Bundle();
        if (obj != null) {
            bundle.putParcelable("sp", new C4157td1(str, obj));
        }
        try {
            throw null;
        } catch (Exception unused) {
        }
    }

    @Override // android.content.ContentProvider
    public final Bundle call(String str, String str2, Bundle bundle) {
        int i;
        Object obj;
        Pair pair;
        C4157td1 c4157td1;
        if (bundle != null) {
            bundle.setClassLoader(getClass().getClassLoader());
        }
        Uri uri = Uri.parse(str);
        synchronized (this) {
            try {
                List<String> pathSegments = uri.getPathSegments();
                i = 0;
                obj = null;
                pair = (pathSegments == null || pathSegments.size() < 2 || !"sp".equals(pathSegments.get(0))) ? null : new Pair(getContext().getSharedPreferences(pathSegments.get(1), 0), pathSegments.size() > 2 ? pathSegments.get(2) : null);
            } catch (Throwable th) {
                throw th;
            }
        }
        if (pair != null) {
            if ("query".equals(str2)) {
                if (bundle != null && (c4157td1 = (C4157td1) bundle.getParcelable("sp")) != null) {
                    obj = c4157td1.b;
                }
                SharedPreferences sharedPreferences = (SharedPreferences) pair.first;
                String str3 = (String) pair.second;
                Bundle bundle2 = new Bundle();
                ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
                if (str3 == null) {
                    for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
                        arrayList.add(new C4157td1(entry.getKey(), entry.getValue()));
                    }
                    bundle2.putParcelableArrayList("sp", arrayList);
                    return bundle2;
                }
                Object obj2 = sharedPreferences.getAll().get(str3);
                if (obj2 != null) {
                    obj = obj2;
                }
                if (obj instanceof Set) {
                    Set set = (Set) obj;
                    String[] strArr = new String[set.size()];
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        strArr[i] = (String) it.next();
                        i++;
                    }
                    obj = strArr;
                }
                arrayList.add(new C4157td1(str3, obj));
                bundle2.putParcelableArrayList("sp", arrayList);
                return bundle2;
            }
            if ("contains".equals(str2)) {
                SharedPreferences sharedPreferences2 = (SharedPreferences) pair.first;
                String str4 = (String) pair.second;
                Bundle bundle3 = new Bundle();
                bundle3.putBoolean("contains", sharedPreferences2.contains(str4));
                return bundle3;
            }
            if ("edit".equals(str2) && bundle != null) {
                try {
                    a((SharedPreferences) pair.first, bundle);
                    long jClearCallingIdentity = Binder.clearCallingIdentity();
                    this.a.notifyChange(Uri.parse(str), null);
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return null;
                } catch (Throwable th2) {
                    AbstractC0539Jv0.N("MultiProcessSharedPref", "edit", th2);
                }
            }
        }
        return null;
    }

    @Override // android.content.SharedPreferences
    public final boolean contains(String str) {
        return false;
    }

    @Override // android.content.ContentProvider
    public final int delete(Uri uri, String str, String[] strArr) {
        return -1;
    }

    @Override // android.content.SharedPreferences
    public final SharedPreferences.Editor edit() {
        return new SharedPreferencesEditorC2900ke0(this);
    }

    @Override // android.content.SharedPreferences
    public final Map getAll() {
        b(null, null);
        return null;
    }

    @Override // android.content.SharedPreferences
    public final boolean getBoolean(String str, boolean z) {
        b(String.valueOf(z), str);
        return z;
    }

    @Override // android.content.SharedPreferences
    public final float getFloat(String str, float f) {
        b(String.valueOf(f), str);
        return f;
    }

    @Override // android.content.SharedPreferences
    public final int getInt(String str, int i) {
        b(String.valueOf(i), str);
        return i;
    }

    @Override // android.content.SharedPreferences
    public final long getLong(String str, long j) {
        b(String.valueOf(j), str);
        return j;
    }

    @Override // android.content.SharedPreferences
    public final String getString(String str, String str2) {
        b(str2, str);
        return null;
    }

    @Override // android.content.SharedPreferences
    public final Set getStringSet(String str, Set set) {
        b(set, str);
        return null;
    }

    @Override // android.content.ContentProvider
    public final String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final boolean onCreate() {
        this.a = getContext().getContentResolver();
        return false;
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    @Override // android.content.SharedPreferences
    public final void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }

    @Override // android.content.SharedPreferences
    public final void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }

    @Override // android.content.ContentProvider
    public final int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return -1;
    }
}
