package ru.netology.nmedia.data

import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.local.dao.UnsentPostDao
import ru.netology.nmedia.data.local.entity.UnsentPostEntity
import ru.netology.nmedia.data.mapper.toModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.UnsentPostRepository
import java.util.*

class UnsentPostRepositoryImpl(private val dao: UnsentPostDao) : UnsentPostRepository {

    override fun save(id: Long, content: String): Post {

        return if (id == 0L) {
            dao.save(
                UnsentPostEntity(
                    id = if (dao.getFirstId() >= 0) - 1 else (dao.getFirstId() - 1),
                    author = AUTHOR,
                    content = content,
                    published = Date().time / 1000
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

