package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class ArrayTable<R, C, V> extends AbstractTable<R, C, V> implements Table<R, C, V>, Serializable {

    @J2ktIncompatible
    @GwtIncompatible
    private static final long serialVersionUID = 0;
    private final V[][] array;
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableList<C> columnList;
    private transient ArrayTable<R, C, V>.ColumnMap columnMap;
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableList<R> rowList;
    private transient ArrayTable<R, C, V>.RowMap rowMap;

    public static abstract class ArrayMap<K, V> extends Maps.IteratorBasedAbstractMap<K, V> {
        private final ImmutableMap<K, Integer> keyIndex;

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            return this.keyIndex.containsKey(obj);
        }

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
        public Iterator<Map.Entry<K, V>> entryIterator() {
            return new AbstractIndexedListIterator<Map.Entry<K, V>>(size()) { // from class: com.google.common.collect.ArrayTable.ArrayMap.2
                @Override // com.google.common.collect.AbstractIndexedListIterator
                public Map.Entry<K, V> get(int i) {
                    return ArrayMap.this.getEntry(i);
                }
            };
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            Integer num = this.keyIndex.get(obj);
            if (num == null) {
                return null;
            }
            return getValue(num.intValue());
        }

        public Map.Entry<K, V> getEntry(final int i) {
            Preconditions.checkElementIndex(i, size());
            return new AbstractMapEntry<K, V>(this) { // from class: com.google.common.collect.ArrayTable.ArrayMap.1
                final /* synthetic */ ArrayMap this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                public K getKey() {
                    return (K) this.this$0.getKey(i);
                }

                @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                @ParametricNullness
                public V getValue() {
                    return (V) this.this$0.getValue(i);
                }

                @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                @ParametricNullness
                public V setValue(@ParametricNullness V v) {
                    return (V) this.this$0.setValue(i, v);
                }
            };
        }

        public K getKey(int i) {
            return this.keyIndex.keySet().asList().get(i);
        }

        public abstract String getKeyRole();

        @ParametricNullness
        public abstract V getValue(int i);

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return this.keyIndex.isEmpty();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return this.keyIndex.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V put(K k, @ParametricNullness V v) {
            Integer num = this.keyIndex.get(k);
            if (num != null) {
                return setValue(num.intValue(), v);
            }
            throw new IllegalArgumentException(getKeyRole() + " " + k + " not in " + this.keyIndex.keySet());
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @ParametricNullness
        public abstract V setValue(int i, @ParametricNullness V v);

        @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap, java.util.AbstractMap, java.util.Map
        public int size() {
            return this.keyIndex.size();
        }

        private ArrayMap(ImmutableMap<K, Integer> immutableMap) {
            this.keyIndex = immutableMap;
        }
    }

    public final class Column extends ArrayMap<R, V> {
        final int columnIndex;

        public Column(int i) {
            super(ArrayTable.this.rowKeyToIndex);
            this.columnIndex = i;
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public String getKeyRole() {
            return "Row";
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public V getValue(int i) {
            return (V) ArrayTable.this.at(i, this.columnIndex);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public V setValue(int i, V v) {
            return (V) ArrayTable.this.set(i, this.columnIndex, v);
        }
    }

    public final class ColumnMap extends ArrayMap<C, Map<R, V>> {
        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public String getKeyRole() {
            return "Column";
        }

        private ColumnMap() {
            super(ArrayTable.this.columnKeyToIndex);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public Map<R, V> getValue(int i) {
            return new Column(i);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap, java.util.AbstractMap, java.util.Map
        public Map<R, V> put(C c, Map<R, V> map) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public Map<R, V> setValue(int i, Map<R, V> map) {
            throw new UnsupportedOperationException();
        }
    }

    public final class Row extends ArrayMap<C, V> {
        final int rowIndex;

        public Row(int i) {
            super(ArrayTable.this.columnKeyToIndex);
            this.rowIndex = i;
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public String getKeyRole() {
            return "Column";
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public V getValue(int i) {
            return (V) ArrayTable.this.at(this.rowIndex, i);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public V setValue(int i, V v) {
            return (V) ArrayTable.this.set(this.rowIndex, i, v);
        }
    }

    public final class RowMap extends ArrayMap<R, Map<C, V>> {
        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public String getKeyRole() {
            return "Row";
        }

        private RowMap() {
            super(ArrayTable.this.rowKeyToIndex);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public Map<C, V> getValue(int i) {
            return new Row(i);
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap, java.util.AbstractMap, java.util.Map
        public Map<C, V> put(R r, Map<C, V> map) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ArrayTable.ArrayMap
        public Map<C, V> setValue(int i, Map<C, V> map) {
            throw new UnsupportedOperationException();
        }
    }

    private ArrayTable(Iterable<? extends R> iterable, Iterable<? extends C> iterable2) {
        ImmutableList<R> immutableListCopyOf = ImmutableList.copyOf(iterable);
        this.rowList = immutableListCopyOf;
        ImmutableList<C> immutableListCopyOf2 = ImmutableList.copyOf(iterable2);
        this.columnList = immutableListCopyOf2;
        Preconditions.checkArgument(immutableListCopyOf.isEmpty() == immutableListCopyOf2.isEmpty());
        this.rowKeyToIndex = Maps.indexMap(immutableListCopyOf);
        this.columnKeyToIndex = Maps.indexMap(immutableListCopyOf2);
        this.array = (V[][]) ((Object[][]) Array.newInstance((Class<?>) Object.class, immutableListCopyOf.size(), immutableListCopyOf2.size()));
        eraseAll();
    }

    public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> iterable, Iterable<? extends C> iterable2) {
        return new ArrayTable<>(iterable, iterable2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Table.Cell<R, C, V> getCell(int i) {
        return new Tables.AbstractCell<R, C, V>(this, i) { // from class: com.google.common.collect.ArrayTable.2
            final int columnIndex;
            final int rowIndex;
            final /* synthetic */ ArrayTable this$0;
            final /* synthetic */ int val$index;

            {
                this.val$index = i;
                this.this$0 = this;
                this.rowIndex = i / this.columnList.size();
                this.columnIndex = i % this.columnList.size();
            }

            @Override // com.google.common.collect.Table.Cell
            public C getColumnKey() {
                return (C) this.this$0.columnList.get(this.columnIndex);
            }

            @Override // com.google.common.collect.Table.Cell
            public R getRowKey() {
                return (R) this.this$0.rowList.get(this.rowIndex);
            }

            @Override // com.google.common.collect.Table.Cell
            public V getValue() {
                return (V) this.this$0.at(this.rowIndex, this.columnIndex);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V getValue(int i) {
        return at(i / this.columnList.size(), i % this.columnList.size());
    }

    public V at(int i, int i2) {
        Preconditions.checkElementIndex(i, this.rowList.size());
        Preconditions.checkElementIndex(i2, this.columnList.size());
        return this.array[i][i2];
    }

    @Override // com.google.common.collect.AbstractTable
    public Iterator<Table.Cell<R, C, V>> cellIterator() {
        return new AbstractIndexedListIterator<Table.Cell<R, C, V>>(size()) { // from class: com.google.common.collect.ArrayTable.1
            @Override // com.google.common.collect.AbstractIndexedListIterator
            public Table.Cell<R, C, V> get(int i) {
                return ArrayTable.this.getCell(i);
            }
        };
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public Set<Table.Cell<R, C, V>> cellSet() {
        return super.cellSet();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Table
    public Map<R, V> column(C c) {
        Preconditions.checkNotNull(c);
        Integer num = this.columnKeyToIndex.get(c);
        return num == null ? Collections.EMPTY_MAP : new Column(num.intValue());
    }

    public ImmutableList<C> columnKeyList() {
        return this.columnList;
    }

    @Override // com.google.common.collect.Table
    public Map<C, Map<R, V>> columnMap() {
        ArrayTable<R, C, V>.ColumnMap columnMap = this.columnMap;
        if (columnMap != null) {
            return columnMap;
        }
        ArrayTable<R, C, V>.ColumnMap columnMap2 = new ColumnMap();
        this.columnMap = columnMap2;
        return columnMap2;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean contains(Object obj, Object obj2) {
        return containsRow(obj) && containsColumn(obj2);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean containsColumn(Object obj) {
        return this.columnKeyToIndex.containsKey(obj);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean containsRow(Object obj) {
        return this.rowKeyToIndex.containsKey(obj);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean containsValue(Object obj) {
        for (V[] vArr : this.array) {
            for (V v : vArr) {
                if (Objects.equals(obj, v)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public V erase(Object obj, Object obj2) {
        Integer num = this.rowKeyToIndex.get(obj);
        Integer num2 = this.columnKeyToIndex.get(obj2);
        if (num == null || num2 == null) {
            return null;
        }
        return set(num.intValue(), num2.intValue(), null);
    }

    public void eraseAll() {
        for (V[] vArr : this.array) {
            Arrays.fill(vArr, (Object) null);
        }
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public V get(Object obj, Object obj2) {
        Integer num = this.rowKeyToIndex.get(obj);
        Integer num2 = this.columnKeyToIndex.get(obj2);
        if (num == null || num2 == null) {
            return null;
        }
        return at(num.intValue(), num2.intValue());
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean isEmpty() {
        return this.rowList.isEmpty() || this.columnList.isEmpty();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public V put(R r, C c, V v) {
        Preconditions.checkNotNull(r);
        Preconditions.checkNotNull(c);
        Integer num = this.rowKeyToIndex.get(r);
        Preconditions.checkArgument(num != null, "Row %s not in %s", r, this.rowList);
        Integer num2 = this.columnKeyToIndex.get(c);
        Preconditions.checkArgument(num2 != null, "Column %s not in %s", c, this.columnList);
        return set(num.intValue(), num2.intValue(), v);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        super.putAll(table);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @Deprecated
    public V remove(Object obj, Object obj2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Table
    public Map<C, V> row(R r) {
        Preconditions.checkNotNull(r);
        Integer num = this.rowKeyToIndex.get(r);
        return num == null ? Collections.EMPTY_MAP : new Row(num.intValue());
    }

    public ImmutableList<R> rowKeyList() {
        return this.rowList;
    }

    @Override // com.google.common.collect.Table
    public Map<R, Map<C, V>> rowMap() {
        ArrayTable<R, C, V>.RowMap rowMap = this.rowMap;
        if (rowMap != null) {
            return rowMap;
        }
        ArrayTable<R, C, V>.RowMap rowMap2 = new RowMap();
        this.rowMap = rowMap2;
        return rowMap2;
    }

    public V set(int i, int i2, V v) {
        Preconditions.checkElementIndex(i, this.rowList.size());
        Preconditions.checkElementIndex(i2, this.columnList.size());
        V[] vArr = this.array[i];
        V v2 = vArr[i2];
        vArr[i2] = v;
        return v2;
    }

    @Override // com.google.common.collect.Table
    public int size() {
        return this.columnList.size() * this.rowList.size();
    }

    @GwtIncompatible
    public V[][] toArray(Class<V> cls) {
        V[][] vArr = (V[][]) ((Object[][]) Array.newInstance((Class<?>) cls, this.rowList.size(), this.columnList.size()));
        for (int i = 0; i < this.rowList.size(); i++) {
            V[] vArr2 = this.array[i];
            System.arraycopy(vArr2, 0, vArr[i], 0, vArr2.length);
        }
        return vArr;
    }

    @Override // com.google.common.collect.AbstractTable
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public Collection<V> values() {
        return super.values();
    }

    @Override // com.google.common.collect.AbstractTable
    public Iterator<V> valuesIterator() {
        return new AbstractIndexedListIterator<V>(size()) { // from class: com.google.common.collect.ArrayTable.3
            @Override // com.google.common.collect.AbstractIndexedListIterator
            public V get(int i) {
                return (V) ArrayTable.this.getValue(i);
            }
        };
    }

    public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, ? extends V> table) {
        return table instanceof ArrayTable ? new ArrayTable<>((ArrayTable) table) : new ArrayTable<>(table);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public ImmutableSet<C> columnKeySet() {
        return this.columnKeyToIndex.keySet();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public ImmutableSet<R> rowKeySet() {
        return this.rowKeyToIndex.keySet();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ArrayTable(Table<R, C, ? extends V> table) {
        this(table.rowKeySet(), table.columnKeySet());
        putAll(table);
    }

    private ArrayTable(ArrayTable<R, C, V> arrayTable) {
        ImmutableList<R> immutableList = arrayTable.rowList;
        this.rowList = immutableList;
        ImmutableList<C> immutableList2 = arrayTable.columnList;
        this.columnList = immutableList2;
        this.rowKeyToIndex = arrayTable.rowKeyToIndex;
        this.columnKeyToIndex = arrayTable.columnKeyToIndex;
        V[][] vArr = (V[][]) ((Object[][]) Array.newInstance((Class<?>) Object.class, immutableList.size(), immutableList2.size()));
        this.array = vArr;
        for (int i = 0; i < this.rowList.size(); i++) {
            V[] vArr2 = arrayTable.array[i];
            System.arraycopy(vArr2, 0, vArr[i], 0, vArr2.length);
        }
    }
}
