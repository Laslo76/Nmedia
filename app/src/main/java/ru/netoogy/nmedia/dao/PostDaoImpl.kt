package ru.netoogy.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netoogy.nmedia.dto.Post
import ru.netoogy.nmedia.dao.PostDao

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARED} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEWS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIDEO_URL} TEXT
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKED_BY_ME = "isLiked"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_SHARED = "shared"
        const val COLUMN_VIEWS = "views"
        const val COLUMN_VIDEO_URL = "videoURL"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKES,
            COLUMN_SHARED,
            COLUMN_VIEWS,
            COLUMN_VIDEO_URL
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun likeById(id: Int) {
        db.execSQL(
            """
           UPDATE posts SET
               likes = likes + CASE WHEN isLiked THEN -1 ELSE 1 END,
               isLiked = CASE WHEN isLiked THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun repostById(id: Int) {
        db.execSQL(
            """
           UPDATE posts SET 
               shared = shared + 1
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Int) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_AUTHOR, "Me")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, "now")
        }
        val id = if (post.id != 0) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun getById(id: Int): Post? {
        val posts = mutableListOf<Post>()

        // 1. Создаем условие выборки (аналог WHERE id = ?)
        val selection = "${PostColumns.COLUMN_ID} = ?"

        // 2. Передаем значение id в качестве аргумента.
        // Это защищает от SQL-инъекций и проблем с форматированием.
        val selectionArgs = arrayOf(id.toString())

        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            selection,
            selectionArgs,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return if (posts.isNotEmpty()) posts[0] else null
    }


    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                isLiked = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                shared = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARED)),
                videoUrl = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO_URL))
            )
        }
    }
}
