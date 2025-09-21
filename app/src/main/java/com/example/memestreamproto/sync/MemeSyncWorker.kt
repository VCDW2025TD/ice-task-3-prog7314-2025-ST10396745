package com.example.memestreamproto.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.NetworkType
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.memestreamproto.data.MemeRepository
import java.util.concurrent.TimeUnit

class MemeSyncWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = try {
        val repo = MemeRepository(applicationContext)
        repo.pushPending()
        Result.success()
    } catch (t: Throwable) {
        Result.retry()
    }

    companion object {
        private const val UNIQUE_NAME = "memes-periodic-sync"

        fun schedule(context: Context) {
            val req = PeriodicWorkRequestBuilder<MemeSyncWorker>(15, TimeUnit.MINUTES)
                .setConstraints(
                    androidx.work.Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(UNIQUE_NAME, ExistingPeriodicWorkPolicy.UPDATE, req)
        }
    }
}
