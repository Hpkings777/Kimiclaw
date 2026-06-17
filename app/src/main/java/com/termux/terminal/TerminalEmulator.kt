package com.termux.terminal

class TerminalEmulator(
    val session: TerminalSession,
    initialColumns: Int,
    initialRows: Int,
    var scrollbackRows: Int = 2000
) {
    var columns: Int = initialColumns
        private set
    var rows: Int = initialRows
        private set

    private var cursorCol = 0
    private var cursorRow = 0
    private var scrollOffset = 0

    private val screen = Array(initialRows) { CharArray(initialColumns) { ' ' } }
    private val styles = Array(initialRows) { IntArray(initialColumns) { 0 } }
    private val wrapNext = BooleanArray(initialRows)
    private val lineModified = BooleanArray(initialRows)

    private val scrollback = mutableListOf<TerminalRow>()
    private var scrollbackTotal = 0

    private data class TerminalRow(val chars: CharArray, val style: IntArray, val wrap: Boolean)

    val cursorVisible: Boolean = true
    val cursorX: Int get() = cursorCol
    val cursorY: Int get() = cursorRow

    private var ansiState = AnsiState.GROUND
    private var ansiParams = mutableListOf<Int>()
    private var ansiIntermediate = ""
    private var ansiPrivateMarker = false
    private var utf8BytesLeft = 0
    private var utf8CodePoint = 0

    private enum class AnsiState {
        GROUND, ESCAPE, CSI, CSI_PARAM, CSI_INTERMEDIATE, OSC, DCS, SOS, STRING_END
    }

    fun write(byte: Byte) {
        val unsigned = byte.toInt() and 0xFF
        when {
            utf8BytesLeft > 0 -> {
                utf8CodePoint = (utf8CodePoint shl 6) or (unsigned and 0x3F)
                utf8BytesLeft--
                if (utf8BytesLeft == 0) {
                    handleChar(utf8CodePoint)
                }
            }
            unsigned < 0x80 -> {
                if (unsigned >= 0x20) {
                    handleChar(unsigned)
                } else {
                    handleControl(unsigned)
                }
            }
            unsigned < 0xC0 -> { }
            unsigned < 0xE0 -> {
                utf8BytesLeft = 1
                utf8CodePoint = unsigned and 0x1F
            }
            unsigned < 0xF0 -> {
                utf8BytesLeft = 2
                utf8CodePoint = unsigned and 0x0F
            }
            else -> {
                utf8BytesLeft = 3
                utf8CodePoint = unsigned and 0x07
            }
        }
    }

    private fun handleControl(code: Int) {
        when (code) {
            0x07 -> {} // BEL
            0x08 -> { // BS
                if (cursorCol > 0) cursorCol--
            }
            0x09 -> { // TAB
                cursorCol = (cursorCol / 8 + 1) * 8
                if (cursorCol >= columns) cursorCol = columns - 1
            }
            0x0A, 0x0B, 0x0C -> { // LF, VT, FF
                newline()
            }
            0x0D -> { // CR
                cursorCol = 0
            }
            0x1B -> { // ESC
                ansiState = AnsiState.ESCAPE
                ansiParams.clear()
                ansiIntermediate = ""
                ansiPrivateMarker = false
            }
        }
    }

    private fun handleChar(codePoint: Int) {
        when (ansiState) {
            AnsiState.GROUND -> {
                if (codePoint == 0x1B) {
                    ansiState = AnsiState.ESCAPE
                    return
                }
                putChar(codePoint)
            }
            AnsiState.ESCAPE -> {
                when (codePoint.toChar()) {
                    '[' -> ansiState = AnsiState.CSI
                    ']' -> ansiState = AnsiState.OSC
                    'P' -> ansiState = AnsiState.DCS
                    'X' -> ansiState = AnsiState.SOS
                    '^' -> ansiState = AnsiState.SOS
                    '_' -> ansiState = AnsiState.SOS
                    '7' -> { /* DECSC - save cursor */ ansiState = AnsiState.GROUND }
                    '8' -> { /* DECRC - restore cursor */ ansiState = AnsiState.GROUND }
                    'c' -> { /* RIS - reset */ reset(); ansiState = AnsiState.GROUND }
                    'D' -> { /* IND - index */ newline(); ansiState = AnsiState.GROUND }
                    'M' -> { /* RI - reverse index */ reverseIndex(); ansiState = AnsiState.GROUND }
                    'E' -> { /* NEL */ cursorCol = 0; newline(); ansiState = AnsiState.GROUND }
                    'H' -> { /* HTS */ ansiState = AnsiState.GROUND }
                    else -> ansiState = AnsiState.GROUND
                }
            }
            AnsiState.CSI -> {
                when {
                    codePoint in '0'.code..'9'.code -> {
                        ansiState = AnsiState.CSI_PARAM
                        ansiParams.add(codePoint - '0'.code)
                    }
                    codePoint == '?'.code -> {
                        ansiPrivateMarker = true
                    }
                    codePoint in 0x20..0x2F -> {
                        ansiIntermediate += codePoint.toChar()
                        ansiState = AnsiState.CSI_INTERMEDIATE
                    }
                    codePoint in 0x40..0x7E -> {
                        executeCSI(codePoint.toChar())
                        ansiState = AnsiState.GROUND
                    }
                    else -> ansiState = AnsiState.GROUND
                }
            }
            AnsiState.CSI_PARAM -> {
                when {
                    codePoint in '0'.code..'9'.code -> {
                        val last = ansiParams.size - 1
                        ansiParams[last] = ansiParams[last] * 10 + (codePoint - '0'.code)
                    }
                    codePoint == ';'.code -> ansiParams.add(0)
                    codePoint in 0x20..0x2F -> {
                        ansiIntermediate += codePoint.toChar()
                        ansiState = AnsiState.CSI_INTERMEDIATE
                    }
                    codePoint in 0x40..0x7E -> {
                        executeCSI(codePoint.toChar())
                        ansiState = AnsiState.GROUND
                    }
                    else -> ansiState = AnsiState.GROUND
                }
            }
            AnsiState.CSI_INTERMEDIATE -> {
                if (codePoint in 0x40..0x7E) {
                    ansiState = AnsiState.GROUND
                } else {
                    ansiState = AnsiState.GROUND
                }
            }
            AnsiState.OSC -> {
                if (codePoint == 0x07 || (codePoint == '\\'.code && ansiState == AnsiState.STRING_END)) {
                    ansiState = AnsiState.GROUND
                } else if (codePoint == 0x1B) {
                    ansiState = AnsiState.STRING_END
                }
            }
            AnsiState.DCS, AnsiState.SOS -> {
                if (codePoint == 0x1B) {
                    ansiState = AnsiState.STRING_END
                } else {
                    ansiState = AnsiState.GROUND
                }
            }
            AnsiState.STRING_END -> {
                if (codePoint == '\\'.code) ansiState = AnsiState.GROUND
                else ansiState = AnsiState.GROUND
            }
        }
    }

    private fun executeCSI(command: Char) {
        val p0 = ansiParams.getOrElse(0) { 1 }
        val p1 = ansiParams.getOrElse(1) { 1 }

        if (ansiPrivateMarker) {
            executePrivateCSI(command, p0, p1)
            return
        }

        when (command) {
            'A' -> cursorRow = maxOf(0, cursorRow - p0)
            'B' -> cursorRow = minOf(rows - 1, cursorRow + p0)
            'C' -> cursorCol = minOf(columns - 1, cursorCol + p0)
            'D' -> cursorCol = maxOf(0, cursorCol - p0)
            'H', 'f' -> {
                cursorRow = maxOf(0, minOf(rows - 1, p1 - 1))
                cursorCol = maxOf(0, minOf(columns - 1, p0 - 1))
            }
            'J' -> eraseDisplay(p0)
            'K' -> eraseLine(p0)
            'L' -> insertLines(p0)
            'M' -> deleteLines(p0)
            'P' -> deleteChars(p0)
            '@' -> insertChars(p0)
            'm' -> { /* SGR - ignored for now */ }
            'r' -> { /* DECSTBM - set scroll region, ignored */ }
            'h' -> { /* DECSET - set mode, ignored */ }
            'l' -> { /* DECRST - reset mode, ignored */ }
            's' -> { /* save cursor */ }
            'u' -> { /* restore cursor */ }
        }
    }

    private fun executePrivateCSI(command: Char, p0: Int, p1: Int) {
        // DEC private mode sequences - mostly ignored
    }

    private fun putChar(c: Int) {
        if (cursorCol >= columns) {
            if (wrapNext[cursorRow]) cursorCol = 0
            else cursorCol = columns - 1
        }
        if (cursorRow >= rows) {
            scrollDown()
            cursorRow = rows - 1
        }
        screen[cursorRow][cursorCol] = c.toChar()
        lineModified[cursorRow] = true
        cursorCol++
    }

    private fun newline() {
        if (cursorRow + 1 >= rows) {
            scrollDown()
        } else {
            cursorRow++
        }
    }

    private fun reverseIndex() {
        if (cursorRow > 0) {
            cursorRow--
        }
    }

    private fun scrollDown() {
        val saved = TerminalRow(
            screen[0].copyOf(),
            styles[0].copyOf(),
            wrapNext[0]
        )
        for (r in 1 until rows) {
            screen[r - 1] = screen[r]
            styles[r - 1] = styles[r]
            wrapNext[r - 1] = wrapNext[r]
            lineModified[r - 1] = lineModified[r]
        }
        screen[rows - 1] = CharArray(columns) { ' ' }
        styles[rows - 1] = IntArray(columns) { 0 }
        wrapNext[rows - 1] = false
        lineModified[rows - 1] = false

        scrollback.add(saved)
        if (scrollback.size > scrollbackRows) {
            scrollback.removeAt(0)
        }
        scrollbackTotal++
    }

    private fun eraseDisplay(param: Int) {
        when (param) {
            0 -> {
                for (c in cursorCol until columns) screen[cursorRow][c] = ' '
                for (r in cursorRow + 1 until rows) screen[r] = CharArray(columns) { ' ' }
            }
            1 -> {
                for (c in 0..cursorCol) screen[cursorRow][c] = ' '
                for (r in 0 until cursorRow) screen[r] = CharArray(columns) { ' ' }
            }
            2 -> for (r in 0 until rows) screen[r] = CharArray(columns) { ' ' }
        }
    }

    private fun eraseLine(param: Int) {
        when (param) {
            0 -> for (c in cursorCol until columns) screen[cursorRow][c] = ' '
            1 -> for (c in 0..cursorCol) screen[cursorRow][c] = ' '
            2 -> screen[cursorRow] = CharArray(columns) { ' ' }
        }
    }

    private fun insertLines(count: Int) {
        val n = minOf(count, rows - cursorRow)
        for (r in rows - 1 downTo cursorRow + n) {
            screen[r] = screen[r - n]
            styles[r] = styles[r - n]
            wrapNext[r] = wrapNext[r - n]
        }
        for (r in cursorRow until cursorRow + n) {
            screen[r] = CharArray(columns) { ' ' }
            styles[r] = IntArray(columns) { 0 }
            wrapNext[r] = false
        }
    }

    private fun deleteLines(count: Int) {
        val n = minOf(count, rows - cursorRow)
        for (r in cursorRow until rows - n) {
            screen[r] = screen[r + n]
            styles[r] = styles[r + n]
            wrapNext[r] = wrapNext[r + n]
        }
        for (r in rows - n until rows) {
            screen[r] = CharArray(columns) { ' ' }
            styles[r] = IntArray(columns) { 0 }
            wrapNext[r] = false
        }
    }

    private fun deleteChars(count: Int) {
        val n = minOf(count, columns - cursorCol)
        for (c in cursorCol until columns - n) {
            screen[cursorRow][c] = screen[cursorRow][c + n]
        }
        for (c in columns - n until columns) {
            screen[cursorRow][c] = ' '
        }
    }

    private fun insertChars(count: Int) {
        val n = minOf(count, columns - cursorCol)
        for (c in columns - 1 downTo cursorCol + n) {
            screen[cursorRow][c] = screen[cursorRow][c - n]
        }
        for (c in cursorCol until cursorCol + n) {
            screen[cursorRow][c] = ' '
        }
    }

    private fun reset() {
        for (r in 0 until rows) {
            screen[r] = CharArray(columns) { ' ' }
            styles[r] = IntArray(columns) { 0 }
            wrapNext[r] = false
        }
        cursorCol = 0
        cursorRow = 0
        scrollback.clear()
        scrollbackTotal = 0
    }

    fun resize(newColumns: Int, newRows: Int) {
        if (newColumns == columns && newRows == rows) return
        val oldScreen = screen.map { it.copyOf() }.toTypedArray()
        rows = newRows
        columns = newColumns
        for (r in 0 until rows) {
            screen[r] = oldScreen.getOrElse(r) { CharArray(columns) { ' ' } }.let {
                it.copyOf(columns).also { arr ->
                    for (c in oldScreen[r].indices) arr[c] = oldScreen[r][c]
                    for (c in it.size until columns) arr[c] = ' '
                }
            }
        }
        cursorCol = minOf(cursorCol, columns - 1)
        cursorRow = minOf(cursorRow, rows - 1)
    }

    fun getScreenLines(): List<String> {
        val lines = mutableListOf<String>()
        for (r in 0 until rows) {
            lines.add(String(screen[r]).trimEnd())
        }
        return lines
    }

    fun getSelectedText(startRow: Int, startCol: Int, endRow: Int, endCol: Int): String {
        val sb = StringBuilder()
        for (r in startRow..endRow) {
            val line = String(screen[r])
            if (r == startRow && r == endRow) {
                sb.append(line.substring(startCol, endCol + 1))
            } else if (r == startRow) {
                sb.append(line.substring(startCol))
            } else if (r == endRow) {
                sb.append(line.substring(0, endCol + 1))
            } else {
                sb.append(line.trimEnd())
            }
            if (r != endRow) sb.append('\n')
        }
        return sb.toString()
    }
}
