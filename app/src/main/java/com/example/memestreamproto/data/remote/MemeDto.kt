package com.example.memestreamproto.data.remote

data class MemeDto(
    val _id: String,
    val userId: String,
    val title: String,
    val imageUrl: String,
    val tags: List<String>? = emptyList(),
    val upvotes: Int? = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val __v: Int? = null
)

data class MemeListDto(
    val total: Int? = null,
    val count: Int? = null,
    val items: List<MemeDto>
)
