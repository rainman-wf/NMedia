package ru.netology.nmedia.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.netology.nmedia.data.local.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id DESC ")
    fun getPagingSource(): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun getById(id: Long) : PostEntity

    @Insert(onConflict = REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun removeById(id: Long): Int

    @Query("DELETE FROM posts")
    suspend fun clear()

    @Query(
        """
        UPDATE posts SET
        likes = likes + CASE WHEN liked_by_me THEN -1 ELSE 1 END,
        liked_by_me = CASE WHEN liked_by_me THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    suspend fun like(id: Long): Int
}