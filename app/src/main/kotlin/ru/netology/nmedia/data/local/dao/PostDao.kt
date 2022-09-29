package ru.netology.nmedia.data.local.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Url
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.domain.models.PostModel

@Dao
interface PostDao {

    // get all

    @Query("SELECT * FROM posts WHERE removed = 0")
    fun getAll(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts where id = 0")
    suspend fun getAllUnsent() : List<PostEntity>

    @Query("SELECT id FROM posts WHERE id != 0")
    suspend fun getAllSentIds() : List<Long>

    @Query("SELECT id FROM posts WHERE removed = 1")
    suspend fun getAllRemovedIds() : List<Long>

    @Query("SELECT * FROM posts LIMIT :count OFFSET :key")
    suspend fun getBefore(key: Long, count: Int) : List<PostEntity>

    @Query("SELECT * FROM posts LIMIT :count")
    suspend fun getLatest(count: Int) : List<PostEntity>

    // get by key

    @Query("SELECT * FROM posts WHERE `key` = :key")
    suspend fun getByKey(key: Long) : PostEntity

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getById(id: Long): PostEntity

    // insert

    @Insert(onConflict = IGNORE)
    suspend fun insert(postEntity: PostEntity): Long

    @Insert
    suspend fun insert(posts: List<PostEntity>)

    @Transaction
    suspend fun save (postEntity: PostEntity) : PostEntity {
        return getByKey(insert(postEntity))
    }

    // update

    @Query("UPDATE posts SET author_avatar = :url WHERE `key` = :key")
    suspend fun setAvatar(key: Long, url: String)

    @Query("UPDATE posts SET author_name = :name WHERE `key` = :key")
    suspend fun setAuthorName(key: Long, name: String)

    @Query("UPDATE posts SET attachment_url = :url WHERE `key` = :key")
    suspend fun setMediaUrl(key: Long, url: String)

    @Query("UPDATE posts SET published = :dateTime WHERE `key` = :key")
    suspend fun setServerDateTime(key: Long, dateTime: Long)

    @Insert(onConflict = REPLACE)
    suspend fun replace(postEntity: PostEntity)

    @Query("UPDATE posts SET content = :content, synced = 0 WHERE `key` = :key")
    suspend fun updateContent(key: Long, content: String): Int

    @Query("UPDATE posts SET content = :content, synced = 0 WHERE id = :id")
    suspend fun updateContentById(id: Long, content: String)

    @Transaction
    suspend fun updateContentByKey(key: Long, content: String) : PostEntity {
        updateContent(key,content)
        return getByKey(key)
    }

    @Query(
        """
        UPDATE posts SET
        likes = likes + CASE WHEN liked_by_me THEN -1 ELSE 1 END,
        liked_by_me = CASE WHEN liked_by_me THEN 0 ELSE 1 END,
        synced = 0
        WHERE `key` = :key
        """
    )
    suspend fun like(key: Long): Int

    @Transaction
    suspend fun likeByKey (key: Long) : Long {
        like(key)
        return getByKey(key).id
    }

    @Query(
        """
        UPDATE posts SET
        likes = likes + CASE WHEN liked_by_me THEN -1 ELSE 1 END,
        liked_by_me = CASE WHEN liked_by_me THEN 0 ELSE 1 END,
        synced = 0
        WHERE id = :id
        """
    )
    suspend fun likeById (id: Long)


    // set states

    @Query("UPDATE posts SET read = 1 WHERE `key` = :key")
    suspend fun setRead(key: Long)

    @Query("UPDATE posts SET removed = 1 WHERE `key` = :key")
    suspend fun setRemoved(key: Long)

    @Transaction
    suspend fun setRemovedByKey(key: Long) : PostEntity {
        setRemoved(key)
        return getByKey(key)
    }

    @Query("UPDATE posts SET synced = 1 WHERE id = :id")
    suspend fun setSyncedById (id: Long)

    @Query("UPDATE posts SET synced = 0 WHERE id = :id")
    suspend fun unsetSynced (id: Long)

    @Query("UPDATE posts SET id = :serverId WHERE `key` = :key")
    suspend fun setServerId(key: Long, serverId: Long)

    @Query("UPDATE posts SET state = :state WHERE `key` = :key")
    suspend fun setState(key: Long, state: PostModel.State)
    // remove

    @Query("DELETE FROM posts WHERE `key` = :key")
    suspend fun removeByKey(key: Long): Int

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun removeById(id: Long): Int


}
