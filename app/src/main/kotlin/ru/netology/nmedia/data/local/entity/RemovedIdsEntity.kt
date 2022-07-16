package ru.netology.nmedia.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "removed")
data class RemovedIdsEntity(@PrimaryKey @ColumnInfo(name = "id") val id: Long)