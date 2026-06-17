package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import javax.mail.Flags;

/* JADX INFO: loaded from: classes.dex */
public class FLAGS extends Flags implements Item {
    static final char[] name = {'F', 'L', 'A', 'G', 'S'};
    private static final long serialVersionUID = 439049847053756670L;
    public int msgno;

    public FLAGS(IMAPResponse iMAPResponse) throws ParsingException {
        this.msgno = iMAPResponse.getNumber();
        iMAPResponse.skipSpaces();
        String[] simpleList = iMAPResponse.readSimpleList();
        if (simpleList != null) {
            for (String str : simpleList) {
                if (str.length() < 2 || str.charAt(0) != '\\') {
                    add(str);
                } else {
                    char upperCase = Character.toUpperCase(str.charAt(1));
                    if (upperCase == '*') {
                        add(Flags.Flag.USER);
                    } else if (upperCase == 'A') {
                        add(Flags.Flag.ANSWERED);
                    } else if (upperCase != 'D') {
                        if (upperCase == 'F') {
                            add(Flags.Flag.FLAGGED);
                        } else if (upperCase == 'R') {
                            add(Flags.Flag.RECENT);
                        } else if (upperCase != 'S') {
                            add(str);
                        } else {
                            add(Flags.Flag.SEEN);
                        }
                    } else if (str.length() >= 3) {
                        char cCharAt = str.charAt(2);
                        if (cCharAt == 'e' || cCharAt == 'E') {
                            add(Flags.Flag.DELETED);
                        } else if (cCharAt == 'r' || cCharAt == 'R') {
                            add(Flags.Flag.DRAFT);
                        }
                    } else {
                        add(str);
                    }
                }
            }
        }
    }
}
