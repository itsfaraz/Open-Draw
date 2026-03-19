package com.designlife.opendraw.common.utils

import android.content.Context
import android.util.Log
import java.io.*
object FileStorage {

        private const val TAG = "OpenDrawFileManager"
        private const val FOLDER_NAME = "OpenDraw"

        private fun getOrCreateDirectory(context: Context): File {
            val dir = File(context.filesDir, FOLDER_NAME)
            if (!dir.exists()) dir.mkdirs()
            return dir
        }

        fun writeFile(context: Context, json: String, fileName: String): Boolean {
            return try {
                val dir = getOrCreateDirectory(context)
                val targetFile = File(dir, fileName)
                val tempFileName = "$fileName.tmp"

                context.openFileOutput(tempFileName, Context.MODE_PRIVATE).use { fos ->
                    fos.write(json.toByteArray(Charsets.UTF_8))
                    fos.flush()
                }

                val rootTemp = File(context.filesDir, tempFileName)
                if (targetFile.exists()) targetFile.delete()
                val renamed = rootTemp.renameTo(targetFile)
                if (!renamed) throw IOException("Rename failed for $fileName")

                Log.d(TAG, "writeFile OK: $fileName")
                true
            } catch (e: Exception) {
                Log.e(TAG, "writeFile failed: ${e.message}", e)
                false
            } finally {
                try { File(context.filesDir, "$fileName.tmp").delete() } catch (_: Exception) {}
            }
        }

        fun readAllFiles(context: Context): List<String> {
            return try {
                val dir = getOrCreateDirectory(context)
                val fileNames = dir.list() ?: return emptyList()
                val result = mutableListOf<String>()

                for (name in fileNames) {
                    if (name.endsWith(".tmp")) continue
                    val file = File(dir, name)
                    if (!file.isFile || !file.canRead()) {
                        if (!file.canRead()) file.delete() // remove corrupt backup files
                        continue
                    }
                    try {
                        result.add(file.readText(Charsets.UTF_8))
                    } catch (e: Exception) {
                        Log.e(TAG, "Read failed: $name — ${e.message}", e)
                    }
                }

                Log.d(TAG, "readAllFiles: ${result.size} loaded")
                result
            } catch (e: Exception) {
                Log.e(TAG, "readAllFiles failed: ${e.message}", e)
                emptyList()
            }
        }

        fun deleteFile(context: Context, fileName: String): Boolean {
            return try {
                File(getOrCreateDirectory(context), fileName).delete()
            } catch (e: Exception) {
                Log.e(TAG, "deleteFile failed: ${e.message}", e)
                false
            }
        }

}