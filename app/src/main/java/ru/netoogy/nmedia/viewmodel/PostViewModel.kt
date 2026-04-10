package ru.netoogy.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netoogy.nmedia.db.AppDb
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.repository.PostRepository
import ru.netoogy.nmedia.repository.PostRepositoryRoomImpl
import kotlin.Int

private val emptyPost = Post()

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryRoomImpl(
        AppDb.getInstance(application).postDao
    )
    val data = repository.getAll()
    val edited = MutableLiveData(emptyPost)
    var viewRecordID: Int = 0
    var unSavedContent: String = ""
    fun save(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text) {
                repository.save(it.copy(content = text))
            }
        }
        edited.value = emptyPost
    }

    fun likeById(id: Int) = repository.likeById(id)

    fun removeById(id: Int) = repository.removeById(id)

    fun getById(id: Int) = repository.getById(id)

    fun repostById(id: Int) = repository.repostById(id)

    fun edit(post: Post) {
        edited.value = post
    }
    fun view(post: Post) {
        viewRecordID = post.id
    }

    fun cancel() {
        edited.value = emptyPost
    }
}