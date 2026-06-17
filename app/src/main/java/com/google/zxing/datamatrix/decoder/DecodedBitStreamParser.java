package com.google.zxing.datamatrix.decoder;

import com.google.common.base.Ascii;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.ECIStringBuilder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/* JADX INFO: loaded from: classes.dex */
final class DecodedBitStreamParser {
    private static final char[] C40_BASIC_SET_CHARS = {'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final char[] C40_SHIFT2_SET_CHARS;
    private static final char[] TEXT_BASIC_SET_CHARS;
    private static final char[] TEXT_SHIFT2_SET_CHARS;
    private static final char[] TEXT_SHIFT3_SET_CHARS;

    /* JADX INFO: renamed from: com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$1, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$zxing$datamatrix$decoder$DecodedBitStreamParser$Mode;

        static {
            int[] iArr = new int[Mode.values().length];
            $SwitchMap$com$google$zxing$datamatrix$decoder$DecodedBitStreamParser$Mode = iArr;
            try {
                iArr[Mode.C40_ENCODE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$zxing$datamatrix$decoder$DecodedBitStreamParser$Mode[Mode.TEXT_ENCODE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$zxing$datamatrix$decoder$DecodedBitStreamParser$Mode[Mode.ANSIX12_ENCODE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$google$zxing$datamatrix$decoder$DecodedBitStreamParser$Mode[Mode.EDIFACT_ENCODE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$google$zxing$datamatrix$decoder$DecodedBitStreamParser$Mode[Mode.BASE256_ENCODE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$google$zxing$datamatrix$decoder$DecodedBitStreamParser$Mode[Mode.ECI_ENCODE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public enum Mode {
        PAD_ENCODE,
        ASCII_ENCODE,
        C40_ENCODE,
        TEXT_ENCODE,
        ANSIX12_ENCODE,
        EDIFACT_ENCODE,
        BASE256_ENCODE,
        ECI_ENCODE
    }

    static {
        char[] cArr = {'!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', FilenameUtils.EXTENSION_SEPARATOR, IOUtils.DIR_SEPARATOR_UNIX, ':', ';', '<', '=', '>', '?', '@', '[', IOUtils.DIR_SEPARATOR_WINDOWS, ']', '^', '_'};
        C40_SHIFT2_SET_CHARS = cArr;
        TEXT_BASIC_SET_CHARS = new char[]{'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        TEXT_SHIFT2_SET_CHARS = cArr;
        TEXT_SHIFT3_SET_CHARS = new char[]{'`', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '{', '|', '}', '~', Ascii.MAX};
    }

    private DecodedBitStreamParser() {
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static DecoderResult decode(byte[] bArr) throws FormatException {
        int i;
        BitSource bitSource = new BitSource(bArr);
        ECIStringBuilder eCIStringBuilder = new ECIStringBuilder(100);
        boolean z = false;
        StringBuilder sb = new StringBuilder(0);
        int i2 = 1;
        ArrayList arrayList = new ArrayList(1);
        Mode modeDecodeAsciiSegment = Mode.ASCII_ENCODE;
        HashSet hashSet = new HashSet();
        do {
            Mode mode = Mode.ASCII_ENCODE;
            if (modeDecodeAsciiSegment == mode) {
                modeDecodeAsciiSegment = decodeAsciiSegment(bitSource, eCIStringBuilder, sb, hashSet);
            } else {
                switch (AnonymousClass1.$SwitchMap$com$google$zxing$datamatrix$decoder$DecodedBitStreamParser$Mode[modeDecodeAsciiSegment.ordinal()]) {
                    case 1:
                        decodeC40Segment(bitSource, eCIStringBuilder, hashSet);
                        break;
                    case 2:
                        decodeTextSegment(bitSource, eCIStringBuilder, hashSet);
                        break;
                    case 3:
                        decodeAnsiX12Segment(bitSource, eCIStringBuilder);
                        break;
                    case 4:
                        decodeEdifactSegment(bitSource, eCIStringBuilder);
                        break;
                    case 5:
                        decodeBase256Segment(bitSource, eCIStringBuilder, arrayList);
                        break;
                    case 6:
                        decodeECISegment(bitSource, eCIStringBuilder);
                        z = true;
                        break;
                    default:
                        throw FormatException.getFormatInstance();
                }
                modeDecodeAsciiSegment = mode;
            }
            if (modeDecodeAsciiSegment != Mode.PAD_ENCODE) {
            }
            if (sb.length() > 0) {
                eCIStringBuilder.appendCharacters(sb);
            }
            if (!z) {
                if (hashSet.contains(0) || hashSet.contains(4)) {
                    i = 5;
                } else if (hashSet.contains(1) || hashSet.contains(5)) {
                    i2 = 6;
                } else {
                    i = 4;
                }
                String string = eCIStringBuilder.toString();
                if (arrayList.isEmpty()) {
                    arrayList = null;
                }
                return new DecoderResult(bArr, string, arrayList, null, i);
            }
            if (hashSet.contains(0) || hashSet.contains(4)) {
                i2 = 2;
            } else if (hashSet.contains(1) || hashSet.contains(5)) {
                i2 = 3;
            }
            i = i2;
            String string2 = eCIStringBuilder.toString();
            if (arrayList.isEmpty()) {
            }
            return new DecoderResult(bArr, string2, arrayList, null, i);
        } while (bitSource.available() > 0);
        if (sb.length() > 0) {
        }
        if (!z) {
        }
        i = i2;
        String string22 = eCIStringBuilder.toString();
        if (arrayList.isEmpty()) {
        }
        return new DecoderResult(bArr, string22, arrayList, null, i);
    }

    private static void decodeAnsiX12Segment(BitSource bitSource, ECIStringBuilder eCIStringBuilder) throws FormatException {
        int bits;
        int[] iArr = new int[3];
        while (bitSource.available() != 8 && (bits = bitSource.readBits(8)) != 254) {
            parseTwoBytes(bits, bitSource.readBits(8), iArr);
            for (int i = 0; i < 3; i++) {
                int i2 = iArr[i];
                if (i2 == 0) {
                    eCIStringBuilder.append('\r');
                } else if (i2 == 1) {
                    eCIStringBuilder.append('*');
                } else if (i2 == 2) {
                    eCIStringBuilder.append('>');
                } else if (i2 == 3) {
                    eCIStringBuilder.append(' ');
                } else if (i2 < 14) {
                    eCIStringBuilder.append((char) (i2 + 44));
                } else {
                    if (i2 >= 40) {
                        throw FormatException.getFormatInstance();
                    }
                    eCIStringBuilder.append((char) (i2 + 51));
                }
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static Mode decodeAsciiSegment(BitSource bitSource, ECIStringBuilder eCIStringBuilder, StringBuilder sb, Set<Integer> set) throws FormatException {
        boolean z = false;
        do {
            int bits = bitSource.readBits(8);
            if (bits == 0) {
                throw FormatException.getFormatInstance();
            }
            if (bits > 128) {
                if (bits != 129) {
                    if (bits > 229) {
                        switch (bits) {
                            case 230:
                                return Mode.C40_ENCODE;
                            case 231:
                                return Mode.BASE256_ENCODE;
                            case 232:
                                set.add(Integer.valueOf(eCIStringBuilder.length()));
                                eCIStringBuilder.append((char) 29);
                                break;
                            case 233:
                            case 234:
                                break;
                            case 235:
                                z = true;
                                break;
                            case 236:
                                eCIStringBuilder.append("[)>\u001e05\u001d");
                                sb.insert(0, "\u001e\u0004");
                                break;
                            case 237:
                                eCIStringBuilder.append("[)>\u001e06\u001d");
                                sb.insert(0, "\u001e\u0004");
                                break;
                            case 238:
                                return Mode.ANSIX12_ENCODE;
                            case 239:
                                return Mode.TEXT_ENCODE;
                            case 240:
                                return Mode.EDIFACT_ENCODE;
                            case 241:
                                return Mode.ECI_ENCODE;
                            default:
                                if (bits != 254 || bitSource.available() != 0) {
                                    throw FormatException.getFormatInstance();
                                }
                                break;
                        }
                    } else {
                        int i = bits - 130;
                        if (i < 10) {
                            eCIStringBuilder.append('0');
                        }
                        eCIStringBuilder.append(i);
                    }
                } else {
                    return Mode.PAD_ENCODE;
                }
            } else {
                if (z) {
                    bits += 128;
                }
                eCIStringBuilder.append((char) (bits - 1));
                return Mode.ASCII_ENCODE;
            }
        } while (bitSource.available() > 0);
        return Mode.ASCII_ENCODE;
    }

    private static void decodeBase256Segment(BitSource bitSource, ECIStringBuilder eCIStringBuilder, Collection<byte[]> collection) throws FormatException {
        int byteOffset = bitSource.getByteOffset();
        int i = byteOffset + 2;
        int iUnrandomize255State = unrandomize255State(bitSource.readBits(8), byteOffset + 1);
        if (iUnrandomize255State == 0) {
            iUnrandomize255State = bitSource.available() / 8;
        } else if (iUnrandomize255State >= 250) {
            iUnrandomize255State = ((iUnrandomize255State - 249) * 250) + unrandomize255State(bitSource.readBits(8), i);
            i = byteOffset + 3;
        }
        if (iUnrandomize255State < 0) {
            throw FormatException.getFormatInstance();
        }
        byte[] bArr = new byte[iUnrandomize255State];
        int i2 = 0;
        while (i2 < iUnrandomize255State) {
            if (bitSource.available() < 8) {
                throw FormatException.getFormatInstance();
            }
            bArr[i2] = (byte) unrandomize255State(bitSource.readBits(8), i);
            i2++;
            i++;
        }
        collection.add(bArr);
        eCIStringBuilder.append(new String(bArr, StandardCharsets.ISO_8859_1));
    }

    private static void decodeC40Segment(BitSource bitSource, ECIStringBuilder eCIStringBuilder, Set<Integer> set) throws FormatException {
        int bits;
        int[] iArr = new int[3];
        boolean z = false;
        int i = 0;
        while (bitSource.available() != 8 && (bits = bitSource.readBits(8)) != 254) {
            parseTwoBytes(bits, bitSource.readBits(8), iArr);
            for (int i2 = 0; i2 < 3; i2++) {
                int i3 = iArr[i2];
                if (i != 0) {
                    if (i != 1) {
                        if (i == 2) {
                            char[] cArr = C40_SHIFT2_SET_CHARS;
                            if (i3 < cArr.length) {
                                char c = cArr[i3];
                                if (z) {
                                    eCIStringBuilder.append((char) (c + 128));
                                    z = false;
                                } else {
                                    eCIStringBuilder.append(c);
                                }
                            } else if (i3 == 27) {
                                set.add(Integer.valueOf(eCIStringBuilder.length()));
                                eCIStringBuilder.append((char) 29);
                            } else {
                                if (i3 != 30) {
                                    throw FormatException.getFormatInstance();
                                }
                                z = true;
                            }
                            i = 0;
                        } else {
                            if (i != 3) {
                                throw FormatException.getFormatInstance();
                            }
                            if (z) {
                                eCIStringBuilder.append((char) (i3 + 224));
                                z = false;
                                i = 0;
                            } else {
                                eCIStringBuilder.append((char) (i3 + 96));
                                i = 0;
                            }
                        }
                    } else if (z) {
                        eCIStringBuilder.append((char) (i3 + 128));
                        z = false;
                        i = 0;
                    } else {
                        eCIStringBuilder.append((char) i3);
                        i = 0;
                    }
                } else if (i3 < 3) {
                    i = i3 + 1;
                } else {
                    char[] cArr2 = C40_BASIC_SET_CHARS;
                    if (i3 >= cArr2.length) {
                        throw FormatException.getFormatInstance();
                    }
                    char c2 = cArr2[i3];
                    if (z) {
                        eCIStringBuilder.append((char) (c2 + 128));
                        z = false;
                    } else {
                        eCIStringBuilder.append(c2);
                    }
                }
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static void decodeECISegment(BitSource bitSource, ECIStringBuilder eCIStringBuilder) throws FormatException {
        if (bitSource.available() < 8) {
            throw FormatException.getFormatInstance();
        }
        int bits = bitSource.readBits(8);
        if (bits <= 127) {
            eCIStringBuilder.appendECI(bits - 1);
        }
    }

    private static void decodeEdifactSegment(BitSource bitSource, ECIStringBuilder eCIStringBuilder) {
        while (bitSource.available() > 16) {
            for (int i = 0; i < 4; i++) {
                int bits = bitSource.readBits(6);
                if (bits == 31) {
                    int bitOffset = 8 - bitSource.getBitOffset();
                    if (bitOffset != 8) {
                        bitSource.readBits(bitOffset);
                        return;
                    }
                    return;
                }
                if ((bits & 32) == 0) {
                    bits |= 64;
                }
                eCIStringBuilder.append((char) bits);
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static void decodeTextSegment(BitSource bitSource, ECIStringBuilder eCIStringBuilder, Set<Integer> set) throws FormatException {
        int bits;
        int[] iArr = new int[3];
        boolean z = false;
        int i = 0;
        while (bitSource.available() != 8 && (bits = bitSource.readBits(8)) != 254) {
            parseTwoBytes(bits, bitSource.readBits(8), iArr);
            for (int i2 = 0; i2 < 3; i2++) {
                int i3 = iArr[i2];
                if (i != 0) {
                    if (i != 1) {
                        if (i == 2) {
                            char[] cArr = TEXT_SHIFT2_SET_CHARS;
                            if (i3 < cArr.length) {
                                char c = cArr[i3];
                                if (z) {
                                    eCIStringBuilder.append((char) (c + 128));
                                    z = false;
                                } else {
                                    eCIStringBuilder.append(c);
                                }
                            } else if (i3 == 27) {
                                set.add(Integer.valueOf(eCIStringBuilder.length()));
                                eCIStringBuilder.append((char) 29);
                            } else {
                                if (i3 != 30) {
                                    throw FormatException.getFormatInstance();
                                }
                                z = true;
                            }
                            i = 0;
                        } else {
                            if (i != 3) {
                                throw FormatException.getFormatInstance();
                            }
                            char[] cArr2 = TEXT_SHIFT3_SET_CHARS;
                            if (i3 >= cArr2.length) {
                                throw FormatException.getFormatInstance();
                            }
                            char c2 = cArr2[i3];
                            if (z) {
                                eCIStringBuilder.append((char) (c2 + 128));
                                z = false;
                                i = 0;
                            } else {
                                eCIStringBuilder.append(c2);
                                i = 0;
                            }
                        }
                    } else if (z) {
                        eCIStringBuilder.append((char) (i3 + 128));
                        z = false;
                        i = 0;
                    } else {
                        eCIStringBuilder.append((char) i3);
                        i = 0;
                    }
                } else if (i3 < 3) {
                    i = i3 + 1;
                } else {
                    char[] cArr3 = TEXT_BASIC_SET_CHARS;
                    if (i3 >= cArr3.length) {
                        throw FormatException.getFormatInstance();
                    }
                    char c3 = cArr3[i3];
                    if (z) {
                        eCIStringBuilder.append((char) (c3 + 128));
                        z = false;
                    } else {
                        eCIStringBuilder.append(c3);
                    }
                }
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static void parseTwoBytes(int i, int i2, int[] iArr) {
        int i3 = ((i << 8) + i2) - 1;
        int i4 = i3 / 1600;
        iArr[0] = i4;
        int i5 = i3 - (i4 * 1600);
        int i6 = i5 / 40;
        iArr[1] = i6;
        iArr[2] = i5 - (i6 * 40);
    }

    private static int unrandomize255State(int i, int i2) {
        int i3 = i - (((i2 * 149) % 255) + 1);
        return i3 >= 0 ? i3 : i3 + 256;
    }
}
