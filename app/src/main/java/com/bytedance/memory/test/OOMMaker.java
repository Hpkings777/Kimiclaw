package com.bytedance.memory.test;

import defpackage.AbstractC0173Cu0;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class OOMMaker {
    public static ArrayList<byte[]> a = new ArrayList<>();

    public static void createOOM() {
        while (true) {
            a.add(new byte[2097152]);
        }
    }

    public static void createReachTop(int i) {
        while (AbstractC0173Cu0.b() < i) {
            encreaseMem();
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void encreaseMem() {
        a.add(new byte[15728640]);
    }
}
