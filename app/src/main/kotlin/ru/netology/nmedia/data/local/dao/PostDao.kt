package ru.netology.nmedia.data.local.dao

import androidx.room.*
import ru.netology.nmedia.data.local.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun getAll(): List<PostEntity>

    @Query("DELETE FROM posts WHERE id = :id")
    fun remove(id: Long): Int

    @Insert
    fun insert(postEntity: PostEntity): Long

    @Query(
        """
        UPDATE posts SET
        likes = likes + CASE WHEN isLiked THEN -1 ELSE 1 END,
        isLiked = CASE WHEN isLiked THEN 0 ELSE 1 END,
        syncStatus = 0
        WHERE id = :id
        """
    )
    fun like(id: Long): Int

    @Query("UPDATE posts SET content = :content, syncStatus = 0 WHERE id = :id")
    fun _update(id: Long, content: String): Int

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getById(id: Long) : PostEntity

    @Query("UPDATE posts SET syncStatus = 1 WHERE id = :id")
    fun syncData(id: Long)

    @Transaction
    fun save (postEntity: PostEntity) : PostEntity {
        return getById(insert(postEntity))
    }

    @Transaction
    fun update(id: Long, content: String) : PostEntity {
        _update(id,content)
        return getById(id)
    }
}
