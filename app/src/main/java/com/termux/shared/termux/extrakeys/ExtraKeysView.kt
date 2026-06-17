package com.termux.shared.termux.extrakeys

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.GridLayout
import android.widget.PopupWindow
import com.google.android.material.button.MaterialButton
import com.moonshot.kimiclaw.R
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

data class ExtraButton(
    val id: String,
    val label: Any,
    val popup: ExtraButton? = null
)

data class ExtraButtonState(
    val id: String,
    var isActive: Boolean = false,
    var triggeredByRepeat: Boolean = false,
    val buttons: MutableList<MaterialButton> = mutableListOf()
)

interface ExtraKeysViewClient {
    fun onExtraKey(key: String, ctrl: Boolean, alt: Boolean, shift: Boolean, fn: Boolean)
}

class ExtraKeysView(
    context: Context,
    attrs: AttributeSet?
) : GridLayout(context, attrs) {

    var client: ExtraKeysViewClient? = null
    var buttonTextColor: Int = -1
    var buttonActiveTextColor: Int = -8331542
    var buttonBackgroundColor: Int = 0
    var buttonActiveBackgroundColor: Int = -8421505
    var textAllCaps: Boolean = true
    var longPressTimeout: Int = 400
    var longPressRepeatDelay: Int = 80

    private var specialButtons: Map<String, ExtraButtonState> = emptyMap()
    private var specialButtonKeys: Set<String> = emptySet()
    private var repetitiveKeys: List<String> = emptyList()
    private var scheduledExecutor: ScheduledExecutorService? = null
    private var handler: Handler? = null
    private var longPressRunnable: Runnable? = null
    private var popupWindow: PopupWindow? = null
    private var activeButtonsCount: Int = 0

    init {
        setRepetitiveKeys(defaultRepetitiveKeys)
        updateSpecialButtons()

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ExtraKeysView)
        buttonTextColor = ta.getColor(R.styleable.ExtraKeysView_buttonTextColor, -1)
        buttonActiveTextColor = ta.getColor(R.styleable.ExtraKeysView_buttonActiveTextColor, -8331542)
        buttonBackgroundColor = ta.getColor(R.styleable.ExtraKeysView_buttonBackgroundColor, 0)
        buttonActiveBackgroundColor = ta.getColor(R.styleable.ExtraKeysView_buttonActiveBackgroundColor, -8421505)
        ta.recycle()

        longPressTimeout = ViewConfiguration.getLongPressTimeout()
        longPressRepeatDelay = 80
    }

    private fun createButtonForSpecial(id: String, trackInList: Boolean): MaterialButton? {
        val state = specialButtons[id] ?: return null
        state.isActive = true
        val btn = MaterialButton(context, null, android.R.attr.buttonBarButtonStyle)
        btn.setTextColor(if (state.isActive) buttonActiveTextColor else buttonTextColor)
        if (trackInList) state.buttons.add(btn)
        return btn
    }

    fun handleButtonClick(button: ExtraButton) {
        if (!specialButtonKeys.contains(button.id)) {
            handleKeyPress(button.id, false)
            return
        }
        if (activeButtonsCount <= 0) {
            val state = specialButtons[button.id] ?: return
            state.isActive = !state.isActive
            if (!state.isActive) state.triggeredByRepeat = false
        }
    }

    private fun handleKeyPress(key: String, longPress: Boolean) {
        client?.let { client ->
            if (buttonExtraKeysPattern.matcher(key).matches()) {
                var ctrl = false
                var alt = false
                var shift = false
                var fn = false
                for (part in key.split(" ")) {
                    when (part) {
                        "CONTROL" -> ctrl = true
                        "ALT" -> alt = true
                        "SHIFT" -> shift = true
                        "FN" -> fn = true
                        else -> client.onExtraKey(part, ctrl, alt, shift, fn)
                    }
                    ctrl = false; alt = false; shift = false; fn = false
                }
            } else {
                client.onExtraKey(key, false, false, false, false)
            }
        }
    }

    fun setKeys(keysData: KeysData?) {
        if (keysData == null) return
        specialButtons.values.forEach { it.buttons.clear() }
        removeAllViews()

        val rows = keysData.rows
        rowCount = rows.size
        var maxCols = 0
        rows.forEach { maxCols = maxOf(maxCols, it.size) }
        columnCount = maxCols

        rows.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, button ->
                val isSpecial = specialButtonKeys.contains(button.id)
                val btn = if (isSpecial) {
                    createButtonForSpecial(button.id, true) ?: return
                } else {
                    MaterialButton(context, null, android.R.attr.buttonBarButtonStyle)
                }

                btn.text = button.label.toString()
                btn.setTextColor(buttonTextColor)
                btn.isAllCaps = textAllCaps
                btn.setPadding(0, 0, 0, 0)

                btn.setOnClickListener {
                    if (Settings.System.getInt(context.contentResolver, "haptic_feedback_enabled", 0) != 0) {
                        if (Build.VERSION.SDK_INT >= 28 ||
                            Settings.Global.getInt(context.contentResolver, "zen_mode", 0) != 2
                        ) {
                            btn.performHapticFeedback(3)
                        }
                    }
                    handleButtonClick(button)
                }

                btn.setOnTouchListener { v, event ->
                    handleTouch(v, event, button)
                    true
                }

                val lp = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    setMargins(0, 0, 0, 0)
                    columnSpec = GridLayout.spec(colIdx, GridLayout.FILL, 1f)
                    rowSpec = GridLayout.spec(rowIdx, GridLayout.FILL, 1f)
                }
                btn.layoutParams = lp
                addView(btn)
            }
        }
    }

    private fun handleTouch(v: View, event: MotionEvent, button: ExtraButton) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.setBackgroundColor(buttonActiveBackgroundColor)
                stopRepeating()
                activeButtonsCount = 0

                if (repetitiveKeys.contains(button.id)) {
                    scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
                    scheduledExecutor?.scheduleWithFixedDelay({
                        post { handleKeyPress(button.id, true) }
                    }, longPressTimeout.toLong(), longPressRepeatDelay.toLong(), TimeUnit.MILLISECONDS)
                } else if (specialButtonKeys.contains(button.id)) {
                    val state = specialButtons[button.id]
                    if (state != null && handler == null) {
                        handler = Handler(Looper.getMainLooper())
                    }
                    val runnable = Runnable {
                        if (state?.isActive == true) {
                            state.triggeredByRepeat = true
                            handleKeyPress(button.id, true)
                        }
                    }
                    longPressRunnable = runnable
                    handler?.postDelayed(runnable, longPressTimeout.toLong())
                }
            }
            MotionEvent.ACTION_MOVE -> {
                button.popup?.let { popup ->
                    if (popupWindow == null && event.y < 0f) {
                        stopRepeating()
                        v.setBackgroundColor(buttonBackgroundColor)

                        val popupBtn = if (specialButtonKeys.contains(popup.id)) {
                            createButtonForSpecial(popup.id, false)
                        } else {
                            MaterialButton(context, null, android.R.attr.buttonBarButtonStyle).apply {
                                setTextColor(buttonTextColor)
                            }
                        }

                        if (popupBtn != null) {
                            popupBtn.text = popup.label.toString()
                            popupBtn.isAllCaps = textAllCaps
                            popupBtn.setPadding(0, 0, 0, 0)
                            popupBtn.minHeight = 0
                            popupBtn.minWidth = 0
                            popupBtn.minimumWidth = 0
                            popupBtn.minimumHeight = 0
                            popupBtn.width = v.measuredWidth
                            popupBtn.height = v.measuredHeight
                            popupBtn.setBackgroundColor(buttonActiveBackgroundColor)

                            PopupWindow(this).apply {
                                width = -2
                                height = -2
                                contentView = popupBtn
                                isOutsideTouchable = true
                                isFocusable = false
                                showAsDropDown(v, 0, v.measuredHeight * -2)
                                popupWindow = this
                            }
                        }
                    }
                }
                if (popupWindow != null && event.y > 0f) {
                    v.setBackgroundColor(buttonActiveBackgroundColor)
                    popupWindow?.let {
                        it.contentView = null
                        it.dismiss()
                    }
                    popupWindow = null
                }
            }
            MotionEvent.ACTION_UP -> {
                v.setBackgroundColor(buttonBackgroundColor)
                stopRepeating()
                if (activeButtonsCount == 0 || popupWindow != null) {
                    if (popupWindow == null) {
                        v.performClick()
                    } else {
                        popupWindow?.let {
                            it.contentView = null
                            it.dismiss()
                        }
                        popupWindow = null
                        button.popup?.let { handleButtonClick(it) }
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                v.setBackgroundColor(buttonBackgroundColor)
                stopRepeating()
            }
        }
    }

    private fun stopRepeating() {
        scheduledExecutor?.shutdownNow()
        scheduledExecutor = null
        longPressRunnable?.let { handler?.removeCallbacks(it) }
        longPressRunnable = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopRepeating()
    }

    private fun updateSpecialButtons() {
        specialButtonKeys = specialButtons.keys
    }

    fun setRepetitiveKeys(keys: List<String>?) {
        repetitiveKeys = keys ?: defaultRepetitiveKeys
    }

    fun getRepetitiveKeys(): List<String>? = repetitiveKeys

    companion object {
        private val buttonExtraKeysPattern = Regex("^([A-Z]+ )+[a-z0-9]")
        val defaultRepetitiveKeys = listOf("KEYBOARD", "UP", "DOWN", "LEFT", "RIGHT")
    }
}

data class KeysData(
    val rows: List<List<ExtraButton>>
)
