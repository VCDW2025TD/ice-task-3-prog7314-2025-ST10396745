package com.example.memestreamproto.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.memestreamproto.data.MemeRepository

/**
 * Runs in the background to push pending local memes to the server.
 */
class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val repo = MemeRepository(applicationContext)
            repo.pushPending()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry() // try again later if network/server issue
        }
    }
}
