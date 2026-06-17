package com.bytedance.applog.picker;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import defpackage.Aa1;
import defpackage.AbstractC1993e81;
import defpackage.AbstractC2097ev0;
import defpackage.AbstractC2901ke1;
import defpackage.AbstractC4294uc1;
import defpackage.B70;
import defpackage.C2267g61;
import defpackage.C2407h61;
import defpackage.C2471ha1;
import defpackage.C3399oC;
import defpackage.C4793y91;
import defpackage.C4848ya1;
import defpackage.C4985zZ0;
import defpackage.Me1;
import defpackage.N71;
import defpackage.Pe1;
import defpackage.T91;
import defpackage.Tb1;
import defpackage.Ue1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DomSender extends N71 implements Handler.Callback {
    public static final long[] q = {1000};
    public final Handler g;
    public final Handler h;
    public int i;
    public int j;
    public final Context k;
    public final String l;
    public final C4793y91 m;
    public final String n;
    public final String o;
    public final C2407h61 p;

    public DomSender(C2471ha1 c2471ha1, String str) {
        super(c2471ha1);
        this.g = new Handler(Looper.getMainLooper(), this);
        HandlerThread handlerThread = new HandlerThread("dom_work");
        handlerThread.start();
        this.h = new Handler(handlerThread.getLooper(), this);
        this.m = new C4793y91(this.f);
        this.p = new C2407h61(this.f, this, Looper.myLooper());
        this.k = c2471ha1.c.j;
        Pe1 pe1 = c2471ha1.h;
        this.l = pe1.c.c.a;
        this.n = pe1.u();
        T91 t91 = this.f;
        Object objF = null;
        if (!t91.b("getHeaderValue")) {
            Pe1 pe12 = t91.l;
            objF = pe12.h.f.f(pe12.d, "resolution", null, String.class);
        }
        String str2 = (String) objF;
        if (AbstractC2901ke1.q(str2)) {
            String[] strArrSplit = str2.split("x");
            this.j = Integer.parseInt(strArrSplit[0]);
            this.i = Integer.parseInt(strArrSplit[1]);
        }
        this.o = str;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00ed A[EDGE_INSN: B:49:0x00ed->B:40:0x00ed BREAK  A[LOOP:0: B:33:0x00b3->B:39:0x00e7], SYNTHETIC] */
    @Override // defpackage.N71
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean c() {
        View viewFindViewById;
        int length;
        int i;
        HashMap map;
        C2267g61 c2267g61;
        Class cls;
        C2407h61 c2407h61 = this.p;
        c2407h61.getClass();
        Activity activity = Me1.e;
        Class cls2 = c2407h61.i;
        T91 t91 = c2407h61.j;
        if (activity != null) {
            viewFindViewById = activity.findViewById(R.id.content);
            if (cls2 != null) {
                if (!cls2.isInstance(viewFindViewById)) {
                    if (viewFindViewById instanceof FrameLayout) {
                        FrameLayout frameLayout = (FrameLayout) viewFindViewById;
                        View rootView = frameLayout.getRootView();
                        if (cls2.isInstance(rootView)) {
                            viewFindViewById = rootView;
                        } else {
                            viewFindViewById = frameLayout.getChildAt(0);
                            if (viewFindViewById == null || !cls2.isInstance(viewFindViewById)) {
                            }
                        }
                    }
                }
            }
            if (viewFindViewById != null || (cls = c2407h61.h) == null) {
                AbstractC4294uc1.b();
                View[] viewArrA = AbstractC4294uc1.a();
                length = viewArrA.length;
                i = 0;
                while (true) {
                    map = c2407h61.b;
                    if (i < length) {
                        break;
                    }
                    View view = viewArrA[i];
                    int iA = Tb1.a(view);
                    if (map.containsKey(Integer.valueOf(iA))) {
                        c2267g61 = (C2267g61) map.get(Integer.valueOf(iA));
                    } else {
                        c2267g61 = new C2267g61();
                        c2267g61.b = new ArrayList(2);
                        map.put(Integer.valueOf(iA), c2267g61);
                    }
                    c2407h61.a(view, null, c2267g61);
                    i++;
                }
                c2407h61.d = true;
                if (c2407h61.e == 0) {
                    DomSender domSender = c2407h61.f;
                    if (domSender != null) {
                        domSender.onGetCircleInfoFinish(map);
                    }
                    c2407h61.d = false;
                }
            } else {
                try {
                    Object objInvoke = cls.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
                    Tb1.a(viewFindViewById);
                    cls.getMethod("loadPageInfoFromJS", cls2, Object.class, Long.class).invoke(objInvoke, viewFindViewById, new C4985zZ0(8), 500L);
                    return true;
                } catch (Exception e) {
                    B70 b70 = t91.q;
                    List listSingletonList = Collections.singletonList("PickerApi");
                    StringBuilder sbE = AbstractC2097ev0.e("load reactNative pageInfo failed: ");
                    sbE.append(e.getMessage());
                    b70.g(listSingletonList, sbE.toString(), e, new Object[0]);
                }
            }
            return true;
        }
        t91.q.j(0, Collections.singletonList("CircleHelper"), "could not get current activity in getForegroundActivity", new Object[0]);
        viewFindViewById = null;
        if (viewFindViewById != null) {
            AbstractC4294uc1.b();
            View[] viewArrA2 = AbstractC4294uc1.a();
            length = viewArrA2.length;
            i = 0;
            while (true) {
                map = c2407h61.b;
                if (i < length) {
                }
                c2407h61.a(view, null, c2267g61);
                i++;
            }
            c2407h61.d = true;
            if (c2407h61.e == 0) {
            }
        }
        return true;
    }

    @Override // defpackage.N71
    public String d() {
        return "d";
    }

    @Override // defpackage.N71
    public long[] e() {
        return q;
    }

    @Override // defpackage.N71
    public boolean g() {
        return true;
    }

    @Override // defpackage.N71
    public long h() {
        return 1000L;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        JSONObject jSONObjectOptJSONObject;
        if (message.what != 1) {
            Toast.makeText(this.k, (String) message.obj, 0).show();
            return true;
        }
        JSONObject jSONObjectR = this.m.R((String) this.f.g.b, this.l, this.n, this.o, (LinkedList) message.obj);
        if (jSONObjectR != null && (jSONObjectOptJSONObject = jSONObjectR.optJSONObject("data")) != null && !jSONObjectOptJSONObject.optBoolean("keep", true)) {
            String strOptString = jSONObjectR.optString("message");
            Message messageObtainMessage = this.g.obtainMessage();
            messageObtainMessage.obj = strOptString;
            this.g.sendMessage(messageObtainMessage);
            setStop(true);
        }
        return true;
    }

    public void onGetCircleInfoFinish(int i, JSONArray jSONArray) {
        JSONObject jSONObjectOptJSONObject;
        if (jSONArray == null || jSONArray.length() < 1) {
            return;
        }
        LinkedList linkedList = new LinkedList();
        C3399oC c3399oC = new C3399oC();
        c3399oC.c = this.i;
        c3399oC.d = this.j;
        c3399oC.b = jSONArray;
        c3399oC.a = AbstractC1993e81.a(i);
        linkedList.add(c3399oC);
        JSONObject jSONObjectR = this.m.R((String) this.f.g.b, this.l, this.n, this.o, linkedList);
        if (jSONObjectR == null || (jSONObjectOptJSONObject = jSONObjectR.optJSONObject("data")) == null || jSONObjectOptJSONObject.optBoolean("keep", true)) {
            return;
        }
        String strOptString = jSONObjectR.optString("message");
        Message messageObtainMessage = this.g.obtainMessage();
        messageObtainMessage.obj = strOptString;
        this.g.sendMessage(messageObtainMessage);
        setStop(true);
    }

    public void onGetCircleInfoFinish(Map<Integer, C2267g61> map) {
        C3399oC c3399oC;
        JSONArray jSONArray;
        if (map == null) {
            return;
        }
        LinkedList linkedList = new LinkedList();
        C3399oC c3399oC2 = new C3399oC();
        c3399oC2.d = this.j;
        c3399oC2.c = this.i;
        linkedList.add(c3399oC2);
        for (Integer num : map.keySet()) {
            C2267g61 c2267g61 = map.get(num);
            if (c2267g61 != null && c2267g61.a != null) {
                if (Tb1.e(this.f.j, num.intValue())) {
                    c3399oC = (C3399oC) linkedList.getFirst();
                } else {
                    c3399oC = new C3399oC();
                    try {
                        DisplayManager displayManager = (DisplayManager) this.k.getSystemService("display");
                        c3399oC.d = displayManager.getDisplay(num.intValue()).getHeight();
                        c3399oC.c = displayManager.getDisplay(num.intValue()).getWidth();
                    } catch (Throwable th) {
                        this.f.q.g(Collections.singletonList("DomSender"), "Get display pixels failed", th, new Object[0]);
                    }
                    linkedList.add(c3399oC);
                }
                Ue1 ue1 = c2267g61.a;
                ArrayList<Aa1> arrayList = new ArrayList(c2267g61.b);
                c2267g61.b.clear();
                List list = AbstractC1993e81.a;
                try {
                    jSONArray = new JSONArray();
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("pageKey", ue1.D);
                    jSONObject.put("is_html", false);
                    jSONObject.put("frame", ue1.u());
                    JSONArray jSONArray2 = new JSONArray();
                    jSONArray2.put(AbstractC1993e81.c(ue1));
                    jSONObject.put("dom", jSONArray2);
                    jSONArray.put(jSONObject);
                    for (Aa1 aa1 : arrayList) {
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("pageKey", aa1.a);
                        jSONObject2.put("is_html", true);
                        jSONObject2.put("frame", aa1.c.a());
                        jSONObject2.put("element_path", aa1.d);
                        ArrayList arrayList2 = aa1.b;
                        JSONArray jSONArray3 = new JSONArray();
                        Iterator it = arrayList2.iterator();
                        while (it.hasNext()) {
                            jSONArray3.put(AbstractC1993e81.b((C4848ya1) it.next()));
                        }
                        jSONObject2.put("dom", jSONArray3);
                        jSONArray.put(jSONObject2);
                    }
                } catch (Throwable th2) {
                    ((B70) B70.h()).g(AbstractC1993e81.a, "getDomPagerArray failed", th2, new Object[0]);
                    jSONArray = null;
                }
                c3399oC.b = jSONArray;
                c3399oC.a = AbstractC1993e81.a(num.intValue());
            }
        }
        this.h.obtainMessage(1, linkedList).sendToTarget();
    }
}
