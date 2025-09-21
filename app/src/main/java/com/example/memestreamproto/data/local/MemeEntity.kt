package com.example.memestreamproto.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memes")
data class MemeEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val imageUrl: String,
    val tags: List<String>,
    val upvotes: Int,
    val createdAt: Long,
    val pendingSync: Boolean
)
