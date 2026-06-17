package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
final class State {
    static final State INITIAL_STATE = new State(Token.EMPTY, 0, 0, 0);
    private final int binaryShiftByteCount;
    private final int binaryShiftCost;
    private final int bitCount;
    private final int mode;
    private final Token token;

    private State(Token token, int i, int i2, int i3) {
        this.token = token;
        this.mode = i;
        this.binaryShiftByteCount = i2;
        this.bitCount = i3;
        this.binaryShiftCost = calculateBinaryShiftCost(i2);
    }

    private static int calculateBinaryShiftCost(int i) {
        if (i > 62) {
            return 21;
        }
        if (i > 31) {
            return 20;
        }
        return i > 0 ? 10 : 0;
    }

    public State addBinaryShiftChar(int i) {
        Token tokenAdd = this.token;
        int i2 = this.mode;
        int i3 = this.bitCount;
        if (i2 == 4 || i2 == 2) {
            int i4 = HighLevelEncoder.LATCH_TABLE[i2][0];
            int i5 = 65535 & i4;
            int i6 = i4 >> 16;
            tokenAdd = tokenAdd.add(i5, i6);
            i3 += i6;
            i2 = 0;
        }
        int i7 = this.binaryShiftByteCount;
        State state = new State(tokenAdd, i2, i7 + 1, i3 + ((i7 == 0 || i7 == 31) ? 18 : i7 == 62 ? 9 : 8));
        return state.binaryShiftByteCount == 2078 ? state.endBinaryShift(i + 1) : state;
    }

    public State appendFLGn(int i) {
        Token tokenAdd;
        Token token = shiftAndAppend(4, 0).token;
        int length = 3;
        if (i < 0) {
            tokenAdd = token.add(0, 3);
        } else {
            if (i > 999999) {
                throw new IllegalArgumentException("ECI code must be between 0 and 999999");
            }
            byte[] bytes = Integer.toString(i).getBytes(StandardCharsets.ISO_8859_1);
            Token tokenAdd2 = token.add(bytes.length, 3);
            for (byte b : bytes) {
                tokenAdd2 = tokenAdd2.add(b - 46, 4);
            }
            length = 3 + (bytes.length * 4);
            tokenAdd = tokenAdd2;
        }
        return new State(tokenAdd, this.mode, 0, this.bitCount + length);
    }

    public State endBinaryShift(int i) {
        int i2 = this.binaryShiftByteCount;
        return i2 == 0 ? this : new State(this.token.addBinaryShift(i - i2, i2), this.mode, 0, this.bitCount);
    }

    public int getBinaryShiftByteCount() {
        return this.binaryShiftByteCount;
    }

    public int getBitCount() {
        return this.bitCount;
    }

    public int getMode() {
        return this.mode;
    }

    public Token getToken() {
        return this.token;
    }

    public boolean isBetterThanOrEqualTo(State state) {
        int i = this.bitCount + (HighLevelEncoder.LATCH_TABLE[this.mode][state.mode] >> 16);
        int i2 = this.binaryShiftByteCount;
        int i3 = state.binaryShiftByteCount;
        if (i2 < i3) {
            i += state.binaryShiftCost - this.binaryShiftCost;
        } else if (i2 > i3 && i3 > 0) {
            i += 10;
        }
        return i <= state.bitCount;
    }

    public State latchAndAppend(int i, int i2) {
        int i3 = this.bitCount;
        Token tokenAdd = this.token;
        int i4 = this.mode;
        if (i != i4) {
            int i5 = HighLevelEncoder.LATCH_TABLE[i4][i];
            int i6 = 65535 & i5;
            int i7 = i5 >> 16;
            tokenAdd = tokenAdd.add(i6, i7);
            i3 += i7;
        }
        int i8 = i == 2 ? 4 : 5;
        return new State(tokenAdd.add(i2, i8), i, 0, i3 + i8);
    }

    public State shiftAndAppend(int i, int i2) {
        Token token = this.token;
        int i3 = this.mode;
        int i4 = i3 == 2 ? 4 : 5;
        return new State(token.add(HighLevelEncoder.SHIFT_TABLE[i3][i], i4).add(i2, 5), this.mode, 0, this.bitCount + i4 + 5);
    }

    public BitArray toBitArray(byte[] bArr) {
        ArrayList arrayList = new ArrayList();
        for (Token previous = endBinaryShift(bArr.length).token; previous != null; previous = previous.getPrevious()) {
            arrayList.add(previous);
        }
        BitArray bitArray = new BitArray();
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            ((Token) arrayList.get(size)).appendTo(bitArray, bArr);
        }
        return bitArray;
    }

    public String toString() {
        return String.format("%s bits=%d bytes=%d", HighLevelEncoder.MODE_NAMES[this.mode], Integer.valueOf(this.bitCount), Integer.valueOf(this.binaryShiftByteCount));
    }
}
