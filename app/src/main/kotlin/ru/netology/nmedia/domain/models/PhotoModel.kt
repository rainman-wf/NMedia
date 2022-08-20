package ru.netology.nmedia.domain.models

import android.net.Uri
import java.io.File

data class PhotoModel(
    val uri: Uri? = null,
    val file: File? = null,
)
