package ru.netoogy.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netoogy.nmedia.repository.PostRepository
import ru.netoogy.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    fun like() = repository.like()
    fun repost() = repository.repost()

}