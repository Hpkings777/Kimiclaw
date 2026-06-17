package com.termux.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.termux.shared.termux.shell.am.TermuxAmSocketServer
import java.io.File

class TermuxApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this

        registerActivityLifecycleCallbacks(TermuxActivityLifecycleCallback())

        updateConnectivityStatus()
        registerConnectivityCallback()

        val appsDir = File(filesDir, "apps/com.moonshot.kimiclaw")
        appsDir.mkdirs()

        TermuxAmSocketServer.start(this)
    }

    private fun updateConnectivityStatus() {
        try {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetwork
            val caps = cm.getNetworkCapabilities(network)
            isOnline = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } catch (_: Exception) {}
    }

    private fun registerConnectivityCallback() {
        try {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            cm.registerNetworkCallback(request, ConnectivityCallback())
        } catch (_: Exception) {}
    }

    companion object {
        lateinit var appContext: Context
            private set

        @Volatile
        var isOnline: Boolean = false
    }
}

class ConnectivityCallback : android.net.ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: android.net.Network) {
        TermuxApplication.isOnline = true
    }

    override fun onLost(network: android.net.Network) {
        TermuxApplication.isOnline = false
    }
}

class TermuxActivityLifecycleCallback : android.app.Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: android.app.Activity, bundle: android.os.Bundle?) {}
    override fun onActivityStarted(activity: android.app.Activity) {}
    override fun onActivityResumed(activity: android.app.Activity) {}
    override fun onActivityPaused(activity: android.app.Activity) {}
    override fun onActivityStopped(activity: android.app.Activity) {}
    override fun onActivitySaveInstanceState(activity: android.app.Activity, bundle: android.os.Bundle) {}
    override fun onActivityDestroyed(activity: android.app.Activity) {}
}
