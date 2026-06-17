package com.moonshot.kimiclaw

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModel
import com.moonshot.kimiclaw.model.ClawAuthData

class MainViewModel : ViewModel() {
    var isServiceConnected by mutableStateOf(false)
    var botState by mutableStateOf<BotState>(BotState.Idle)
    var authData by mutableStateOf<ClawAuthData?>(null)
}

sealed class BotState {
    data object Idle : BotState()
    data object Running : BotState()
    data object Error : BotState()
    data object AuthRequired : BotState()
}

class MainActivity : ComponentActivity() {

    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private var kimiClawService: KimiClawService? = null

    private val notificationPermLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startService(Intent(this, KimiClawService::class.java))
        }
    }

    private val manageStorageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        checkPermissions()
    }

    private val backupFileLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { handleBackupUri(it) }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as KimiClawService.ServiceBinder
            kimiClawService = binder.getService()
            viewModel.isServiceConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            kimiClawService = null
            viewModel.isServiceConnected = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(viewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(this, KimiClawService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        checkPermissions()
    }

    override fun onResume() {
        super.onResume()
        checkNotificationPermission()
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        viewModel.isServiceConnected = false
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                manageStorageLauncher.launch(
                    Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                )
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun handleBackupUri(uri: Uri) {
        val contentResolver = contentResolver
        val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)
    }

    @Composable
    fun MainScreen(viewModel: MainViewModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kimi Claw",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (viewModel.botState) {
                is BotState.Idle -> {
                    Button(onClick = { startBot() }) {
                        Text("Start Bot")
                    }
                }
                is BotState.Running -> {
                    Text("Bot is running...")
                    Button(onClick = { stopBot() }) {
                        Text("Stop Bot")
                    }
                }
                is BotState.Error -> {
                    Text("Error occurred", color = MaterialTheme.colorScheme.error)
                    Button(onClick = { startBot() }) {
                        Text("Retry")
                    }
                }
                is BotState.AuthRequired -> {
                    Text("Authentication required")
                    Button(onClick = { launchAuth() }) {
                        Text("Authenticate")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { launchTerminal() }) {
                Text("Terminal")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { launchSettings() }) {
                Text("Settings")
            }
        }
    }

    private fun startBot() {
        viewModel.botState = BotState.Running
        kimiClawService?.runCommand("bot-start") { result ->
            if (!result.success) {
                viewModel.botState = BotState.Error
            }
        }
    }

    private fun stopBot() {
        viewModel.botState = BotState.Idle
        kimiClawService?.runCommand("bot-stop") {}
    }

    private fun launchAuth() {
        // FIXME: launch authentication flow
    }

    private fun launchTerminal() {
        val intent = packageManager.getLaunchIntentForPackage("com.termux")
        if (intent != null) {
            startActivity(intent)
        } else {
            // Open Termux in-app terminal activity if available
            try {
                val cls = Class.forName("com.termux.app.TermuxActivity")
                startActivity(Intent(this, cls))
            } catch (e: ClassNotFoundException) {
                // Termux not installed
            }
        }
    }

    private fun launchSettings() {
        // FIXME: launch settings activity/sheet
    }
}
