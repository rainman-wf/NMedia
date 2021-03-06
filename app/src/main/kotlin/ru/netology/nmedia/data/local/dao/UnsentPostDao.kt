package ru.netology.nmedia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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
    fun updateContent(id: Long, content: String): Int

    @Query("SELECT * FROM unsentPosts WHERE id = :id")
    fun getById(id: Long): UnsentPostEntity

    @Transaction
    fun save(unsentPostEntity: UnsentPostEntity) : UnsentPostEntity {
        return getById(insert(unsentPostEntity))
    }

    @Transaction
    fun update(id: Long, content: String) : UnsentPostEntity {
        updateContent(id, content)
        return getById(id)
    }

    @Query("SELECT id FROM unsentPosts LIMIT 1")
    fun getFirstId() : Long
}