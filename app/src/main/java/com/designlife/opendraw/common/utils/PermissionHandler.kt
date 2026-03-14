package com.designlife.opendraw.common.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

object PermissionHandler {
    val storageWritePermissionString = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val storageReadPermissionString = Manifest.permission.READ_EXTERNAL_STORAGE


    fun checkAllPermissions(context: Context) : Boolean{
        val storageWritePermissionResult = context.checkCallingOrSelfPermission(storageWritePermissionString)
        val storageWritePermission = storageWritePermissionResult == PackageManager.PERMISSION_GRANTED

        val storageReadPermissionResult = context.checkCallingOrSelfPermission(storageReadPermissionString)
        val storageReadPermission = storageReadPermissionResult == PackageManager.PERMISSION_GRANTED

        return storageWritePermission && storageReadPermission
    }

}