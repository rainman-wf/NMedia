package ru.netology.nmedia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.data.local.entity.UnsentPostEntity

@Dao
interface UnsentPostDao {

    @Query("SELECT * FROM unsentPosts")
    fun getAll(): List<UnsentPostEntity>

    @Query("DELETE FROM unsentPosts WHERE id = :id")
    fun remove(id: Long): Int

    @Insert
    fun insert(unsentPostEntity: UnsentPostEntity): Long

    @Query("UPDATE unsentPosts SET content = :content WHERE id = :id")
    fun update(id: Long, content: String): Int

    @Query("SELECT * FROM unsentPosts WHERE id = :id")
    fun getById(id: Long): UnsentPostEntity
}