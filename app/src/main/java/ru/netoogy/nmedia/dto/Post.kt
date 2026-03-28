package ru.netoogy.nmedia.dto

import android.os.Parcel
import android.os.Parcelable

data class Post (
    val id: Int = 0,
    val author: String = "NoName",
    val published: String = "now",
    val content: String = "",
    val likes: Int = 0,
    val isLiked: Boolean = false,
    val shared: Int = 0,
    val views: Int = 0,
    val videoUrl: String? = null
)
