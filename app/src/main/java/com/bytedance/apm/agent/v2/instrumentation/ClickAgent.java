package com.bytedance.apm.agent.v2.instrumentation;

import android.content.res.Resources;
import android.support.annotation.Keep;
import android.view.View;
import android.widget.TextView;
import defpackage.AbstractC4017sd1;
import defpackage.C4784y61;
import defpackage.RunnableC4201tz;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@Keep
public class ClickAgent {
    private static final String ACTION_NAME = "view_click";
    private static final String CLICK_TYPE = "click_type";
    private static final String VIEW_ID = "view_id";
    private static final String VIEW_NAME = "view_name";
    private static final String VIEW_TEXT = "view_text";
    private static final int VIEW_TEXT_LENGTH_LIMIT = 100;

    @Keep
    public static void onClick(View view) {
        if (view == null) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            Resources resources = view.getContext().getResources();
            if (view.getId() != -1) {
                jSONObject.put(VIEW_ID, view.getId());
                jSONObject.put(VIEW_NAME, resources.getResourceEntryName(view.getId()));
            }
            if (view instanceof TextView) {
                CharSequence text = ((TextView) view).getText();
                if (text.length() > VIEW_TEXT_LENGTH_LIMIT) {
                    text = text.subSequence(0, VIEW_TEXT_LENGTH_LIMIT);
                }
                jSONObject.put(VIEW_TEXT, text);
            }
            if (view.getParent() != null) {
                String simpleName = view.getParent().getClass().getSimpleName();
                if (view.getParent().getParent() != null) {
                    simpleName = view.getParent().getParent().getClass().getSimpleName() + "#" + simpleName + "#" + view.getClass().getSimpleName();
                }
                jSONObject.put("view_path", simpleName);
            }
            jSONObject.put(CLICK_TYPE, "View#OnClick");
            C4784y61.i.b(new RunnableC4201tz(ACTION_NAME, "", jSONObject, 4));
        } catch (Exception e) {
            if (AbstractC4017sd1.b) {
                e.printStackTrace();
            }
        }
    }

    @Keep
    public static void onTabChanged(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(VIEW_NAME, str);
            jSONObject.put(CLICK_TYPE, "TabHost#OnTabChanged");
            C4784y61.i.b(new RunnableC4201tz(ACTION_NAME, "", jSONObject, 4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
