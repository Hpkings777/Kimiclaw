package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.nio.file.FileSystemException;

/* JADX INFO: loaded from: classes.dex */
@IgnoreJRERequirement
@J2ktIncompatible
@GwtIncompatible
public final class InsecureRecursiveDeleteException extends FileSystemException {
    public InsecureRecursiveDeleteException(String str) {
        super(str, null, "unable to guarantee security of recursive delete");
    }
}
