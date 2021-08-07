package com.manojrai.androidtest.data.model

import android.net.Uri

data class Book(
    val id: Int?,
    val booName: String,
    val authorName: String,
    val price: String,
    val doi: String
) {
    var list = ArrayList<Uri>()
}