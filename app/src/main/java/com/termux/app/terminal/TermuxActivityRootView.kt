package com.termux.app.terminal

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.termux.app.TermuxActivity

class TermuxActivityRootView(
    context: Context,
    attrs: AttributeSet?
) : LinearLayout(context, attrs), ViewTreeObserver.OnGlobalLayoutListener {

    companion object {
        var statusBarHeight: Int = 0
    }

    var activity: TermuxActivity? = null
    var pendingBottomMargin: Int? = null
    private var lastSmallMarginTime: Long = 0
    private var lastLargeMarginTime: Long = 0
    var isRootViewLoggingEnabled: Boolean = false

    init {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
    }

    fun assignActivity(termuxActivity: TermuxActivity) {
        this.activity = termuxActivity
    }

    fun setIsRootViewLoggingEnabled(enabled: Boolean) {
        this.isRootViewLoggingEnabled = enabled
    }

    override fun onGlobalLayout() {
        val termuxActivity = activity ?: return
        val terminalView = termuxActivity.terminalView ?: return
        if (!terminalView.isShown) return

        val rect = Rect()
        terminalView.getWindowVisibleDisplayFrame(rect)

        val activityHeight = context.resources.displayMetrics.heightPixels
        val navBarHeight = activityHeight - rect.bottom

        if (isRootViewLoggingEnabled) {
            android.util.Log.d("TermuxActivityRootView",
                "windowVisibleDisplayFrame: $rect, navBarHeight: $navBarHeight")
        }

        val layoutParams = layoutParams as? android.widget.FrameLayout.LayoutParams ?: return

        if (navBarHeight > 0 && navBarHeight != layoutParams.bottomMargin) {
            val now = System.currentTimeMillis()
            if (navBarHeight < layoutParams.bottomMargin && now - lastSmallMarginTime < 40) {
                return
            }
            if (navBarHeight > layoutParams.bottomMargin && now - lastLargeMarginTime < 40) {
                return
            }

            if (navBarHeight < layoutParams.bottomMargin) {
                lastSmallMarginTime = now
                pendingBottomMargin = null
            } else {
                lastLargeMarginTime = now
                pendingBottomMargin = navBarHeight
            }

            layoutParams.setMargins(0, 0, 0, navBarHeight)
            setLayoutParams(layoutParams)
        } else if (navBarHeight == 0 && layoutParams.bottomMargin != 0) {
            layoutParams.setMargins(0, 0, 0, 0)
            setLayoutParams(layoutParams)
            pendingBottomMargin = null
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        pendingBottomMargin?.let { margin ->
            val lp = layoutParams as? android.widget.FrameLayout.LayoutParams ?: return
            lp.setMargins(0, 0, 0, margin)
            setLayoutParams(lp)
            pendingBottomMargin = null
            requestLayout()
        }
    }
}
