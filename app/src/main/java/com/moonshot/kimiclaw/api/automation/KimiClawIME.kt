package com.moonshot.kimiclaw.api.automation

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch

class KimiClawIME : InputMethodService() {

    companion object {
        @Volatile
        var currentInstance: WeakReference<KimiClawIME>? = null

        @Volatile
        var pendingLatch: CountDownLatch? = null
    }

    var imeActions: Int = 0
    var imeOptions: Int = 0

    override fun onCreateInputView(): View? = null

    override fun onStartInput(editorInfo: EditorInfo?, restarting: Boolean) {
        super.onStartInput(editorInfo, restarting)
        imeOptions = editorInfo?.imeOptions ?: 0
        imeActions = imeOptions and EditorInfo.IME_MASK_ACTION
        // FIXME: was AbstractC2240fx.J(this.imeActions) - unknown utility
        currentInstance = WeakReference(this)
        pendingLatch?.countDown()
    }

    override fun onDestroy() {
        if (currentInstance?.get() == this) {
            currentInstance = null
        }
        super.onDestroy()
    }
}
