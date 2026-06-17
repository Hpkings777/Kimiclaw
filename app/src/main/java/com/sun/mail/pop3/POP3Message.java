package com.sun.mail.pop3;

import com.sun.mail.util.ReadableMime;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.Enumeration;
import java.util.logging.Level;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Header;
import javax.mail.IllegalWriteException;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.SharedInputStream;

/* JADX INFO: loaded from: classes.dex */
public class POP3Message extends MimeMessage implements ReadableMime {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final String UNKNOWN = "UNKNOWN";
    private POP3Folder folder;
    private int hdrSize;
    private int msgSize;
    private SoftReference<InputStream> rawData;
    String uid;

    public POP3Message(Folder folder, int i) throws MessagingException {
        super(folder, i);
        this.hdrSize = -1;
        this.msgSize = -1;
        this.uid = UNKNOWN;
        this.rawData = new SoftReference<>(null);
        this.folder = (POP3Folder) folder;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private InputStream getRawStream(boolean z) throws MessagingException {
        Object obj;
        InputStream inputStreamRetr;
        int i;
        try {
            synchronized (this) {
                try {
                    Object obj2 = (InputStream) this.rawData.get();
                    obj = obj2;
                    if (obj2 == null) {
                        TempFile fileCache = this.folder.getFileCache();
                        if (fileCache != null) {
                            if (this.folder.logger.isLoggable(Level.FINE)) {
                                this.folder.logger.fine("caching message #" + this.msgnum + " in temp file");
                            }
                            AppendStream appendStream = fileCache.getAppendStream();
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(appendStream);
                            try {
                                this.folder.getProtocol().retr(this.msgnum, bufferedOutputStream);
                                bufferedOutputStream.close();
                                inputStreamRetr = appendStream.getInputStream();
                            } catch (Throwable th) {
                                bufferedOutputStream.close();
                                throw th;
                            }
                        } else {
                            Protocol protocol = this.folder.getProtocol();
                            int i2 = this.msgnum;
                            int i3 = this.msgSize;
                            inputStreamRetr = protocol.retr(i2, i3 > 0 ? i3 + this.hdrSize : 0);
                        }
                        InputStream inputStream = inputStreamRetr;
                        if (inputStream == 0) {
                            this.expunged = true;
                            throw new MessageRemovedException("can't retrieve message #" + this.msgnum + " in POP3Message.getContentStream");
                        }
                        if (this.headers == null || ((POP3Store) this.folder.getStore()).forgetTopHeaders) {
                            this.headers = new InternetHeaders(inputStream);
                            this.hdrSize = (int) ((SharedInputStream) inputStream).getPosition();
                        } else {
                            do {
                                i = 0;
                                while (true) {
                                    int i4 = inputStream.read();
                                    if (i4 < 0 || i4 == 10) {
                                        break;
                                    }
                                    if (i4 != 13) {
                                        i++;
                                    } else if (inputStream.available() > 0) {
                                        inputStream.mark(1);
                                        if (inputStream.read() != 10) {
                                            inputStream.reset();
                                        }
                                    }
                                }
                                if (inputStream.available() == 0) {
                                    break;
                                }
                            } while (i != 0);
                            this.hdrSize = (int) ((SharedInputStream) inputStream).getPosition();
                        }
                        this.msgSize = inputStream.available();
                        this.rawData = new SoftReference<>(inputStream);
                        obj = inputStream;
                    }
                } finally {
                }
            }
            return ((SharedInputStream) obj).newStream(z ? this.hdrSize : 0L, -1L);
        } catch (EOFException e) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, e.toString());
        } catch (IOException e2) {
            throw new MessagingException("error fetching POP3 content", e2);
        }
    }

    private void loadHeaders() throws MessagingException {
        boolean z;
        InputStream pVar;
        try {
            synchronized (this) {
                try {
                    if (this.headers != null) {
                        return;
                    }
                    if (((POP3Store) this.folder.getStore()).disableTop || (pVar = this.folder.getProtocol().top(this.msgnum, 0)) == null) {
                        z = true;
                    } else {
                        try {
                            this.hdrSize = pVar.available();
                            this.headers = new InternetHeaders(pVar);
                            pVar.close();
                            z = false;
                        } catch (Throwable th) {
                            pVar.close();
                            throw th;
                        }
                    }
                    if (z) {
                        InputStream contentStream = getContentStream();
                        if (contentStream != null) {
                            contentStream.close();
                        }
                    }
                } finally {
                }
            }
        } catch (EOFException e) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, e.toString());
        } catch (IOException e2) {
            throw new MessagingException("error loading POP3 headers", e2);
        }
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.Part
    public void addHeader(String str, String str2) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.internet.MimePart
    public void addHeaderLine(String str) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.internet.MimePart
    public Enumeration<String> getAllHeaderLines() throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getAllHeaderLines();
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.Part
    public Enumeration<Header> getAllHeaders() throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getAllHeaders();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.mail.internet.MimeMessage
    public synchronized InputStream getContentStream() throws MessagingException {
        Closeable closeable = this.contentStream;
        if (closeable != null) {
            return ((SharedInputStream) closeable).newStream(0L, -1L);
        }
        InputStream rawStream = getRawStream(true);
        if (this.folder.getFileCache() != null || ((POP3Store) this.folder.getStore()).keepMessageContent) {
            this.contentStream = ((SharedInputStream) rawStream).newStream(0L, -1L);
        }
        return rawStream;
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.Part
    public String[] getHeader(String str) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getHeader(str);
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.internet.MimePart
    public Enumeration<String> getMatchingHeaderLines(String[] strArr) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getMatchingHeaderLines(strArr);
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.Part
    public Enumeration<Header> getMatchingHeaders(String[] strArr) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getMatchingHeaders(strArr);
    }

    @Override // com.sun.mail.util.ReadableMime
    public InputStream getMimeStream() throws MessagingException {
        return getRawStream(false);
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.internet.MimePart
    public Enumeration<String> getNonMatchingHeaderLines(String[] strArr) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getNonMatchingHeaderLines(strArr);
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.Part
    public Enumeration<Header> getNonMatchingHeaders(String[] strArr) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getNonMatchingHeaders(strArr);
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.Part
    public int getSize() throws MessagingException {
        int i;
        try {
            synchronized (this) {
                try {
                    int i2 = this.msgSize;
                    if (i2 > 0) {
                        return i2;
                    }
                    if (this.headers == null) {
                        loadHeaders();
                    }
                    synchronized (this) {
                        try {
                            if (this.msgSize < 0) {
                                this.msgSize = this.folder.getProtocol().list(this.msgnum) - this.hdrSize;
                            }
                            i = this.msgSize;
                        } finally {
                        }
                    }
                    return i;
                } finally {
                }
            }
        } catch (EOFException e) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, e.toString());
        } catch (IOException e2) {
            throw new MessagingException("error getting size", e2);
        }
    }

    public synchronized void invalidate(boolean z) {
        this.content = null;
        InputStream inputStream = this.rawData.get();
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException unused) {
            }
            this.rawData = new SoftReference<>(null);
        }
        InputStream inputStream2 = this.contentStream;
        if (inputStream2 != null) {
            try {
                inputStream2.close();
            } catch (IOException unused2) {
            }
            this.contentStream = null;
        }
        this.msgSize = -1;
        if (z) {
            this.headers = null;
            this.hdrSize = -1;
        }
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.Part
    public void removeHeader(String str) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.Message
    public void saveChanges() throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.Message
    public synchronized void setFlags(Flags flags, boolean z) throws MessagingException {
        Flags flags2 = (Flags) this.flags.clone();
        super.setFlags(flags, z);
        if (!this.flags.equals(flags2)) {
            this.folder.notifyMessageChangedListeners(1, this);
        }
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.Part
    public void setHeader(String str, String str2) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    public InputStream top(int i) throws MessagingException {
        InputStream pVar;
        try {
            synchronized (this) {
                pVar = this.folder.getProtocol().top(this.msgnum, i);
            }
            return pVar;
        } catch (EOFException e) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, e.toString());
        } catch (IOException e2) {
            throw new MessagingException("error getting size", e2);
        }
    }

    @Override // javax.mail.internet.MimeMessage
    public synchronized void writeTo(OutputStream outputStream, String[] strArr) throws MessagingException, IOException {
        try {
            Closeable closeable = (InputStream) this.rawData.get();
            if (closeable == null && strArr == null && !((POP3Store) this.folder.getStore()).cacheWriteTo) {
                if (this.folder.logger.isLoggable(Level.FINE)) {
                    this.folder.logger.fine("streaming msg " + this.msgnum);
                }
                if (!this.folder.getProtocol().retr(this.msgnum, outputStream)) {
                    this.expunged = true;
                    throw new MessageRemovedException("can't retrieve message #" + this.msgnum + " in POP3Message.writeTo");
                }
            } else if (closeable == null || strArr != null) {
                super.writeTo(outputStream, strArr);
            } else {
                InputStream inputStreamNewStream = ((SharedInputStream) closeable).newStream(0L, -1L);
                try {
                    byte[] bArr = new byte[16384];
                    while (true) {
                        int i = inputStreamNewStream.read(bArr);
                        if (i > 0) {
                            outputStream.write(bArr, 0, i);
                        } else {
                            try {
                                break;
                            } catch (IOException unused) {
                            }
                        }
                    }
                    inputStreamNewStream.close();
                } catch (Throwable th) {
                    if (inputStreamNewStream != null) {
                        try {
                            inputStreamNewStream.close();
                        } catch (IOException unused2) {
                        }
                    }
                    throw th;
                }
            }
        } finally {
        }
    }

    @Override // javax.mail.internet.MimeMessage, javax.mail.internet.MimePart
    public String getHeader(String str, String str2) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getHeader(str, str2);
    }
}
