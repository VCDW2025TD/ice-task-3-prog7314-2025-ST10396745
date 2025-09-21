package com.example.memestreamproto.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

object SyncScheduler {
    private const val UNIQUE_NAME = "memes-sync-once"

    fun enqueueNow(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val req = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(UNIQUE_NAME, ExistingWorkPolicy.REPLACE, req)
    }
}
