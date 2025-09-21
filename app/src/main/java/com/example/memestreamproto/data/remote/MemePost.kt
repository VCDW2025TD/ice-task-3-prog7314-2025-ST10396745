package com.example.memestreamproto.data.remote

data class MemePost(
    val userId: String,
    val title: String,
    val imageUrl: String,
    val tags: List<String>,
    val upvotes: Int = 0
)
