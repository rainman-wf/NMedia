package ru.netology.nmedia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.local.dao.RemovedIdsDao
import ru.netology.nmedia.data.local.dao.UnsentPostDao
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.local.entity.RemovedIdsEntity
import ru.netology.nmedia.data.local.entity.UnsentPostEntity

@Database(
    entities = [
        PostEntity::class,
        UnsentPostEntity::class,
        RemovedIdsEntity::class],
    version = 1
)
abstract class AppDb : RoomDatabase() {

    abstract val unsentPostDao: UnsentPostDao
    abstract val postDao: PostDao
    abstract val removedIdsDao: RemovedIdsDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .allowMainThreadQueries()
                .build()
    }
}