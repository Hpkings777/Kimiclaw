package com.google.common.net;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.UnmodifiableIterator;
import defpackage.AbstractC4671xI0;
import defpackage.C2768ji;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.IOUtils;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class MediaType {
    private static final String WILDCARD = "*";
    private int hashCode;
    private final ImmutableListMultimap<String, String> parameters;
    private Optional<Charset> parsedCharset;
    private final String subtype;
    private String toString;
    private final String type;
    private static final String CHARSET_ATTRIBUTE = "charset";
    private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of(CHARSET_ATTRIBUTE, Ascii.toLowerCase(StandardCharsets.UTF_8.name()));
    private static final CharMatcher TOKEN_MATCHER = CharMatcher.ascii().and(CharMatcher.javaIsoControl().negate()).and(CharMatcher.isNot(' ')).and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
    private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ascii().and(CharMatcher.noneOf("\"\\\r"));
    private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
    private static final Map<MediaType, MediaType> knownTypes = new HashMap();
    public static final MediaType ANY_TYPE = createConstant("*", "*");
    private static final String TEXT_TYPE = "text";
    public static final MediaType ANY_TEXT_TYPE = createConstant(TEXT_TYPE, "*");
    private static final String IMAGE_TYPE = "image";
    public static final MediaType ANY_IMAGE_TYPE = createConstant(IMAGE_TYPE, "*");
    private static final String AUDIO_TYPE = "audio";
    public static final MediaType ANY_AUDIO_TYPE = createConstant(AUDIO_TYPE, "*");
    private static final String VIDEO_TYPE = "video";
    public static final MediaType ANY_VIDEO_TYPE = createConstant(VIDEO_TYPE, "*");
    private static final String APPLICATION_TYPE = "application";
    public static final MediaType ANY_APPLICATION_TYPE = createConstant(APPLICATION_TYPE, "*");
    private static final String FONT_TYPE = "font";
    public static final MediaType ANY_FONT_TYPE = createConstant(FONT_TYPE, "*");
    public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8(TEXT_TYPE, "cache-manifest");
    public static final MediaType CSS_UTF_8 = createConstantUtf8(TEXT_TYPE, "css");
    public static final MediaType CSV_UTF_8 = createConstantUtf8(TEXT_TYPE, "csv");
    public static final MediaType HTML_UTF_8 = createConstantUtf8(TEXT_TYPE, "html");
    public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8(TEXT_TYPE, "calendar");
    public static final MediaType MD_UTF_8 = createConstantUtf8(TEXT_TYPE, "markdown");
    public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8(TEXT_TYPE, "plain");
    public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8(TEXT_TYPE, "javascript");
    public static final MediaType TSV_UTF_8 = createConstantUtf8(TEXT_TYPE, "tab-separated-values");
    public static final MediaType VCARD_UTF_8 = createConstantUtf8(TEXT_TYPE, "vcard");
    public static final MediaType WML_UTF_8 = createConstantUtf8(TEXT_TYPE, "vnd.wap.wml");
    public static final MediaType XML_UTF_8 = createConstantUtf8(TEXT_TYPE, "xml");
    public static final MediaType VTT_UTF_8 = createConstantUtf8(TEXT_TYPE, "vtt");
    public static final MediaType BMP = createConstant(IMAGE_TYPE, "bmp");
    public static final MediaType CRW = createConstant(IMAGE_TYPE, "x-canon-crw");
    public static final MediaType GIF = createConstant(IMAGE_TYPE, "gif");
    public static final MediaType ICO = createConstant(IMAGE_TYPE, "vnd.microsoft.icon");
    public static final MediaType JPEG = createConstant(IMAGE_TYPE, "jpeg");
    public static final MediaType PNG = createConstant(IMAGE_TYPE, "png");
    public static final MediaType PSD = createConstant(IMAGE_TYPE, "vnd.adobe.photoshop");
    public static final MediaType SVG_UTF_8 = createConstantUtf8(IMAGE_TYPE, "svg+xml");
    public static final MediaType TIFF = createConstant(IMAGE_TYPE, "tiff");
    public static final MediaType AVIF = createConstant(IMAGE_TYPE, "avif");
    public static final MediaType WEBP = createConstant(IMAGE_TYPE, "webp");
    public static final MediaType HEIF = createConstant(IMAGE_TYPE, "heif");
    public static final MediaType JP2K = createConstant(IMAGE_TYPE, "jp2");
    public static final MediaType MP4_AUDIO = createConstant(AUDIO_TYPE, "mp4");
    public static final MediaType MPEG_AUDIO = createConstant(AUDIO_TYPE, "mpeg");
    public static final MediaType OGG_AUDIO = createConstant(AUDIO_TYPE, "ogg");
    public static final MediaType WEBM_AUDIO = createConstant(AUDIO_TYPE, "webm");
    public static final MediaType L16_AUDIO = createConstant(AUDIO_TYPE, "l16");
    public static final MediaType L24_AUDIO = createConstant(AUDIO_TYPE, "l24");
    public static final MediaType BASIC_AUDIO = createConstant(AUDIO_TYPE, "basic");
    public static final MediaType AAC_AUDIO = createConstant(AUDIO_TYPE, "aac");
    public static final MediaType VORBIS_AUDIO = createConstant(AUDIO_TYPE, "vorbis");
    public static final MediaType WMA_AUDIO = createConstant(AUDIO_TYPE, "x-ms-wma");
    public static final MediaType WAX_AUDIO = createConstant(AUDIO_TYPE, "x-ms-wax");
    public static final MediaType VND_REAL_AUDIO = createConstant(AUDIO_TYPE, "vnd.rn-realaudio");
    public static final MediaType VND_WAVE_AUDIO = createConstant(AUDIO_TYPE, "vnd.wave");
    public static final MediaType MP4_VIDEO = createConstant(VIDEO_TYPE, "mp4");
    public static final MediaType MPEG_VIDEO = createConstant(VIDEO_TYPE, "mpeg");
    public static final MediaType OGG_VIDEO = createConstant(VIDEO_TYPE, "ogg");
    public static final MediaType QUICKTIME = createConstant(VIDEO_TYPE, "quicktime");
    public static final MediaType WEBM_VIDEO = createConstant(VIDEO_TYPE, "webm");
    public static final MediaType WMV = createConstant(VIDEO_TYPE, "x-ms-wmv");
    public static final MediaType FLV_VIDEO = createConstant(VIDEO_TYPE, "x-flv");
    public static final MediaType THREE_GPP_VIDEO = createConstant(VIDEO_TYPE, "3gpp");
    public static final MediaType THREE_GPP2_VIDEO = createConstant(VIDEO_TYPE, "3gpp2");
    public static final MediaType APPLICATION_XML_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "xml");
    public static final MediaType ATOM_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "atom+xml");
    public static final MediaType BZIP2 = createConstant(APPLICATION_TYPE, "x-bzip2");
    public static final MediaType DART_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "dart");
    public static final MediaType APPLE_PASSBOOK = createConstant(APPLICATION_TYPE, "vnd.apple.pkpass");
    public static final MediaType EOT = createConstant(APPLICATION_TYPE, "vnd.ms-fontobject");
    public static final MediaType EPUB = createConstant(APPLICATION_TYPE, "epub+zip");
    public static final MediaType FORM_DATA = createConstant(APPLICATION_TYPE, "x-www-form-urlencoded");
    public static final MediaType KEY_ARCHIVE = createConstant(APPLICATION_TYPE, "pkcs12");
    public static final MediaType APPLICATION_BINARY = createConstant(APPLICATION_TYPE, "binary");
    public static final MediaType CBOR = createConstant(APPLICATION_TYPE, "cbor");
    public static final MediaType GEO_JSON = createConstant(APPLICATION_TYPE, "geo+json");
    public static final MediaType GZIP = createConstant(APPLICATION_TYPE, "x-gzip");
    public static final MediaType HAL_JSON = createConstant(APPLICATION_TYPE, "hal+json");
    public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "javascript");
    public static final MediaType JOSE = createConstant(APPLICATION_TYPE, "jose");
    public static final MediaType JOSE_JSON = createConstant(APPLICATION_TYPE, "jose+json");
    public static final MediaType JSON_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "json");
    public static final MediaType JWT = createConstant(APPLICATION_TYPE, "jwt");
    public static final MediaType MANIFEST_JSON_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "manifest+json");
    public static final MediaType KML = createConstant(APPLICATION_TYPE, "vnd.google-earth.kml+xml");
    public static final MediaType KMZ = createConstant(APPLICATION_TYPE, "vnd.google-earth.kmz");
    public static final MediaType MBOX = createConstant(APPLICATION_TYPE, "mbox");
    public static final MediaType APPLE_MOBILE_CONFIG = createConstant(APPLICATION_TYPE, "x-apple-aspen-config");
    public static final MediaType MICROSOFT_EXCEL = createConstant(APPLICATION_TYPE, "vnd.ms-excel");
    public static final MediaType MICROSOFT_OUTLOOK = createConstant(APPLICATION_TYPE, "vnd.ms-outlook");
    public static final MediaType MICROSOFT_POWERPOINT = createConstant(APPLICATION_TYPE, "vnd.ms-powerpoint");
    public static final MediaType MICROSOFT_WORD = createConstant(APPLICATION_TYPE, "msword");
    public static final MediaType MEDIA_PRESENTATION_DESCRIPTION = createConstant(APPLICATION_TYPE, "dash+xml");
    public static final MediaType WASM_APPLICATION = createConstant(APPLICATION_TYPE, "wasm");
    public static final MediaType NACL_APPLICATION = createConstant(APPLICATION_TYPE, "x-nacl");
    public static final MediaType NACL_PORTABLE_APPLICATION = createConstant(APPLICATION_TYPE, "x-pnacl");
    public static final MediaType OCTET_STREAM = createConstant(APPLICATION_TYPE, "octet-stream");
    public static final MediaType OGG_CONTAINER = createConstant(APPLICATION_TYPE, "ogg");
    public static final MediaType OOXML_DOCUMENT = createConstant(APPLICATION_TYPE, "vnd.openxmlformats-officedocument.wordprocessingml.document");
    public static final MediaType OOXML_PRESENTATION = createConstant(APPLICATION_TYPE, "vnd.openxmlformats-officedocument.presentationml.presentation");
    public static final MediaType OOXML_SHEET = createConstant(APPLICATION_TYPE, "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant(APPLICATION_TYPE, "vnd.oasis.opendocument.graphics");
    public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant(APPLICATION_TYPE, "vnd.oasis.opendocument.presentation");
    public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant(APPLICATION_TYPE, "vnd.oasis.opendocument.spreadsheet");
    public static final MediaType OPENDOCUMENT_TEXT = createConstant(APPLICATION_TYPE, "vnd.oasis.opendocument.text");
    public static final MediaType OPENSEARCH_DESCRIPTION_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "opensearchdescription+xml");
    public static final MediaType PDF = createConstant(APPLICATION_TYPE, "pdf");
    public static final MediaType POSTSCRIPT = createConstant(APPLICATION_TYPE, "postscript");
    public static final MediaType PROTOBUF = createConstant(APPLICATION_TYPE, "protobuf");
    public static final MediaType RDF_XML_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "rdf+xml");
    public static final MediaType RTF_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "rtf");
    public static final MediaType SFNT = createConstant(APPLICATION_TYPE, "font-sfnt");
    public static final MediaType SHOCKWAVE_FLASH = createConstant(APPLICATION_TYPE, "x-shockwave-flash");
    public static final MediaType SKETCHUP = createConstant(APPLICATION_TYPE, "vnd.sketchup.skp");
    public static final MediaType SOAP_XML_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "soap+xml");
    public static final MediaType TAR = createConstant(APPLICATION_TYPE, "x-tar");
    public static final MediaType WOFF = createConstant(APPLICATION_TYPE, "font-woff");
    public static final MediaType WOFF2 = createConstant(APPLICATION_TYPE, "font-woff2");
    public static final MediaType XHTML_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "xhtml+xml");
    public static final MediaType XRD_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "xrd+xml");
    public static final MediaType ZIP = createConstant(APPLICATION_TYPE, "zip");
    public static final MediaType FONT_COLLECTION = createConstant(FONT_TYPE, "collection");
    public static final MediaType FONT_OTF = createConstant(FONT_TYPE, "otf");
    public static final MediaType FONT_SFNT = createConstant(FONT_TYPE, "sfnt");
    public static final MediaType FONT_TTF = createConstant(FONT_TYPE, "ttf");
    public static final MediaType FONT_WOFF = createConstant(FONT_TYPE, "woff");
    public static final MediaType FONT_WOFF2 = createConstant(FONT_TYPE, "woff2");
    private static final Joiner.MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");

    private MediaType(String str, String str2, ImmutableListMultimap<String, String> immutableListMultimap) {
        this.type = str;
        this.subtype = str2;
        this.parameters = immutableListMultimap;
    }

    private static MediaType addKnownType(MediaType mediaType) {
        knownTypes.put(mediaType, mediaType);
        return mediaType;
    }

    private String computeToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.type);
        sb.append(IOUtils.DIR_SEPARATOR_UNIX);
        sb.append(this.subtype);
        if (!this.parameters.isEmpty()) {
            sb.append("; ");
            PARAMETER_JOINER.appendTo(sb, Multimaps.transformValues((ListMultimap) this.parameters, (Function) new C2768ji(11)).entries());
        }
        return sb.toString();
    }

    private static void consumeSeparator(Tokenizer tokenizer, char c) {
        CharMatcher charMatcher = LINEAR_WHITE_SPACE;
        tokenizer.consumeTokenIfPresent(charMatcher);
        tokenizer.consumeCharacter(c);
        tokenizer.consumeTokenIfPresent(charMatcher);
    }

    public static MediaType create(String str, String str2) {
        MediaType mediaTypeCreate = create(str, str2, ImmutableListMultimap.of());
        mediaTypeCreate.parsedCharset = Optional.absent();
        return mediaTypeCreate;
    }

    public static MediaType createApplicationType(String str) {
        return create(APPLICATION_TYPE, str);
    }

    public static MediaType createAudioType(String str) {
        return create(AUDIO_TYPE, str);
    }

    private static MediaType createConstant(String str, String str2) {
        MediaType mediaTypeAddKnownType = addKnownType(new MediaType(str, str2, ImmutableListMultimap.of()));
        mediaTypeAddKnownType.parsedCharset = Optional.absent();
        return mediaTypeAddKnownType;
    }

    private static MediaType createConstantUtf8(String str, String str2) {
        MediaType mediaTypeAddKnownType = addKnownType(new MediaType(str, str2, UTF_8_CONSTANT_PARAMETERS));
        mediaTypeAddKnownType.parsedCharset = Optional.of(StandardCharsets.UTF_8);
        return mediaTypeAddKnownType;
    }

    public static MediaType createFontType(String str) {
        return create(FONT_TYPE, str);
    }

    public static MediaType createImageType(String str) {
        return create(IMAGE_TYPE, str);
    }

    public static MediaType createTextType(String str) {
        return create(TEXT_TYPE, str);
    }

    public static MediaType createVideoType(String str) {
        return create(VIDEO_TYPE, str);
    }

    private static String escapeAndQuote(String str) {
        StringBuilder sb = new StringBuilder(str.length() + 16);
        sb.append('\"');
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '\r' || cCharAt == '\\' || cCharAt == '\"') {
                sb.append(IOUtils.DIR_SEPARATOR_WINDOWS);
            }
            sb.append(cCharAt);
        }
        sb.append('\"');
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$computeToString$0(String str) {
        return (!TOKEN_MATCHER.matchesAllOf(str) || str.isEmpty()) ? escapeAndQuote(str) : str;
    }

    private static String normalizeParameterValue(String str, String str2) {
        Preconditions.checkNotNull(str2);
        Preconditions.checkArgument(CharMatcher.ascii().matchesAllOf(str2), "parameter values must be ASCII: %s", str2);
        return str.equals(CHARSET_ATTRIBUTE) ? Ascii.toLowerCase(str2) : str2;
    }

    private static String normalizeToken(String str) {
        Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(str));
        Preconditions.checkArgument(!str.isEmpty());
        return Ascii.toLowerCase(str);
    }

    private Map<String, ImmutableMultiset<String>> parametersAsMap() {
        return Maps.transformValues(this.parameters.asMap(), new C2768ji(12));
    }

    public static MediaType parse(String str) {
        String strConsumeToken;
        Preconditions.checkNotNull(str);
        Tokenizer tokenizer = new Tokenizer(str);
        try {
            CharMatcher charMatcher = TOKEN_MATCHER;
            String strConsumeToken2 = tokenizer.consumeToken(charMatcher);
            consumeSeparator(tokenizer, IOUtils.DIR_SEPARATOR_UNIX);
            String strConsumeToken3 = tokenizer.consumeToken(charMatcher);
            ImmutableListMultimap.Builder builder = ImmutableListMultimap.builder();
            while (tokenizer.hasMore()) {
                consumeSeparator(tokenizer, ';');
                CharMatcher charMatcher2 = TOKEN_MATCHER;
                String strConsumeToken4 = tokenizer.consumeToken(charMatcher2);
                consumeSeparator(tokenizer, '=');
                if (tokenizer.previewChar() == '\"') {
                    tokenizer.consumeCharacter('\"');
                    StringBuilder sb = new StringBuilder();
                    while (tokenizer.previewChar() != '\"') {
                        if (tokenizer.previewChar() == '\\') {
                            tokenizer.consumeCharacter(IOUtils.DIR_SEPARATOR_WINDOWS);
                            sb.append(tokenizer.consumeCharacter(CharMatcher.ascii()));
                        } else {
                            sb.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
                        }
                    }
                    strConsumeToken = sb.toString();
                    tokenizer.consumeCharacter('\"');
                } else {
                    strConsumeToken = tokenizer.consumeToken(charMatcher2);
                }
                builder.put(strConsumeToken4, strConsumeToken);
            }
            return create(strConsumeToken2, strConsumeToken3, builder.build());
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(AbstractC4671xI0.z("Could not parse '", str, "'"), e);
        }
    }

    public Optional<Charset> charset() {
        Optional<Charset> optionalAbsent = this.parsedCharset;
        if (optionalAbsent == null) {
            optionalAbsent = Optional.absent();
            UnmodifiableIterator<String> it = this.parameters.get(CHARSET_ATTRIBUTE).iterator();
            String str = null;
            while (it.hasNext()) {
                String next = it.next();
                if (str == null) {
                    optionalAbsent = Optional.of(Charset.forName(next));
                    str = next;
                } else if (!str.equals(next)) {
                    throw new IllegalStateException("Multiple charset values defined: " + str + ", " + next);
                }
            }
            this.parsedCharset = optionalAbsent;
        }
        return optionalAbsent;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof MediaType) {
            MediaType mediaType = (MediaType) obj;
            if (this.type.equals(mediaType.type) && this.subtype.equals(mediaType.subtype) && parametersAsMap().equals(mediaType.parametersAsMap())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasWildcard() {
        return this.type.equals("*") || this.subtype.equals("*");
    }

    public int hashCode() {
        int i = this.hashCode;
        if (i != 0) {
            return i;
        }
        int iHash = Objects.hash(this.type, this.subtype, parametersAsMap());
        this.hashCode = iHash;
        return iHash;
    }

    public boolean is(MediaType mediaType) {
        if (mediaType.type.equals("*") || mediaType.type.equals(this.type)) {
            return (mediaType.subtype.equals("*") || mediaType.subtype.equals(this.subtype)) && this.parameters.entries().containsAll(mediaType.parameters.entries());
        }
        return false;
    }

    public ImmutableListMultimap<String, String> parameters() {
        return this.parameters;
    }

    public String subtype() {
        return this.subtype;
    }

    public String toString() {
        String str = this.toString;
        if (str != null) {
            return str;
        }
        String strComputeToString = computeToString();
        this.toString = strComputeToString;
        return strComputeToString;
    }

    public String type() {
        return this.type;
    }

    public MediaType withCharset(Charset charset) {
        Preconditions.checkNotNull(charset);
        MediaType mediaTypeWithParameter = withParameter(CHARSET_ATTRIBUTE, charset.name());
        mediaTypeWithParameter.parsedCharset = Optional.of(charset);
        return mediaTypeWithParameter;
    }

    public MediaType withParameter(String str, String str2) {
        return withParameters(str, ImmutableSet.of(str2));
    }

    public MediaType withParameters(Multimap<String, String> multimap) {
        return create(this.type, this.subtype, multimap);
    }

    public MediaType withoutParameters() {
        return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
    }

    public MediaType withParameters(String str, Iterable<String> iterable) {
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(iterable);
        String strNormalizeToken = normalizeToken(str);
        ImmutableListMultimap.Builder builder = ImmutableListMultimap.builder();
        UnmodifiableIterator<Map.Entry<String, String>> it = this.parameters.entries().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> next = it.next();
            String key = next.getKey();
            if (!strNormalizeToken.equals(key)) {
                builder.put(key, next.getValue());
            }
        }
        Iterator<String> it2 = iterable.iterator();
        while (it2.hasNext()) {
            builder.put(strNormalizeToken, normalizeParameterValue(strNormalizeToken, it2.next()));
        }
        MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
        if (!strNormalizeToken.equals(CHARSET_ATTRIBUTE)) {
            mediaType.parsedCharset = this.parsedCharset;
        }
        return (MediaType) MoreObjects.firstNonNull(knownTypes.get(mediaType), mediaType);
    }

    private static MediaType create(String str, String str2, Multimap<String, String> multimap) {
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(str2);
        Preconditions.checkNotNull(multimap);
        String strNormalizeToken = normalizeToken(str);
        String strNormalizeToken2 = normalizeToken(str2);
        Preconditions.checkArgument(!strNormalizeToken.equals("*") || strNormalizeToken2.equals("*"), "A wildcard type cannot be used with a non-wildcard subtype");
        ImmutableListMultimap.Builder builder = ImmutableListMultimap.builder();
        for (Map.Entry<String, String> entry : multimap.entries()) {
            String strNormalizeToken3 = normalizeToken(entry.getKey());
            builder.put(strNormalizeToken3, normalizeParameterValue(strNormalizeToken3, entry.getValue()));
        }
        MediaType mediaType = new MediaType(strNormalizeToken, strNormalizeToken2, builder.build());
        return (MediaType) MoreObjects.firstNonNull(knownTypes.get(mediaType), mediaType);
    }

    public static final class Tokenizer {
        final String input;
        int position = 0;

        public Tokenizer(String str) {
            this.input = str;
        }

        public char consumeCharacter(CharMatcher charMatcher) {
            Preconditions.checkState(hasMore());
            char cPreviewChar = previewChar();
            Preconditions.checkState(charMatcher.matches(cPreviewChar));
            this.position++;
            return cPreviewChar;
        }

        public String consumeToken(CharMatcher charMatcher) {
            int i = this.position;
            String strConsumeTokenIfPresent = consumeTokenIfPresent(charMatcher);
            Preconditions.checkState(this.position != i);
            return strConsumeTokenIfPresent;
        }

        public String consumeTokenIfPresent(CharMatcher charMatcher) {
            Preconditions.checkState(hasMore());
            int i = this.position;
            this.position = charMatcher.negate().indexIn(this.input, i);
            return hasMore() ? this.input.substring(i, this.position) : this.input.substring(i);
        }

        public boolean hasMore() {
            int i = this.position;
            return i >= 0 && i < this.input.length();
        }

        public char previewChar() {
            Preconditions.checkState(hasMore());
            return this.input.charAt(this.position);
        }

        public char consumeCharacter(char c) {
            Preconditions.checkState(hasMore());
            Preconditions.checkState(previewChar() == c);
            this.position++;
            return c;
        }
    }
}
