package ru.netoogy.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.repository.PostRepository
import ru.netoogy.nmedia.repository.PostRepositoryInMemoryImpl
import kotlin.Int

private val emptyPost = Post()

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()

    val edited = MutableLiveData(emptyPost)
    fun likeById(id: Int) = repository.likeById(id)
    fun repostById(id: Int) = repository.repostById(id)
    fun removeById(id: Int) = repository.removeById(id)
    fun edit(post: Post) {
        edited.value = post
    }
    fun save(content: String) {
        edited.value?.let { post ->
            val trimmed = content.trim()

            if (post.content != trimmed) {
                repository.save(
                    post.copy(content = trimmed)
                )
            }

            edited.value = emptyPost
        }
    }

    fun cancel() {
        edited.value = emptyPost
    }
}