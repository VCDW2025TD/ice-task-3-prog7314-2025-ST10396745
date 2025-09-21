package com.example.memestreamproto.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemeDao {

    @Query("SELECT * FROM memes ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<MemeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<MemeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: MemeEntity)

    @Query("SELECT * FROM memes WHERE pendingSync = 1")
    suspend fun pending(): List<MemeEntity>

    // clean up any leftover local placeholder rows
    @Query("DELETE FROM memes WHERE id IN (:localIds)")
    suspend fun clearPending(localIds: List<String>)
}
