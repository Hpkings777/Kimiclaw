package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import org.apache.commons.io.FilenameUtils;

/* JADX INFO: loaded from: classes.dex */
public class RFC822SIZE implements Item {
    static final char[] name = {'R', 'F', 'C', '8', '2', '2', FilenameUtils.EXTENSION_SEPARATOR, 'S', 'I', 'Z', 'E'};
    public int msgno;
    public long size;

    public RFC822SIZE(FetchResponse fetchResponse) throws ParsingException {
        this.msgno = fetchResponse.getNumber();
        fetchResponse.skipSpaces();
        this.size = fetchResponse.readLong();
    }
}
