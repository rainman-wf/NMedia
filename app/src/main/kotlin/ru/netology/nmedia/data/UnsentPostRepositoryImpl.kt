package ru.netology.nmedia.data

import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.local.dao.UnsentPostDao
import ru.netology.nmedia.data.local.entity.UnsentPostEntity
import ru.netology.nmedia.data.local.mapper.toPost
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.UnsentPostRepository
import ru.netology.nmedia.domain.usecase.params.NewPostParam
import java.util.*

class UnsentPostRepositoryImpl(private val dao: UnsentPostDao) : UnsentPostRepository {

    override fun save(newPostParam: NewPostParam): Post {
        val id = dao.insert(
            UnsentPostEntity(
                author = newPostParam.author,
                content = newPostParam.content,
                published = Date().time
            )
        )
        log(id)
        return getById(id).toPost()
    }

    override fun remove(id: Long): Int {
        return dao.remove(id)
    }

    override fun getAllUnsent(): List<Post> {
        return dao.getAll().map { it.toPost() }
    }

    private fun getById(id: Long): UnsentPostEntity {
        return dao.getById(id)
    }
}

