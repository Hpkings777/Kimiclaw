package com.sun.mail.smtp;

import javax.mail.Session;
import javax.mail.URLName;

/* JADX INFO: loaded from: classes.dex */
public class SMTPSSLTransport extends SMTPTransport {
    public SMTPSSLTransport(Session session, URLName uRLName) {
        super(session, uRLName, "smtps", true);
    }
}
