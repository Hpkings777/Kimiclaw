package com.sun.mail.smtp;

import com.sun.mail.util.DefaultProvider;
import javax.mail.Provider;

/* JADX INFO: loaded from: classes.dex */
@DefaultProvider
public class SMTPProvider extends Provider {
    public SMTPProvider() {
        super(Provider.Type.TRANSPORT, "smtp", SMTPTransport.class.getName(), "Oracle", null);
    }
}
