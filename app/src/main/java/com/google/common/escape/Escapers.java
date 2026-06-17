package com.google.common.escape;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import kotlin.jvm.internal.CharCompanionObject;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class Escapers {
    private static final Escaper NULL_ESCAPER = new CharEscaper() { // from class: com.google.common.escape.Escapers.1
        @Override // com.google.common.escape.CharEscaper
        public char[] escape(char c) {
            return null;
        }

        @Override // com.google.common.escape.CharEscaper, com.google.common.escape.Escaper
        public String escape(String str) {
            return (String) Preconditions.checkNotNull(str);
        }
    };

    public static final class Builder {
        private final Map<Character, String> replacementMap;
        private char safeMax;
        private char safeMin;
        private String unsafeReplacement;

        public Builder addEscape(char c, String str) {
            Preconditions.checkNotNull(str);
            this.replacementMap.put(Character.valueOf(c), str);
            return this;
        }

        public Escaper build() {
            return new ArrayBasedCharEscaper(this.replacementMap, this.safeMin, this.safeMax) { // from class: com.google.common.escape.Escapers.Builder.1
                private final char[] replacementChars;

                {
                    this.replacementChars = Builder.this.unsafeReplacement != null ? Builder.this.unsafeReplacement.toCharArray() : null;
                }

                @Override // com.google.common.escape.ArrayBasedCharEscaper
                public char[] escapeUnsafe(char c) {
                    return this.replacementChars;
                }
            };
        }

        public Builder setSafeRange(char c, char c2) {
            this.safeMin = c;
            this.safeMax = c2;
            return this;
        }

        public Builder setUnsafeReplacement(String str) {
            this.unsafeReplacement = str;
            return this;
        }

        private Builder() {
            this.replacementMap = new HashMap();
            this.safeMin = (char) 0;
            this.safeMax = CharCompanionObject.MAX_VALUE;
            this.unsafeReplacement = null;
        }
    }

    private Escapers() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static String computeReplacement(CharEscaper charEscaper, char c) {
        return stringOrNull(charEscaper.escape(c));
    }

    public static Escaper nullEscaper() {
        return NULL_ESCAPER;
    }

    private static String stringOrNull(char[] cArr) {
        if (cArr == null) {
            return null;
        }
        return new String(cArr);
    }

    public static String computeReplacement(UnicodeEscaper unicodeEscaper, int i) {
        return stringOrNull(unicodeEscaper.escape(i));
    }
}
