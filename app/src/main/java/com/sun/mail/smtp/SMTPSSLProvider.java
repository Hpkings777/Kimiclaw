package com.sun.mail.smtp;

import com.sun.mail.util.DefaultProvider;
import javax.mail.Provider;

/* JADX INFO: loaded from: classes.dex */
@DefaultProvider
public class SMTPSSLProvider extends Provider {
    public SMTPSSLProvider() {
        super(Provider.Type.TRANSPORT, "smtps", SMTPSSLTransport.class.getName(), "Oracle", null);
    }
}
