package ru.netology.nmedia.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.data.local.entity.RemovedIdsEntity

@Dao
interface RemovedIdsDao {

    @Insert
    fun insert(removedIdsEntity: RemovedIdsEntity)

    @Delete
    fun remove(removedIdsEntity: RemovedIdsEntity)

    @Query("SELECT id FROM removed")
    fun getAll() : List<Long>
}