package ru.netoogy.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netoogy.nmedia.dao.PostDao
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.repository.PostRepository

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Int) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                isLiked = !it.isLiked,
                likes = if (it.isLiked) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun repostById(id: Int) {
        dao.repostById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(shared = it.shared + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun getById(id: Int): Post? = dao.getById(id)
}

