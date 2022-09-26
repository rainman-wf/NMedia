package ru.netology.nmedia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.local.entity.PostEntity

@Database(
    entities = [
        PostEntity::class],
    version = 1
)
abstract class AppDb : RoomDatabase() {

    abstract val postDao: PostDao

}