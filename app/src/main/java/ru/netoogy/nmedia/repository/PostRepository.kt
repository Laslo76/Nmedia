package ru.netoogy.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netoogy.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Int)
    fun repostById(id: Int)

    fun removeById(id: Int)

    fun save(post: Post)
}

