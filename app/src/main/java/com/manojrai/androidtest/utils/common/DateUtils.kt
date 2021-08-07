package com.manojrai.androidtest.utils.common

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getDate(long: Long): String? {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        return sdf.format(Date(long))
    }
}