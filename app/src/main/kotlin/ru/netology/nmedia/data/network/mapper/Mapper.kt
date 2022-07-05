package ru.netology.nmedia.data.network.mapper

import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.db.mapper.toThumbData
import ru.netology.nmedia.data.network.dto.PostResponse
import ru.netology.nmedia.data.youtubeApi.RetrofitInstance
import ru.netology.nmedia.domain.models.FirstUrl
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.ThumbData

fun PostResponse.toModel(): Post {

    val url = parseUrl(content)

    var firstUrl: FirstUrl? = null

    val load = Thread {
        url?.let { url ->
            val apiResponse = RetrofitInstance.service.getThumbDataEntity(url).execute()
            firstUrl = apiResponse.body()?.let {
                FirstUrl(
                    url = url,
                    thumbData = it.toThumbData()
                )
            }
        }
    }

    load.start()
    load.join()

    return Post(
        id = id,
        author = author,
        content = content,
        dateTime = published,
        isLiked = likedByMe,
        likes = likes,
        firstUrl = firstUrl
    )
}

fun parseUrl(text: String): String? {
    val regexp = "(http|https)://[\\w]*\\.\\S*".toRegex()
    return regexp.find(text)?.groups?.first()?.value
}
