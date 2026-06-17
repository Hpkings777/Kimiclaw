package com.google.zxing.aztec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.aztec.decoder.Decoder;
import com.google.zxing.aztec.detector.Detector;
import com.google.zxing.common.DecoderResult;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class AztecReader implements Reader {
    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return decode(binaryBitmap, null);
    }

    @Override // com.google.zxing.Reader
    public void reset() {
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0076 A[LOOP:0: B:38:0x0074->B:39:0x0076, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00a6  */
    @Override // com.google.zxing.Reader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Result decode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException, FormatException {
        ResultPoint[] points;
        int errorsCorrected;
        NotFoundException notFoundException;
        ResultPoint[] points2;
        int errorsCorrected2;
        FormatException formatException;
        ResultPoint[] resultPointArr;
        List<byte[]> byteSegments;
        String eCLevel;
        ResultPointCallback resultPointCallback;
        AztecDetectorResult aztecDetectorResultDetect;
        Detector detector = new Detector(binaryBitmap.getBlackMatrix());
        DecoderResult decoderResultDecode = null;
        try {
            aztecDetectorResultDetect = detector.detect(false);
            points = aztecDetectorResultDetect.getPoints();
            try {
                errorsCorrected = aztecDetectorResultDetect.getErrorsCorrected();
            } catch (FormatException e) {
                e = e;
                errorsCorrected = 0;
                errorsCorrected2 = errorsCorrected;
                formatException = e;
                points2 = points;
                notFoundException = null;
                if (decoderResultDecode == null) {
                }
                resultPointArr = points2;
                int i = errorsCorrected2;
                if (map != null) {
                    while (i < r1) {
                    }
                }
                Result result = new Result(decoderResultDecode.getText(), decoderResultDecode.getRawBytes(), decoderResultDecode.getNumBits(), resultPointArr, BarcodeFormat.AZTEC, System.currentTimeMillis());
                byteSegments = decoderResultDecode.getByteSegments();
                if (byteSegments != null) {
                }
                eCLevel = decoderResultDecode.getECLevel();
                if (eCLevel != null) {
                }
                result.putMetadata(ResultMetadataType.ERRORS_CORRECTED, Integer.valueOf(decoderResultDecode.getErrorsCorrected().intValue() + i));
                result.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]z" + decoderResultDecode.getSymbologyModifier());
                return result;
            } catch (NotFoundException e2) {
                e = e2;
                errorsCorrected = 0;
                ResultPoint[] resultPointArr2 = points;
                notFoundException = e;
                points2 = resultPointArr2;
                errorsCorrected2 = errorsCorrected;
                formatException = null;
                if (decoderResultDecode == null) {
                }
                resultPointArr = points2;
                int i2 = errorsCorrected2;
                if (map != null) {
                }
                Result result2 = new Result(decoderResultDecode.getText(), decoderResultDecode.getRawBytes(), decoderResultDecode.getNumBits(), resultPointArr, BarcodeFormat.AZTEC, System.currentTimeMillis());
                byteSegments = decoderResultDecode.getByteSegments();
                if (byteSegments != null) {
                }
                eCLevel = decoderResultDecode.getECLevel();
                if (eCLevel != null) {
                }
                result2.putMetadata(ResultMetadataType.ERRORS_CORRECTED, Integer.valueOf(decoderResultDecode.getErrorsCorrected().intValue() + i2));
                result2.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]z" + decoderResultDecode.getSymbologyModifier());
                return result2;
            }
        } catch (FormatException e3) {
            e = e3;
            points = null;
        } catch (NotFoundException e4) {
            e = e4;
            points = null;
        }
        try {
            errorsCorrected2 = errorsCorrected;
            formatException = null;
            decoderResultDecode = new Decoder().decode(aztecDetectorResultDetect);
            points2 = points;
            notFoundException = null;
        } catch (FormatException e5) {
            e = e5;
            errorsCorrected2 = errorsCorrected;
            formatException = e;
            points2 = points;
            notFoundException = null;
        } catch (NotFoundException e6) {
            e = e6;
            ResultPoint[] resultPointArr22 = points;
            notFoundException = e;
            points2 = resultPointArr22;
            errorsCorrected2 = errorsCorrected;
            formatException = null;
        }
        if (decoderResultDecode == null) {
            try {
                AztecDetectorResult aztecDetectorResultDetect2 = detector.detect(true);
                points2 = aztecDetectorResultDetect2.getPoints();
                errorsCorrected2 = aztecDetectorResultDetect2.getErrorsCorrected();
                decoderResultDecode = new Decoder().decode(aztecDetectorResultDetect2);
            } catch (FormatException | NotFoundException e7) {
                if (notFoundException != null) {
                    throw notFoundException;
                }
                if (formatException != null) {
                    throw formatException;
                }
                throw e7;
            }
        }
        resultPointArr = points2;
        int i22 = errorsCorrected2;
        if (map != null && (resultPointCallback = (ResultPointCallback) map.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK)) != null) {
            for (ResultPoint resultPoint : resultPointArr) {
                resultPointCallback.foundPossibleResultPoint(resultPoint);
            }
        }
        Result result22 = new Result(decoderResultDecode.getText(), decoderResultDecode.getRawBytes(), decoderResultDecode.getNumBits(), resultPointArr, BarcodeFormat.AZTEC, System.currentTimeMillis());
        byteSegments = decoderResultDecode.getByteSegments();
        if (byteSegments != null) {
            result22.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }
        eCLevel = decoderResultDecode.getECLevel();
        if (eCLevel != null) {
            result22.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, eCLevel);
        }
        result22.putMetadata(ResultMetadataType.ERRORS_CORRECTED, Integer.valueOf(decoderResultDecode.getErrorsCorrected().intValue() + i22));
        result22.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]z" + decoderResultDecode.getSymbologyModifier());
        return result22;
    }
}
