package ru.netoogy.nmedia.dao

import ru.netoogy.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun likeById(id: Int)
    fun repostById(id: Int)
    fun removeById(id: Int)
    fun save(post: Post): Post
    fun getById(id: Int): Post?

}
