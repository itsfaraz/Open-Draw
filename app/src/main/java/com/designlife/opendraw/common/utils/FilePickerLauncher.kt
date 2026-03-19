package com.designlife.opendraw.common.utils

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity

class FilePickerLauncher(
    activity: FragmentActivity,
    private val onFilePicked: (json: String, fileName: String) -> Unit,
    private val onError: (String) -> Unit = {}
) {
    private val launcher: ActivityResultLauncher<Array<String>> =
        activity.registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            if (uri == null) {
                onError("No file selected")
                return@registerForActivityResult
            }
            readFromUri(activity, uri)
        }

    fun launch() {
        // Only allows .json files
        launcher.launch(arrayOf("application/json"))
    }

    private fun readFromUri(context: Context, uri: Uri) {
        try {
            val json = context.contentResolver
                .openInputStream(uri)
                ?.bufferedReader(Charsets.UTF_8)
                ?.use { it.readText() }
                ?: throw Exception("Could not open file")

            // Extract file name from URI
            val fileName = uri.lastPathSegment
                ?.substringAfterLast("/")
                ?: "imported_${System.currentTimeMillis()}.json"

            onFilePicked(json, fileName)
        } catch (e: Exception) {
            onError("Failed to read file: ${e.message}")
        }
    }
}