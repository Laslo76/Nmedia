package ru.netoogy.nmedia.dto

data class Post (
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int = 0,
    var isLiked: Boolean,
    var shared: Int = 999999,
    var views: Int = 999

)
