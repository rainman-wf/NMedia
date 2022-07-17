package ru.netology.nmedia.data

import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.local.dao.UnsentPostDao
import ru.netology.nmedia.data.local.entity.UnsentPostEntity
import ru.netology.nmedia.data.mapper.toModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.UnsentPostRepository
import java.time.OffsetDateTime
import java.util.*
import kotlin.time.Duration.Companion.seconds

class UnsentPostRepositoryImpl(private val dao: UnsentPostDao) : UnsentPostRepository {

    override fun save(id: Long, content: String): Post {

        return if (id == 0L) {
            dao.save(
                UnsentPostEntity(
                    author = AUTHOR,
                    content = content,
                    published = OffsetDateTime.now().toEpochSecond()
                )
            ).toModel()
        } else {
            log(id)
            val entity = dao.update(id, content)
            log(entity)
            entity.toModel()
        }
    }

    override fun remove(id: Long): Int {
        return dao.remove(id)
    }

    override fun getAll(): List<Post> {
        return dao.getAll().map { it.toModel() }
    }

    override fun getById(id: Long): Post {
        return dao.getById(id).toModel()
    }
}

