package ru.netology.nmedia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.local.dao.PostRemoteKeyDao
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.local.entity.PostRemoteKeyEntity

@Database(
    entities = [
        PostEntity::class,
        PostRemoteKeyEntity::class],
    version = 1
)
abstract class AppDb : RoomDatabase() {

    abstract val postDao: PostDao
    abstract val postRemoteKeyDao: PostRemoteKeyDao

}