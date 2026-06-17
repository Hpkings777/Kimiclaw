package com.sun.mail.iap;

/* JADX INFO: loaded from: classes.dex */
public class BadCommandException extends ProtocolException {
    private static final long serialVersionUID = 5769722539397237515L;

    public BadCommandException() {
    }

    public BadCommandException(String str) {
        super(str);
    }

    public BadCommandException(Response response) {
        super(response);
    }
}
