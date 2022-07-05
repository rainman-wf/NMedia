package ru.netology.nmedia.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.netology.nmedia.data.db.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun getAll(): LiveData<List<PostEntity>>

    @Query("DELETE FROM posts WHERE id = :id")
    fun remove(id: Long): Int

    @Insert
    fun insert(postEntity: PostEntity): Long

    @Query(
        """
        UPDATE posts SET
        likes = likes + CASE WHEN isLiked THEN -1 ELSE 1 END,
        isLiked = CASE WHEN isLiked THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun like(id: Long): Int

    @Query(
        """
        UPDATE posts SET 
        shares = shares + 1 
        WHERE id = :id
        """
    )
    fun share(id: Long): Int

    @Update
    fun update(postEntity: PostEntity): Int

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getById(id: Long) : PostEntity

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getObservableById(id: Long) : LiveData<PostEntity>
}
