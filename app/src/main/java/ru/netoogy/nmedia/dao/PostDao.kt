package ru.netoogy.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ru.netoogy.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("Select * from PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query("""
           UPDATE PostEntity SET
               likes = likes + CASE WHEN isLiked THEN -1 ELSE 1 END,
               isLiked = CASE WHEN isLiked THEN 0 ELSE 1 END
           WHERE id = :id
        """)
    fun likeById(id: Int)

    @Query("""
           UPDATE PostEntity SET 
               shared = shared + 1
           WHERE id = :id;
        """)
    fun repostById(id: Int)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Int)

    @Upsert
    fun save(post: PostEntity)

    @Query("SELECT * FROM PostEntity WHERE id = :id")
    fun getById(id: Int): PostEntity?

}
