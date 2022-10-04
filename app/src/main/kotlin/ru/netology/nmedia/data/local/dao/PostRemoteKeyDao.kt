package ru.netology.nmedia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.netology.nmedia.data.local.entity.PostRemoteKeyEntity

@Dao
interface PostRemoteKeyDao {

    @Query("SELECT max(`key`) FROM PostRemoteKeyEntity")
    suspend fun max() : Long?

    @Query("SELECT min(`key`) FROM PostRemoteKeyEntity")
    suspend fun min () : Long?

    @Insert(onConflict = REPLACE)
    suspend fun insert(postRemoteKeyEntity: PostRemoteKeyEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insert(postRemoteKeyEntity: List<PostRemoteKeyEntity>)

    @Query("DELETE FROM PostRemoteKeyEntity")
    suspend fun clear()
}