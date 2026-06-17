package com.sun.mail.imap.protocol;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MessageSet {
    public int end;
    public int start;

    public MessageSet() {
    }

    public static MessageSet[] createMessageSets(int[] iArr) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < iArr.length) {
            MessageSet messageSet = new MessageSet();
            messageSet.start = iArr[i];
            do {
                i++;
                if (i < iArr.length) {
                }
                messageSet.end = iArr[i - 1];
                arrayList.add(messageSet);
            } while (iArr[i] == iArr[i - 1] + 1);
            messageSet.end = iArr[i - 1];
            arrayList.add(messageSet);
        }
        return (MessageSet[]) arrayList.toArray(new MessageSet[arrayList.size()]);
    }

    public static String toString(MessageSet[] messageSetArr) {
        if (messageSetArr == null || messageSetArr.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int length = messageSetArr.length;
        int i = 0;
        while (true) {
            MessageSet messageSet = messageSetArr[i];
            int i2 = messageSet.start;
            int i3 = messageSet.end;
            if (i3 > i2) {
                sb.append(i2);
                sb.append(':');
                sb.append(i3);
            } else {
                sb.append(i2);
            }
            i++;
            if (i >= length) {
                return sb.toString();
            }
            sb.append(',');
        }
    }

    public int size() {
        return (this.end - this.start) + 1;
    }

    public MessageSet(int i, int i2) {
        this.start = i;
        this.end = i2;
    }

    public static int size(MessageSet[] messageSetArr) {
        if (messageSetArr == null) {
            return 0;
        }
        int size = 0;
        for (MessageSet messageSet : messageSetArr) {
            size += messageSet.size();
        }
        return size;
    }
}
