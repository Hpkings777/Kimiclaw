package com.sun.mail.iap;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionException extends ProtocolException {
    private static final long serialVersionUID = 5749739604257464727L;
    private transient Protocol p;

    public ConnectionException() {
    }

    public Protocol getProtocol() {
        return this.p;
    }

    public ConnectionException(String str) {
        super(str);
    }

    public ConnectionException(Protocol protocol, Response response) {
        super(response);
        this.p = protocol;
    }
}
