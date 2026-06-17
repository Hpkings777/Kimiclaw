package com.termux.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.TextPaint
import android.util.AttributeSet
import android.view.ActionMode
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Scroller
import com.termux.terminal.TerminalEmulator
import com.termux.terminal.TerminalSession
import java.util.Arrays

class TerminalView(
    context: Context,
    attrs: AttributeSet?
) : View(context, attrs) {

    companion object {
        var DEBUG = false
    }

    var session: TerminalSession? = null
        set(value) {
            field = value
            emulator = value?.emulator
        }
    var emulator: TerminalEmulator? = null
    var client: TerminalViewClient? = null

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = Typeface.MONOSPACE
        textSize = 48f
    }
    private val cursorPaint = Paint().apply {
        color = -1
    }
    private val selectionPaint = Paint().apply {
        color = 0x40808080.toInt()
    }
    private var charWidth = 0f
    private var charHeight = 0f
    private var topRow = 0
    private val scroller = Scroller(context)
    private var scaleFactor = 1f

    private val gestureDetector: GestureDetector
    private val scaleDetector: ScaleGestureDetector
    private var doubleTapSlop = 20
    private var longPressTimeout = ViewConfiguration.getLongPressTimeout()

    private var selectionInProgress = false
    private var selectionStart = intArrayOf(-1, -1, -1, -1)
    private val selectionUpdateRunnable = Runnable { }
    private var cursorBlinkHandler: Handler? = null
    private var cursorBlinkRunnable: CursorBlinkRunnable? = null
    var cursorBlinkRate: Int = 500

    private var textSelectionActionMode: ActionMode? = null

    inner class CursorBlinkRunnable(
        var emu: TerminalEmulator?
    ) : Runnable {
        override fun run() {
            emu?.let {
                it.cursorVisible // toggle visibility
                invalidate()
                cursorBlinkHandler?.postDelayed(this, cursorBlinkRate.toLong())
            }
        }
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                client?.onSingleTapUp(e)
                return true
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                client?.onDoubleTap(e)
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                if (!selectionInProgress) {
                    startSelection(e)
                }
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                val emulator = emulator
                if (selectionInProgress) {
                    updateSelection(e2)
                } else if (emulator != null) {
                    topRow = (topRow + distanceY / charHeight).toInt().coerceIn(
                        -(emulator.session.outputQueue.completedCount), 0
                    )
                    invalidate()
                }
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                scroller.fling(
                    0, topRow, 0, velocityY.toInt(),
                    0, 0, Integer.MIN_VALUE, 0
                )
                invalidate()
                return true
            }
        })

        scaleDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = scaleFactor.coerceIn(0.5f, 3f)
                textPaint.textSize = 48f * scaleFactor
                recalculateCharDimensions()
                invalidate()
                return true
            }
        })
        scaleDetector.isQuickScaleEnabled = false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        recalculateCharDimensions()
        session?.let { session ->
            val cols = maxOf(4, (w / charWidth).toInt())
            val rows = maxOf(4, ((h - charHeight) / charHeight).toInt())
            emulator?.resize(cols, rows)
        }
    }

    private fun recalculateCharDimensions() {
        val fm = textPaint.fontMetrics
        charWidth = textPaint.measureText("W")
        charHeight = fm.descent - fm.ascent
    }

    override fun onDraw(canvas: Canvas) {
        val emu = emulator ?: run {
            canvas.drawColor(android.graphics.Color.BLACK)
            return
        }

        canvas.drawColor(android.graphics.Color.BLACK)

        val lines = emu.getScreenLines()
        val yOffset = -textPaint.fontMetrics.ascent

        lines.forEachIndexed { row, line ->
            canvas.drawText(line, 0f, row * charHeight + yOffset, textPaint)
        }

        if (emu.cursorVisible) {
            val cursorY = emu.cursorY
            val cursorX = emu.cursorX
            if (cursorY in 0 until emu.rows && cursorX in 0 until emu.columns) {
                canvas.drawRect(
                    cursorX * charWidth,
                    cursorY * charHeight,
                    (cursorX + 1) * charWidth,
                    (cursorY + 1) * charHeight,
                    cursorPaint
                )
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val session = session ?: return super.onKeyDown(keyCode, event)
        val emu = emulator ?: return super.onKeyDown(keyCode, event)

        emu.session.outputQueue.apply {
            // FIXME: proper key handling
        }

        return handleKeyCode(keyCode, event?.metaState ?: 0)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    private fun handleKeyCode(keyCode: Int, metaState: Int): Boolean {
        val session = session ?: return false
        val emu = emulator ?: return false

        val isCtrl = (metaState and KeyEvent.META_CTRL_ON) != 0 ||
                (metaState and KeyEvent.META_CTRL_LEFT_ON) != 0 ||
                (metaState and KeyEvent.META_CTRL_RIGHT_ON) != 0
        val isAlt = (metaState and KeyEvent.META_ALT_ON) != 0

        if (isCtrl && keyCode == KeyEvent.KEYCODE_C) {
            copySelectionToClipboard()
            return true
        }
        if (isCtrl && keyCode == KeyEvent.KEYCODE_V) {
            pasteFromClipboard()
            return true
        }
        if (isCtrl && keyCode == KeyEvent.KEYCODE_A) {
            session.write("\u0001")
            return true
        }
        if (isCtrl && keyCode == KeyEvent.KEYCODE_D) {
            session.write("\u0004")
            return true
        }
        if (isCtrl && keyCode == KeyEvent.KEYCODE_E) {
            session.write("\u0005")
            return true
        }

        return false
    }

    override fun onCreateInputConnection(editorInfo: EditorInfo): InputConnection {
        editorInfo.inputType = android.text.InputType.TYPE_CLASS_TEXT
        editorInfo.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        return TerminalInputConnection(this)
    }

    override fun computeVerticalScrollOffset(): Int {
        return -topRow
    }

    override fun computeVerticalScrollRange(): Int {
        return 1000
    }

    override fun computeVerticalScrollExtent(): Int {
        return emulator?.rows ?: 1
    }

    fun onSessionUpdated() {
        if (emulator?.let { it.session.outputQueue.finished } == true) {
            invalidate()
        }
        invalidate()
    }

    fun onSessionFinished() {
        invalidate()
    }

    private fun startSelection(event: MotionEvent) {
        selectionInProgress = true
        val x = (event.x / charWidth).toInt()
        val y = (event.y / charHeight).toInt() + topRow
        selectionStart = intArrayOf(x, y, x, y)
        invalidate()
    }

    private fun updateSelection(event: MotionEvent) {
        val x = (event.x / charWidth).toInt()
        val y = (event.y / charHeight).toInt() + topRow
        selectionStart[2] = x
        selectionStart[3] = y
        invalidate()
    }

    private fun copySelectionToClipboard() {
        val emu = emulator ?: return
        val text = emu.getSelectedText(
            selectionStart[1], selectionStart[0],
            selectionStart[3], selectionStart[2]
        )
        if (text.isNotEmpty()) {
            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("terminal", text))
        }
        cancelSelection()
    }

    private fun cancelSelection() {
        selectionInProgress = false
        Arrays.fill(selectionStart, -1)
        invalidate()
    }

    private fun pasteFromClipboard() {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.primaryClip?.getItemAt(0)?.text?.toString()?.let { text ->
            session?.write(text)
        }
    }

    fun startCursorBlinker(emu: TerminalEmulator?) {
        stopCursorBlinker()
        if (cursorBlinkRate in 100..2000) {
            cursorBlinkHandler = Handler(Looper.getMainLooper())
            cursorBlinkRunnable = CursorBlinkRunnable(emu)
            cursorBlinkRunnable?.run()
        }
    }

    fun stopCursorBlinker() {
        cursorBlinkRunnable?.let { cursorBlinkHandler?.removeCallbacks(it) }
        cursorBlinkRunnable = null
    }
}

interface TerminalViewClient {
    fun onSingleTapUp(event: MotionEvent)
    fun onDoubleTap(event: MotionEvent)
}

class TerminalInputConnection(
    private val view: TerminalView
) : android.view.inputmethod.BaseInputConnection(view, true) {

    override fun commitText(text: CharSequence, newCursorPosition: Int): Boolean {
        view.session?.write(text.toString())
        return true
    }

    override fun sendKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            view.session?.write(event.unicodeChar.toChar().toString())
        }
        return true
    }

    override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
        view.session?.write("\u007f")
        return true
    }
}
