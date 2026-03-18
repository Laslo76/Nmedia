package ru.netoogy.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netoogy.nmedia.repository.PostRepository
import ru.netoogy.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    fun likeById(id: Int) = repository.likeById(id)
    fun repostById(id: Int) = repository.repostById(id)
    fun removeById(id: Int) = repository.removeById(id)

}