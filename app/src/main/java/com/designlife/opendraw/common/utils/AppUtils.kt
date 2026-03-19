package com.designlife.opendraw.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object AppUtils {
    fun getFormattedTimestamp(epoch : Long): String {
        val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        return formatter.format(Date(epoch))
    }
}

