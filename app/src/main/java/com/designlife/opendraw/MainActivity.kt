package com.designlife.opendraw

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.database.CursorWindow
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.designlife.opendraw.common.utils.PermissionHandler
import com.designlife.opendraw.ui.theme.updateSystemColor
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isStorageWritePermissionGranted = false
    private var isStorageReadPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        if (!PermissionHandler.checkAllPermissions(this)){
            permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isStorageWritePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isStorageWritePermissionGranted
                isStorageReadPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isStorageReadPermissionGranted
            }
            requestPermissions()
        }
        resizeCursorWindow()
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

    }

    private fun resizeCursorWindow() {
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun requestPermissions() {
        isStorageWritePermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isStorageReadPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val permissionRequestList = ArrayList<String>()

        if (!isStorageWritePermissionGranted) {
            permissionRequestList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!isStorageReadPermissionGranted) {
            permissionRequestList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionRequestList.isNotEmpty()) {
            permissionLauncher.launch(permissionRequestList.toTypedArray())
        }
    }

    private fun observeDarkModeChanges() {
        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            isDarkModeTheme.value = isSystemInDarkTheme()
            updateSystemColor(isDarkModeTheme.value)
//            SettingViewModel.updateDarkModeSetting(isDarkModeTheme.value)
        }
    }

    companion object{
        val isDarkModeTheme : MutableState<Boolean> = mutableStateOf(false);
    }
}