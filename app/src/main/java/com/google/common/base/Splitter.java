package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import kotlin.jvm.internal.IntCompanionObject;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class Splitter {
    private final int limit;
    private final boolean omitEmptyStrings;
    private final Strategy strategy;
    private final CharMatcher trimmer;

    public static final class MapSplitter {
        private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
        private final Splitter entrySplitter;
        private final Splitter outerSplitter;

        public Map<String, String> split(CharSequence charSequence) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (String str : this.outerSplitter.split(charSequence)) {
                Iterator itSplittingIterator = this.entrySplitter.splittingIterator(str);
                Preconditions.checkArgument(itSplittingIterator.hasNext(), INVALID_ENTRY_MESSAGE, str);
                String str2 = (String) itSplittingIterator.next();
                Preconditions.checkArgument(!linkedHashMap.containsKey(str2), "Duplicate key [%s] found.", str2);
                Preconditions.checkArgument(itSplittingIterator.hasNext(), INVALID_ENTRY_MESSAGE, str);
                linkedHashMap.put(str2, (String) itSplittingIterator.next());
                Preconditions.checkArgument(!itSplittingIterator.hasNext(), INVALID_ENTRY_MESSAGE, str);
            }
            return Collections.unmodifiableMap(linkedHashMap);
        }

        private MapSplitter(Splitter splitter, Splitter splitter2) {
            this.outerSplitter = splitter;
            this.entrySplitter = (Splitter) Preconditions.checkNotNull(splitter2);
        }
    }

    public static abstract class SplittingIterator extends AbstractIterator<String> {
        int limit;
        int offset = 0;
        final boolean omitEmptyStrings;
        final CharSequence toSplit;
        final CharMatcher trimmer;

        public SplittingIterator(Splitter splitter, CharSequence charSequence) {
            this.trimmer = splitter.trimmer;
            this.omitEmptyStrings = splitter.omitEmptyStrings;
            this.limit = splitter.limit;
            this.toSplit = charSequence;
        }

        public abstract int separatorEnd(int i);

        public abstract int separatorStart(int i);

        @Override // com.google.common.base.AbstractIterator
        public String computeNext() {
            int iSeparatorStart;
            int i = this.offset;
            while (true) {
                int i2 = this.offset;
                if (i2 == -1) {
                    return endOfData();
                }
                iSeparatorStart = separatorStart(i2);
                if (iSeparatorStart == -1) {
                    iSeparatorStart = this.toSplit.length();
                    this.offset = -1;
                } else {
                    this.offset = separatorEnd(iSeparatorStart);
                }
                int i3 = this.offset;
                if (i3 == i) {
                    int i4 = i3 + 1;
                    this.offset = i4;
                    if (i4 > this.toSplit.length()) {
                        this.offset = -1;
                    }
                } else {
                    while (i < iSeparatorStart && this.trimmer.matches(this.toSplit.charAt(i))) {
                        i++;
                    }
                    while (iSeparatorStart > i && this.trimmer.matches(this.toSplit.charAt(iSeparatorStart - 1))) {
                        iSeparatorStart--;
                    }
                    if (!this.omitEmptyStrings || i != iSeparatorStart) {
                        break;
                    }
                    i = this.offset;
                }
            }
            int i5 = this.limit;
            if (i5 == 1) {
                iSeparatorStart = this.toSplit.length();
                this.offset = -1;
                while (iSeparatorStart > i && this.trimmer.matches(this.toSplit.charAt(iSeparatorStart - 1))) {
                    iSeparatorStart--;
                }
            } else {
                this.limit = i5 - 1;
            }
            return this.toSplit.subSequence(i, iSeparatorStart).toString();
        }
    }

    public interface Strategy {
        Iterator<String> iterator(Splitter splitter, CharSequence charSequence);
    }

    private Splitter(Strategy strategy) {
        this(strategy, false, CharMatcher.none(), IntCompanionObject.MAX_VALUE);
    }

    public static Splitter fixedLength(final int i) {
        Preconditions.checkArgument(i > 0, "The length may not be less than 1");
        return new Splitter(new Strategy() { // from class: com.google.common.base.a
            @Override // com.google.common.base.Splitter.Strategy
            public final Iterator iterator(Splitter splitter, CharSequence charSequence) {
                return Splitter.lambda$fixedLength$0(i, splitter, charSequence);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Iterator lambda$fixedLength$0(final int i, Splitter splitter, CharSequence charSequence) {
        return new SplittingIterator(splitter, charSequence) { // from class: com.google.common.base.Splitter.4
            @Override // com.google.common.base.Splitter.SplittingIterator
            public int separatorEnd(int i2) {
                return i2;
            }

            @Override // com.google.common.base.Splitter.SplittingIterator
            public int separatorStart(int i2) {
                int i3 = i2 + i;
                if (i3 < this.toSplit.length()) {
                    return i3;
                }
                return -1;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Iterator lambda$on$0(final CharMatcher charMatcher, Splitter splitter, CharSequence charSequence) {
        return new SplittingIterator(splitter, charSequence) { // from class: com.google.common.base.Splitter.1
            @Override // com.google.common.base.Splitter.SplittingIterator
            public int separatorEnd(int i) {
                return i + 1;
            }

            @Override // com.google.common.base.Splitter.SplittingIterator
            public int separatorStart(int i) {
                return charMatcher.indexIn(this.toSplit, i);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Iterator lambda$on$1(final String str, Splitter splitter, CharSequence charSequence) {
        return new SplittingIterator(splitter, charSequence) { // from class: com.google.common.base.Splitter.2
            @Override // com.google.common.base.Splitter.SplittingIterator
            public int separatorEnd(int i) {
                return str.length() + i;
            }

            /* JADX WARN: Code restructure failed: missing block: B:8:0x0022, code lost:
            
                r6 = r6 + 1;
             */
            @Override // com.google.common.base.Splitter.SplittingIterator
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public int separatorStart(int i) {
                int length = str.length();
                int length2 = this.toSplit.length() - length;
                while (i <= length2) {
                    for (int i2 = 0; i2 < length; i2++) {
                        if (this.toSplit.charAt(i2 + i) != str.charAt(i2)) {
                            break;
                        }
                    }
                    return i;
                }
                return -1;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Iterator lambda$onPatternInternal$0(CommonPattern commonPattern, Splitter splitter, CharSequence charSequence) {
        final CommonMatcher commonMatcherMatcher = commonPattern.matcher(charSequence);
        return new SplittingIterator(splitter, charSequence) { // from class: com.google.common.base.Splitter.3
            @Override // com.google.common.base.Splitter.SplittingIterator
            public int separatorEnd(int i) {
                return commonMatcherMatcher.end();
            }

            @Override // com.google.common.base.Splitter.SplittingIterator
            public int separatorStart(int i) {
                if (commonMatcherMatcher.find(i)) {
                    return commonMatcherMatcher.start();
                }
                return -1;
            }
        };
    }

    public static Splitter on(char c) {
        return on(CharMatcher.is(c));
    }

    @GwtIncompatible
    public static Splitter onPattern(String str) {
        return onPatternInternal(Platform.compilePattern(str));
    }

    public static Splitter onPatternInternal(CommonPattern commonPattern) {
        Preconditions.checkArgument(!commonPattern.matcher("").matches(), "The pattern may not match the empty string: %s", commonPattern);
        return new Splitter(new b(commonPattern, 2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Iterator<String> splittingIterator(CharSequence charSequence) {
        return this.strategy.iterator(this, charSequence);
    }

    public Splitter limit(int i) {
        Preconditions.checkArgument(i > 0, "must be greater than zero: %s", i);
        return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, i);
    }

    public Splitter omitEmptyStrings() {
        return new Splitter(this.strategy, true, this.trimmer, this.limit);
    }

    public Iterable<String> split(final CharSequence charSequence) {
        Preconditions.checkNotNull(charSequence);
        return new Iterable<String>(this) { // from class: com.google.common.base.Splitter.5
            final /* synthetic */ Splitter this$0;

            {
                this.this$0 = this;
            }

            @Override // java.lang.Iterable
            public Iterator<String> iterator() {
                return this.this$0.splittingIterator(charSequence);
            }

            public String toString() {
                Joiner joinerOn = Joiner.on(", ");
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                StringBuilder sbAppendTo = joinerOn.appendTo(sb, (Iterable<?>) this);
                sbAppendTo.append(']');
                return sbAppendTo.toString();
            }
        };
    }

    public List<String> splitToList(CharSequence charSequence) {
        Preconditions.checkNotNull(charSequence);
        Iterator<String> itSplittingIterator = splittingIterator(charSequence);
        ArrayList arrayList = new ArrayList();
        while (itSplittingIterator.hasNext()) {
            arrayList.add(itSplittingIterator.next());
        }
        return Collections.unmodifiableList(arrayList);
    }

    @IgnoreJRERequirement
    public Stream<String> splitToStream(CharSequence charSequence) {
        return StreamSupport.stream(split(charSequence).spliterator(), false);
    }

    public Splitter trimResults() {
        return trimResults(CharMatcher.whitespace());
    }

    public MapSplitter withKeyValueSeparator(String str) {
        return withKeyValueSeparator(on(str));
    }

    private Splitter(Strategy strategy, boolean z, CharMatcher charMatcher, int i) {
        this.strategy = strategy;
        this.omitEmptyStrings = z;
        this.trimmer = charMatcher;
        this.limit = i;
    }

    public static Splitter on(CharMatcher charMatcher) {
        Preconditions.checkNotNull(charMatcher);
        return new Splitter(new b(charMatcher, 1));
    }

    public Splitter trimResults(CharMatcher charMatcher) {
        Preconditions.checkNotNull(charMatcher);
        return new Splitter(this.strategy, this.omitEmptyStrings, charMatcher, this.limit);
    }

    public MapSplitter withKeyValueSeparator(char c) {
        return withKeyValueSeparator(on(c));
    }

    public MapSplitter withKeyValueSeparator(Splitter splitter) {
        return new MapSplitter(splitter);
    }

    public static Splitter on(String str) {
        Preconditions.checkArgument(str.length() != 0, "The separator may not be the empty string.");
        if (str.length() == 1) {
            return on(str.charAt(0));
        }
        return new Splitter(new b(str, 0));
    }

    @GwtIncompatible
    public static Splitter on(Pattern pattern) {
        return onPatternInternal(new JdkPattern(pattern));
    }
}
