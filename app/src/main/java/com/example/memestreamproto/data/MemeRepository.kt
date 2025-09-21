package com.example.memestreamproto.data

import android.content.Context
import com.example.memestreamproto.data.local.MemeDatabase
import com.example.memestreamproto.data.local.MemeEntity
import com.example.memestreamproto.data.local.Converters
import com.example.memestreamproto.data.remote.ApiClient
import com.example.memestreamproto.data.remote.MemePost
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.UUID

class MemeRepository(ctx: Context) {

    private val dao = MemeDatabase.get(ctx).memeDao()
    private val api = ApiClient.api

    fun observeAll(): Flow<List<MemeEntity>> = dao.observeAll()

    suspend fun refreshFromServer(userId: String?) {
        val resp = api.getMemes(userId) // your interface already supports ?userId=
        val mapped = resp.items.map { dto ->
            MemeEntity(
                id = dto._id,                         // match your MemeDto names
                userId = dto.userId,
                title = dto.title,
                imageUrl = dto.imageUrl,
                tags = dto.tags ?: emptyList(),       // dto.tags likely nullable
                upvotes = dto.upvotes ?: 0,
                createdAt = parseMillis(dto.createdAt),
                pendingSync = false
            )
        }
        dao.upsertAll(mapped)
    }

    suspend fun createLocalMeme(
        userId: String,
        title: String,
        url: String,
        tags: List<String>
    ) {
        val localId = "local-" + UUID.randomUUID()
        val entity = MemeEntity(
            id = localId,
            userId = userId,
            title = title,
            imageUrl = url,
            tags = tags,
            upvotes = 0,
            createdAt = System.currentTimeMillis(),
            pendingSync = true
        )
        dao.upsert(entity)
    }

    suspend fun pushPending() {
        val pending = dao.pending()
        if (pending.isEmpty()) return

        val cleared = mutableListOf<String>()

        for (e in pending) {
            val body = MemePost(
                userId = e.userId,
                title = e.title,
                imageUrl = e.imageUrl,
                tags = e.tags,
                upvotes = e.upvotes
            )
            runCatching {
                val created = api.createMeme(body)     // POST /memes
                dao.upsert(
                    e.copy(
                        id = created._id,
                        createdAt = parseMillis(created.createdAt),
                        pendingSync = false
                    )
                )
                cleared += e.id
            }
        }
        if (cleared.isNotEmpty()) dao.clearPending(cleared)
    }

    private fun parseMillis(createdAt: String?): Long =
        try { createdAt?.let { Instant.parse(it).toEpochMilli() } ?: System.currentTimeMillis() }
        catch (_: DateTimeParseException) { System.currentTimeMillis() }
}
