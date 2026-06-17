package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class Platform {
    private static final PatternCompiler patternCompiler = loadPatternCompiler();

    public static final class JdkPatternCompiler implements PatternCompiler {
        private JdkPatternCompiler() {
        }

        @Override // com.google.common.base.PatternCompiler
        public CommonPattern compile(String str) {
            return new JdkPattern(Pattern.compile(str));
        }

        @Override // com.google.common.base.PatternCompiler
        public boolean isPcreLike() {
            return true;
        }
    }

    private Platform() {
    }

    public static CommonPattern compilePattern(String str) {
        Preconditions.checkNotNull(str);
        return patternCompiler.compile(str);
    }

    public static String emptyToNull(String str) {
        if (stringIsNullOrEmpty(str)) {
            return null;
        }
        return str;
    }

    public static String formatCompact4Digits(double d) {
        return String.format(Locale.ROOT, "%.4g", Double.valueOf(d));
    }

    public static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> cls, String str) {
        WeakReference<? extends Enum<?>> weakReference = Enums.getEnumConstants(cls).get(str);
        return weakReference == null ? Optional.absent() : Optional.fromNullable(cls.cast(weakReference.get()));
    }

    public static String lenientFormat(String str, Object... objArr) {
        return Strings.lenientFormat(str, objArr);
    }

    private static PatternCompiler loadPatternCompiler() {
        return new JdkPatternCompiler();
    }

    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    public static boolean patternCompilerIsPcreLike() {
        return patternCompiler.isPcreLike();
    }

    public static CharMatcher precomputeCharMatcher(CharMatcher charMatcher) {
        return charMatcher.precomputedInternal();
    }

    public static boolean stringIsNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String stringValueOf(Object obj) {
        return String.valueOf(obj);
    }
}
