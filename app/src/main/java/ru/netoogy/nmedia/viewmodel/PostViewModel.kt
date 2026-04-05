package ru.netoogy.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.repository.PostRepository
import ru.netoogy.nmedia.repository.PostRepositoryInFileImpl
import kotlin.Int

private val emptyPost = Post()

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryInFileImpl(application)
    val data = repository.getAll()
    var viewRecordID: Int = 0
    private val _edited = MutableLiveData<Post?>(emptyPost)
    val edited: LiveData<Post?> = _edited
    fun getById(id: Int) = repository.getById(id)
    fun likeById(id: Int) = repository.likeById(id)
    fun repostById(id: Int) = repository.repostById(id)
    fun removeById(id: Int) = repository.removeById(id)
    fun edit(post: Post) {
        _edited.value = post
    }
    fun view(post: Post) {
        viewRecordID = post.id
    }
    fun save(content: String) {
        _edited.value?.let { post ->
            val trimmed = content.trim()

            if (post.content != trimmed) {
                repository.save(
                    post.copy(content = trimmed)
                )
            }
        }
        cancel()
    }

    fun cancel() {
        _edited.value = emptyPost
    }
}