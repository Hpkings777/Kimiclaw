package com.termux.app

import android.app.Activity
import androidx.core.view.GravityCompat
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.termux.app.terminal.TermuxActivityRootView
import com.termux.terminal.TerminalSession
import com.termux.view.TerminalView
import com.termux.view.TerminalViewClient

class TermuxActivity : Activity(), ServiceConnection, TerminalViewClient {

    lateinit var terminalView: TerminalView
    lateinit var rootView: TermuxActivityRootView
    lateinit var drawerLayout: DrawerLayout
    var termuxService: TermuxService? = null
    var isVisible: Boolean = false

    private var serviceBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootView = TermuxActivityRootView(this, null).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setActivity(this@TermuxActivity)
        }

        drawerLayout = DrawerLayout(this).apply {
            addView(rootView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            ))
        }

        terminalView = TerminalView(this, null).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            client = this@TermuxActivity
        }

        rootView.addView(terminalView)

        setContentView(drawerLayout)

        rootView.viewTreeObserver.addOnGlobalLayoutListener(rootView)

        SetupHelper.checkInstallation(this) {
            bindService()
        }
    }

    override fun onStart() {
        super.onStart()
        isVisible = true
        if (!serviceBound) {
            bindService()
        }
    }

    override fun onStop() {
        super.onStop()
        isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            unbindService(this)
            serviceBound = false
        }
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(rootView)
    }

    private fun bindService() {
        val intent = Intent(this, TermuxService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val binder = service as TermuxService.TermuxServiceBinder
        termuxService = binder.getService()
        serviceBound = true

        terminalView.session = termuxService?.currentSession
        terminalView.emulator = termuxService?.currentSession?.emulator

        if (terminalView.session == null) {
            val session = termuxService?.createTermuxSession()
            terminalView.session = session
            terminalView.emulator = session?.emulator
        }

        terminalView.onSessionUpdated()
        terminalView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                terminalView.onKeyDown(keyCode, event)
            }
            true
        }
    }

    override fun onServiceDisconnected(name: ComponentName) {
        termuxService = null
        serviceBound = false
    }

    override fun onSingleTapUp(event: android.view.MotionEvent) {
        terminalView.requestFocus()
    }

    override fun onDoubleTap(event: android.view.MotionEvent) {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun getCurrentSession(): TerminalSession? = termuxService?.currentSession

    fun getTermuxService(): TermuxService? = termuxService
}
