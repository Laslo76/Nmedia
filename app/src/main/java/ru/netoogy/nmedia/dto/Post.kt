package ru.netoogy.nmedia.dto

data class Post (
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int = 0,
    val isLiked: Boolean,
    val shared: Int = 999999,
    val views: Int = 999

)
