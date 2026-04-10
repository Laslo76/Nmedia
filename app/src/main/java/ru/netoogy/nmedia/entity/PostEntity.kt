package ru.netoogy.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netoogy.nmedia.dto.Post

@Entity
data class PostEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int = 0,
    val isLiked: Boolean,
    val shared: Int = 0,
    val views: Int = 0,
    val videoUrl: String? = ""
) {
    fun toDto() = Post(id, author, published, content, likes, isLiked, shared, views, videoUrl)

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            dto.id,
            dto.author,
            dto.published,
            dto.content,
            dto.likes,
            dto.isLiked,
            dto.shared,
            dto.views,
            dto.videoUrl)
    }
}