package ru.netoogy.nmedia.repository

import androidx.lifecycle.map
import ru.netoogy.nmedia.dao.PostDao
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.entity.PostEntity
import kotlin.Int

class PostRepositoryRoomImpl(
    private val dao: PostDao,
) : PostRepository {

    override fun getAll() = dao.getAll().map { list ->
        list.map {
            it.toDto()
        }
    }

    override fun likeById(id: Int) {
        dao.likeById(id)
    }

    override fun repostById(id: Int) {
        dao.repostById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun getById(id: Int): Post? = dao.getById(id)?.toDto()

    override fun removeById(id: Int) {
        dao.removeById(id)
    }
}