package ru.netology.nmedia.data.local.dao

import androidx.room.*
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.domain.models.Post

@Dao
interface PostDao {

    @Query("UPDATE posts SET syncStatus = 1 WHERE id = :id")
    fun syncData(id: Long)

    @Query("SELECT * FROM posts")
    fun getAll(): List<PostEntity>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getById(id: Long) : PostEntity

    @Query("DELETE FROM posts WHERE id = :id")
    fun remove(id: Long): Int

    @Insert
    fun insert(postEntity: PostEntity): Long

    @Query("UPDATE posts SET content = :content, syncStatus = 0 WHERE id = :id")
    fun updateContent(id: Long, content: String): Int

    @Transaction
    fun save (postEntity: PostEntity) : PostEntity {
        return getById(insert(postEntity))
    }

    @Transaction
    fun update(id: Long, content: String) : PostEntity {
        updateContent(id,content)
        return getById(id)
    }

    @Query("SELECT id FROM posts")
    fun getLocalPostIds() : List<Long>

    @Query("SELECT isLiked FROM posts WHERE id = :id")
    fun isLiked(id: Long) : Int

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

    @Transaction
    fun likeById (id: Long) : Boolean {
        like(id)
        return isLiked(id) == 1
    }

    @Query("SELECT * FROM posts where syncStatus = 0")
    fun getNotSynced() : List<PostEntity>
}
